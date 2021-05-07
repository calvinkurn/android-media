package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TOTAL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NEG_KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.*
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.unifycomponents.setCounter
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_detail_view_widget.*
import kotlinx.android.synthetic.main.topads_dash_fragment_group_detail_view_layout.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 1/6/20.
 */

class TopAdsGroupDetailViewActivity : TopAdsBaseDetailActivity(), HasComponent<TopAdsDashboardComponent>, CompoundButton.OnCheckedChangeListener {

    private var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = ""
    private var groupStatus: String? = ""
    private var groupName: String? = ""
    private var autoBidStatus: String = ""

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_fragment_group_detail_view_layout
    }

    override fun loadChildStatisticsData() {
        loadData()
        loadStatisticsData()
    }

    override fun renderGraph() {
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }

    private var priceDaily = 0
    private var groupTotal = 0
    private val ACTIVE = "1"
    private val TIDAK_TAMPIL = "2"
    private var mCurrentState = TopAdsProductIklanFragment.State.IDLE

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    private lateinit var detailPagerAdapter: TopAdsDashboardBasePagerAdapter

    private fun renderTabAndViewPager() {
        view_pager_frag.adapter = getViewPagerAdapter()
        view_pager_frag.offscreenPageLimit = 3
        view_pager_frag.currentItem = 0
        tab_layout.setupWithViewPager(view_pager_frag)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        tab_layout?.getUnifyTabLayout()?.removeAllTabs()
        tab_layout?.addNewTab(PRODUK)
        if(autoBidStatus.isEmpty()) {
            tab_layout?.addNewTab(KATA_KUNCI)
            tab_layout?.addNewTab(NEG_KATA_KUNCI)
            tab_layout?.customTabMode = TabLayout.MODE_FIXED
        } else {
            tab_layout?.customTabMode = TabLayout.MODE_SCROLLABLE
        }
        val bundle = Bundle()
        bundle.putInt(GROUP_ID, groupId ?: 0)
        bundle.putString(GROUP_NAME, groupName)
        bundle.putInt(GROUP_TOTAL, groupTotal)
        list.add(FragmentTabItem(PRODUK, ProductTabFragment.createInstance(bundle)))
        list.add(FragmentTabItem(KATA_KUNCI, KeywordTabFragment.createInstance(bundle)))
        list.add(FragmentTabItem(NEG_KATA_KUNCI, NegKeywordTabFragment.createInstance(bundle)))
        detailPagerAdapter = TopAdsDashboardBasePagerAdapter(supportFragmentManager, 0)
        detailPagerAdapter.setList(list)
        return detailPagerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        getBundleArguments()
        loadData()
        swipe_refresh_layout.setOnRefreshListener {
            loadData()
        }
        header_toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
        header_toolbar.addRightIcon(com.tokopedia.topads.common.R.drawable.topads_edit_pen_icon).setOnClickListener {

            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
                putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, autoBidStatus)
            }
            startActivityForResult(intent, EDIT_GROUP_REQUEST_CODE)
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
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.priceDaily
        if(data.strategies.isNotEmpty()) {
            autoBidStatus = data.strategies[0]
            per_click.visibility = View.GONE
            budgetPerClick.text = getString(com.tokopedia.topads.common.R.string.autobid_otomatis)
        } else {
            per_click.visibility = View.VISIBLE
            budgetPerClick.text = "Rp " + data.priceBid
        }
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
        renderTabAndViewPager()
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        swipe_refresh_layout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_GROUP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
                setResult(Activity.RESULT_OK)
                loadData()
        }
    }

    private fun getBundleArguments() {
        groupId = intent?.extras?.getInt(GROUP_ID)
        priceSpent = intent?.extras?.getString(TopAdsDashboardConstant.PRICE_SPEND)
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipe_refresh_layout.isRefreshing = false
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null && dataStatistic.cells.isNotEmpty()) {
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

    fun setProductCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    fun setKeywordCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
    }

    fun setNegKeywordCount(size: Int) {
        tab_layout?.getUnifyTabLayout()?.getTabAt(2)?.setCounter(size)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        setResult(Activity.RESULT_OK)
        when {
            isChecked -> viewModel.setGroupAction(ACTION_ACTIVATE, listOf(groupId.toString()), resources)
            else -> viewModel.setGroupAction(ACTION_DEACTIVATE, listOf(groupId.toString()), resources)
        }
    }
}



