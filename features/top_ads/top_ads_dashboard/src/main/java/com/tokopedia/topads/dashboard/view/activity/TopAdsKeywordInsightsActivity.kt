package com.tokopedia.topads.dashboard.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.DATA_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INSIGHT_DATA_HEADER
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INVALID_KEYWORD_TAG
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEY_INSIGHT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_NEG
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.REQUEST_FROM_POS
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyBidFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyNegFragment
import com.tokopedia.topads.dashboard.view.fragment.insight.TopAdsInsightKeyPosFragment
import com.tokopedia.topads.dashboard.view.listener.TopAdsInsightView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsInsightPresenter
import com.tokopedia.topads.dashboard.view.sheet.GroupSelectInsightSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_insight_key_activity_base_layout.*
import javax.inject.Inject

/**
 * Created by Pika on 21/7/20.
 */

const val EVENT_CLICK_KATA_KUNCI_BARU = "click - kata kunci baru"
const val EVENT_CLICK_KATA_KUNCI_NEGATIF = "click - kata kunci negatif"
const val EVENT_CLICK_BIAYA_KATA_KUNCI = "click - biaya kata kunci"
const val EVENT_CLICK_SEMUA_KATA_KUNCI_BARU = "click - tambahkan semua kata kunci baru"
const val EVENT_CLICK_SEMUA_KATA_KUNCI_NEGATIF = "click - tambahkan semua kata kunci negatif"
const val EVENT_CLICK_SEMUA_BIAYA_KATA_KUNCI = "click - terapkan semua rekomendasi biaya kata kunci"
const val EVENT_LIST_KATA_KUNCI_BARU = "list semua rekomendasi kata kunci baru"
const val EVENT_LIST_KATA_KUNCI_NEGATIF = "list semua rekomendasi kata kunci negatif"
const val ACTION_CLICK_TAMBAHKAN_KATA_KUNCI = "click - tambahkan kata kunci baru"
const val ACTION_CLICK_TAMBAHKAN_KATA_NEGATIF = "click - tambahkan kata kunci negatif"
const val ACTION_CLICK_TAMBAHKAN_BIAYA_KATA_KUNCI = "click - terapkan biaya kata kunci"
const val LABEL_KATA_KUNCI_BARU_TERPILIH = "kata kunci baru terpilih"
const val LABEL_KATA_KUNCI_NEGATIF_TERPILIH = "kata kunci negatif terpilih"
class TopAdsKeywordInsightsActivity : BaseActivity(), HasComponent<TopAdsDashboardComponent>, TopAdsInsightView, TopAdsInsightKeyPosFragment.SetCount, TopAdsInsightKeyNegFragment.OnKeywordAdded, TopAdsInsightKeyBidFragment.OnKeywordBidAdded {

    @Inject
    lateinit var topAdsInsightPresenter: TopAdsInsightPresenter
    private var currentGroupId: String = ""
    lateinit var adapter: TopAdsDashboardBasePagerAdapter
    var data: InsightKeyData? = null
    private var keyList: MutableList<String> = mutableListOf()
    private var requestFrom: String = REQUEST_FROM_POS
    private var countToAdd: Int = 1
    private var currentTabPosition = 0

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        setContentView(R.layout.topads_dash_insight_key_activity_base_layout)
        topAdsInsightPresenter.attachView(this)
        fetchData()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        tabUnify.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                view_pager.setCurrentItem(tab?.position?:0, true)
                var eventAction: String = ""
                val eventLabel = currentGroupId
                when (tab?.position?:0) {
                    0 -> {
                        eventAction = EVENT_CLICK_KATA_KUNCI_BARU
                    }
                    1 -> {
                        eventAction = EVENT_CLICK_KATA_KUNCI_NEGATIF
                    }
                    2 -> {
                        eventAction = EVENT_CLICK_BIAYA_KATA_KUNCI
                    }
                }
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightGtmEvent(eventAction, eventLabel, userSession.userId)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

        changeSelectedGroup.setOnClickListener {
            data?.let {
                val sheet = GroupSelectInsightSheet(it, currentGroupId)
                sheet.show(supportFragmentManager, "")
                sheet.selectedGroup = { position, groupId ->
                    sheet.dismiss()
                    currentGroupId = groupId
                    renderViewPager(it)
                }
            }
        }
        header_toolbar?.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }

    private fun renderViewPager(it: InsightKeyData) {
        view_pager?.adapter = getViewPagerAdapter(it)
        view_pager?.currentItem = currentTabPosition
        tabUnify?.getUnifyTabLayout()?.getTabAt(currentTabPosition)?.select()
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    private fun getViewPagerAdapter(data: InsightKeyData): TopAdsDashboardBasePagerAdapter? {
        val list: ArrayList<FragmentTabItem> = arrayListOf()
        val bundle = Bundle()
        currentGroupId = if (currentGroupId.isEmpty()) {
            intent.getStringExtra(KEY_INSIGHT) ?: ""
        } else
            currentGroupId
        currentTabPosition = tabUnify.getUnifyTabLayout().selectedTabPosition
        bundle.putString(KEY_INSIGHT, currentGroupId)
        selectGroup.text = data.data[currentGroupId]?.name
        bundle.putParcelable(INSIGHT_DATA_HEADER, data.header)
        bundle.putSerializable(DATA_INSIGHT, data.data)
        tabUnify?.getUnifyTabLayout()?.removeAllTabs()
        tabUnify?.addNewTab(getString(R.string.topads_insight_pos_key_ini))
        tabUnify?.addNewTab(getString(R.string.topads_insight_neg_key_ini))
        tabUnify?.addNewTab(getString(R.string.topads_insight_bid_ini))
        list.add(FragmentTabItem(getString(R.string.topads_insight_pos_key_ini), TopAdsInsightKeyPosFragment.createInstance(bundle)))
        list.add(FragmentTabItem(getString(R.string.topads_insight_neg_key_ini), TopAdsInsightKeyNegFragment.createInstance(bundle)))
        list.add(FragmentTabItem(getString(R.string.topads_insight_bid_ini), TopAdsInsightKeyBidFragment.createInstance(bundle)))
        adapter = TopAdsDashboardBasePagerAdapter(supportFragmentManager, 0)
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
        tabUnify?.getUnifyTabLayout()?.getTabAt(0)?.setCustomText(String.format(resources.getString(R.string.topads_insight_pos_key), sizePos))
        tabUnify?.getUnifyTabLayout()?.getTabAt(1)?.setCustomText(String.format(resources.getString(R.string.topads_insight_neg_key), sizeNeg))
        tabUnify?.getUnifyTabLayout()?.getTabAt(2)?.setCustomText(String.format(resources.getString(R.string.topads_insight_bid), sizeBid))
    }

    override fun onButtonClicked(mutationData: List<MutationData>, groupId: String, countToAdd: Int, forAllButton: Boolean) {
        fetchData()
        currentGroupId = groupId
        val query = data?.header?.btnAction?.insight ?: ""
        this.countToAdd = countToAdd
        topAdsInsightPresenter.topAdsCreated(groupId, query, mutationData)
        var eventAction = ""
        var eventLabel = ""
        if (forAllButton) {
            when (requestFrom) {
                REQUEST_FROM_POS -> {
                    eventAction = EVENT_CLICK_SEMUA_KATA_KUNCI_BARU
                    eventLabel = "$currentGroupId-$EVENT_LIST_KATA_KUNCI_BARU"

                }
                REQUEST_FROM_NEG -> {
                    eventAction = EVENT_CLICK_SEMUA_KATA_KUNCI_NEGATIF
                    eventLabel = "$currentGroupId-$EVENT_LIST_KATA_KUNCI_NEGATIF"
                }
                REQUEST_FROM_BID -> {
                    eventAction = EVENT_CLICK_SEMUA_BIAYA_KATA_KUNCI
                    eventLabel = currentGroupId
                }
            }
        } else {
            when (requestFrom) {
                REQUEST_FROM_POS -> {
                    eventAction = ACTION_CLICK_TAMBAHKAN_KATA_KUNCI
                    eventLabel = "$currentGroupId-$LABEL_KATA_KUNCI_BARU_TERPILIH"

                }
                REQUEST_FROM_NEG -> {
                    eventAction = ACTION_CLICK_TAMBAHKAN_KATA_NEGATIF
                    eventLabel = "$currentGroupId-$LABEL_KATA_KUNCI_NEGATIF_TERPILIH"
                }
                REQUEST_FROM_BID -> {
                    eventAction = ACTION_CLICK_TAMBAHKAN_BIAYA_KATA_KUNCI
                    eventLabel = currentGroupId
                }
            }
        }
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendInsightGtmEvent(eventAction, eventLabel, userSession.userId)
    }

    override fun onButtonClickedNeg(data: List<MutationData>, groupId: String, countToAdd: Int, forAllButton: Boolean) {
        requestFrom = REQUEST_FROM_NEG
        onButtonClicked(data, groupId, countToAdd, forAllButton)
    }

    override fun onButtonClickedBid(data: List<MutationData>, groupId: String, countToAdd: Int, forAllButton: Boolean) {
        requestFrom = REQUEST_FROM_BID
        onButtonClicked(data, groupId, countToAdd, forAllButton)
    }

    private fun fetchData() {
        topAdsInsightPresenter.getInsight(resources)
    }
}