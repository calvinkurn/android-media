package com.tokopedia.topads.headline.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsBaseDetailActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsProductIklanFragment
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.headline.view.fragment.TopAdsHeadlineKeyFragment
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_headline_detail_layout.*
import kotlinx.android.synthetic.main.topads_dash_headline_detail_view_widget.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 16/10/20.
 */

class TopAdsHeadlineAdDetailViewActivity : TopAdsBaseDetailActivity(), HasComponent<TopAdsDashboardComponent>, CompoundButton.OnCheckedChangeListener{

    private var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = ""
    private var groupStatus: String? = ""
    private var groupName: String? = ""
    private var priceDaily = 0
    private var groupTotal = 0

    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE

    companion object {
        private const val ACTIVE = "1"
        private const val TIDAK_TAMPIL = "2"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {

        ViewModelProvider(this, viewModelFactory).get(GroupDetailViewModel::class.java)
    }

    private lateinit var detailPagerAdapter: TopAdsDashboardBasePagerAdapter

    private fun renderTabAndViewPager() {
        viewPagerHeadline.adapter = getViewPagerAdapter()
        viewPagerHeadline.offscreenPageLimit = 2
        viewPagerHeadline.currentItem = 0
        tab_layout?.setupWithViewPager(viewPagerHeadline)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        val bundle = Bundle()
        bundle.putInt(GROUP_ID, groupId ?: 0)
        list.add(FragmentTabItem(KATA_KUNCI, TopAdsHeadlineKeyFragment.createInstance(bundle)))
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(supportFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_headline_detail_layout
    }

    override fun loadChildStatisticsData() {
        loadData()
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        getBundleArguments()
        loadData()
        loadStatisticsData()
        renderTabAndViewPager()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
            loadStatisticsData()
        }
        header_toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
        app_bar_layout_2?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
            when {
                offset == 0 -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.EXPANDED) {
                        onStateChanged(TopAdsProductIklanFragment.State.EXPANDED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.EXPANDED
                }
                abs(offset) >= appBarLayout.totalScrollRange -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.COLLAPSED) {
                        onStateChanged(TopAdsProductIklanFragment.State.COLLAPSED)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.COLLAPSED
                }
                else -> {
                    if (mCurrentState != TopAdsProductIklanFragment.State.IDLE) {
                        onStateChanged(TopAdsProductIklanFragment.State.IDLE)
                    }
                    mCurrentState = TopAdsProductIklanFragment.State.IDLE
                }
            }
        })
    }

    private fun loadData() {
        viewModel.getGroupInfo(resources, groupId.toString(), ::onSuccessGroupInfo)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.priceDaily
        group_name.text = groupName
        btn_switch.setOnCheckedChangeListener(null)
        btn_switch.isChecked = data.status == ACTIVE || data.status == TIDAK_TAMPIL
        btn_switch.setOnCheckedChangeListener(this)
        if (priceDaily == 0) {
            progress_status1.text = TopAdsDashboardConstant.TIDAK_DIBATASI
            progress_status2.visibility = View.GONE
            progress_bar.visibility = View.GONE
        } else {
            progress_status2.visibility = View.VISIBLE
            progress_status2.text = String.format(resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status), priceDaily)
            progress_status1.text = priceSpent
            progress_bar.visibility = View.VISIBLE
            try {
                priceSpent = null
                Utils.convertMoneyToValue(priceSpent ?: "0").let {
                    progress_bar.setValue(it, false)
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                loadData()
        }
    }

    private fun getBundleArguments() {
        groupId = intent?.extras?.getInt(GROUP_ID)
        priceSpent = intent?.extras?.getString(TopAdsDashboardConstant.PRICE_SPEND)
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, TopAdsStatisticsType.HEADLINE_ADS, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipe_refresh_layout.isRefreshing = false
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashStatisticFragment) {
            fragment.showLineGraph(this.dataStatistic)
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()

    private fun initInjector() {
        DaggerTopAdsDashboardComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    fun setKeywordCount(size: Int) {
        detailPagerAdapter.setTitle(String.format(getString(R.string.topads_dash_keyword_count), size), CONST_0)
    }

    fun setNegKeywordCount(size: Int) {
        detailPagerAdapter.setTitle(String.format(getString(R.string.topads_dash_neg_key_count), size), CONST_1)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        setResult(Activity.RESULT_OK)
        when {
            isChecked -> viewModel.setGroupAction(ACTION_ACTIVATE, listOf(groupId.toString()), resources)
            else -> viewModel.setGroupAction(ACTION_DEACTIVATE, listOf(groupId.toString()), resources)
        }
    }
}



