package com.tokopedia.topads.dashboard.view.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INSIGHT_DATA_HEADER
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INVALID_KEYWORD_TAG
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_NEG
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_POS
import com.tokopedia.topads.dashboard.data.model.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashInsightKeyPagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyBidFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyNegFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyPosFragment
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightPresenter
import com.tokopedia.topads.dashboard.view.sheet.GroupSelectInsightSheet
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_insight_key_activity_base_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 21/7/20.
 */

class TopAdsKeywordInsightsActivity : BaseActivity(), HasComponent<TopAdsDashboardComponent>, TopAdsInsightView, TopAdsInsightKeyPosFragment.SetCount, TopAdsInsightKeyNegFragment.OnKeywordAdded, TopAdsInsightKeyBidFragment.OnKeywordBidAdded {

    @Inject
    lateinit var topAdsInsightPresenter: TopAdsInsightPresenter
    private var currentGroupId: String = ""
    lateinit var adapter: TopAdsDashInsightKeyPagerAdapter
    lateinit var data: InsightKeyData
    private var keyList: MutableList<String> = mutableListOf()
    private var requestFrom: String = REQUEST_FROM_POS
    private var countToAdd: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.topads_dash_insight_key_activity_base_layout)
        topAdsInsightPresenter.attachView(this)
        topAdsInsightPresenter.getInsight(resources)
        tabUnify.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                view_pager.setCurrentItem(tab!!.position, true)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })
        changeSelectedGroup.setOnClickListener {
            val sheet = GroupSelectInsightSheet(data, currentGroupId)
            sheet.show(supportFragmentManager, "")
            sheet.selectedGroup = { position, groupId ->
                sheet.dismiss()
                currentGroupId = groupId
                renderViewPager(data)
            }
        }
        header_toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun renderViewPager(it: InsightKeyData) {
        view_pager?.adapter = getViewPagerAdapter(it)
        view_pager?.currentItem = 0
        tabUnify.tabLayout.setupWithViewPager(view_pager)
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun getViewPagerAdapter(data: InsightKeyData): TopAdsDashInsightKeyPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        val bundle = Bundle()
        val keyToMap = if (currentGroupId.isEmpty()) {
            intent.getStringExtra(KEY_INSIGHT)
        } else
            currentGroupId
        bundle.putString(KEY_INSIGHT, keyToMap)
        selectGroup.text = data.data[keyToMap]?.name
        bundle.putParcelable(INSIGHT_DATA_HEADER, data.header)
        bundle.putSerializable(DATA_INSIGHT, data.data)
        list.add(TopAdsInsightKeyPosFragment.createInstance(bundle))
        list.add(TopAdsInsightKeyNegFragment.createInstance(bundle))
        list.add(TopAdsInsightKeyBidFragment.createInstance(bundle))
        adapter = TopAdsDashInsightKeyPagerAdapter(supportFragmentManager, 0)
        adapter.setList(list)
        return adapter
    }

    override fun onSuccessKeywordInsight(it: InsightKeyData) {
        data = it
        val list: HashMap<String, KeywordInsightDataMain> = it.data
        list.forEach {
            keyList.add(it.key)
        }
        renderViewPager(it)
        loader.visibility = View.GONE
    }

    override fun onSuccessEditKeywords(it: FinalAdResponse) {
        when (requestFrom) {
            REQUEST_FROM_NEG -> Toaster.make(this.findViewById(android.R.id.content), String.format(getString(R.string.topads_insight_add_negative), countToAdd), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL)
            REQUEST_FROM_BID -> Toaster.make(this.findViewById(android.R.id.content), String.format(getString(R.string.topads_insight_add_bid), countToAdd), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL)
            else -> Toaster.make(this.findViewById(android.R.id.content), String.format(getString(R.string.topads_insight_add_keyword), countToAdd), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL)
        }
        topAdsInsightPresenter.getInsight(resources)
    }

    override fun onErrorEditKeyword(erorr: List<FinalAdResponse.TopadsManageGroupAds.ErrorsItem>?) {
        if (erorr?.get(0)?.detail == INVALID_KEYWORD_TAG) {
            when (requestFrom) {
                REQUEST_FROM_POS -> Toaster.make(this.findViewById(android.R.id.content), getString(R.string.topads_insight_invalid_key_tag_pos), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_ERROR)
                REQUEST_FROM_NEG -> Toaster.make(this.findViewById(android.R.id.content), getString(R.string.topads_insight_invalid_key_tag_neg), TopAdsDashboardConstant.TOASTER_DURATION.toInt(), Toaster.TYPE_ERROR)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        topAdsInsightPresenter.detachView()
    }

    override fun setCount(sizePos: Int, sizeNeg: Int, sizeBid: Int) {
        adapter.setTitle(String.format(resources.getString(R.string.topads_insight_pos_key), sizePos),
                String.format(resources.getString(R.string.topads_insight_neg_key), sizeNeg),
                String.format(resources.getString(R.string.topads_insight_bid), sizeBid))
    }

    override fun onButtonClicked(mutationData: List<MutationData>, groupId: String, countToAdd: Int) {
        currentGroupId = groupId
        val query = data.header.btnAction?.insight ?: ""
        this.countToAdd = countToAdd
        topAdsInsightPresenter.topAdsCreated(groupId, query, mutationData)
    }

    override fun onButtonClickedNeg(data: List<MutationData>, groupId: String, countToAdd: Int) {
        requestFrom = REQUEST_FROM_NEG
        onButtonClicked(data, groupId, countToAdd)
    }

    override fun onButtonClickedBid(data: List<MutationData>, groupId: String, countToAdd: Int) {
        requestFrom = REQUEST_FROM_BID
        onButtonClicked(data, groupId, countToAdd)
    }
}