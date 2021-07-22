package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BID_TYPE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.FROM_DETAIL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TOTAL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEYWORD_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.MAX_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.MIN_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NAME_EDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NEG_KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PARAM_DAILY_BUDGET
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.PRODUK
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SUGGESTION_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.FragmentTabItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsDashboardBasePagerAdapter
import com.tokopedia.topads.dashboard.view.fragment.*
import com.tokopedia.topads.dashboard.view.interfaces.ChangePlacementFilter
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.topads_dash_detail_view_widget.*
import kotlinx.android.synthetic.main.topads_dash_fragment_group_detail_view_layout.*
import java.util.HashMap
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Pika on 1/6/20.
 */

private const val CLICK_DATE_FILTER = "click - date filter"
private const val GROUP_DETAIL_PAGE = "android.group_detail"
private const val VIEW_GROUP_IKLAN = "view - detail group iklan"
private const val CLICK_TOGGLE_ON = "click - toggle on iklan"
private const val EDIT_BIAYA_PENCERIAN = "click - edit bid pencarian"
private const val EDIT_BIAYA_REKOMENDASI = "click - edit bid rekomendasi"
private const val CLICK_GRAPH_SECTOR_1 = "click - graph selector 1"
private const val CLICK_GRAPH_SECTOR_2 = "click - graph selector 2"
private const val CLICK_GRAPH_SECTOR_3 = "click - graph selector 3"
class TopAdsGroupDetailViewActivity : TopAdsBaseDetailActivity(), HasComponent<TopAdsDashboardComponent>, CompoundButton.OnCheckedChangeListener, ChangePlacementFilter {

    private var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = "0"
    private var groupStatus: String? = ""
    private var groupName: String? = ""
    private var autoBidStatus: String = ""
    private var minSuggestKeyword = "0"
    private var maxSuggestKeyword = "0"
    private var suggestedBid = "0"
    private var bidType = "search"
    private var searchBid: Float? = 0.0F
    private var rekommendedBid: Float? = 0.0F
    private var bidTypeData: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
    private var placementType: Int = 0

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getLayoutId(): Int {
        return R.layout.topads_dash_fragment_group_detail_view_layout
    }

    override fun loadChildStatisticsData() {
        loadData()
        loadStatisticsData()
    }

    override fun renderGraph(position: Int) {
        when(position) {
            0 -> {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                    CLICK_GRAPH_SECTOR_1, dataStatistic?.summary?.clickSum.toString())
            }
            1 -> {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                    CLICK_GRAPH_SECTOR_2, dataStatistic?.summary?.impressionSumFmt.toString())
            }
            2 -> {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                    CLICK_GRAPH_SECTOR_3, dataStatistic?.summary?.costSumFmt.toString())
            }
        }
        currentStatisticsFragment?.showLineGraph(dataStatistic)
    }


    override fun handleDateClick(customDateText: String) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(CLICK_DATE_FILTER, customDateText)
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
        if(autoBidStatus.isEmpty() && placementType != 3) {
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
        bundle.putInt("placementType", placementType)
        bundle.putString(TopAdsDashboardConstant.GROUP_STRATEGY, autoBidStatus)
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
        header_toolbar.addRightIcon(0).apply {
            clearImage()
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.EDIT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)))
            setOnClickListener {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                    putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, autoBidStatus)
                }
                startActivityForResult(intent, EDIT_GROUP_REQUEST_CODE)
            }
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

        editpancarianBudget.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                EDIT_BIAYA_PENCERIAN, "")
            val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(false))
            sheet.show(supportFragmentManager, "")
            sheet.onSaved = { bid, pos ->
                saveBidData(bid, "search")
            }
        }

        editRekomendasiBudget.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                EDIT_BIAYA_REKOMENDASI, "")
            val sheet = TopAdsEditKeywordBidSheet.createInstance(prepareBundle(true))
            sheet.show(supportFragmentManager, "")
            sheet.onSaved = { bid, pos ->
                saveBidData(bid, "browse")
            }
        }
    }

    private fun saveBidData(bid: String, bidType: String) {
        val dataMap = HashMap<String, Any?>()
        this.bidType = bidType
        try {
            dataMap[ParamObject.ACTION_TYPE] = ParamObject.ACTION_EDIT
            dataMap["groupName"] = groupName
            dataMap[GROUPID] = groupId
            dataMap[NAME_EDIT] = true
            if (this.bidType == "browse") {
                bidTypeData?.clear()
                bidTypeData?.add(
                    TopAdsBidSettingsModel(
                        "product_browse",
                        bid.toFloat()
                    )
                )
                bidTypeData?.add(
                        TopAdsBidSettingsModel(
                            "product_search",
                            searchBid
                        )
                        )

            } else {
                bidTypeData?.clear()
                bidTypeData?.add(
                    TopAdsBidSettingsModel(
                        "product_search",
                        bid.toFloat()
                    )
                )
                bidTypeData?.add(
                    TopAdsBidSettingsModel(
                        "product_browse",
                        rekommendedBid
                    )
                )
            }
            dataMap[BID_TYPE] = bidTypeData
        } catch (e: NumberFormatException) {
        }
        viewModel.topAdsCreated(dataMap, ::onSuccesGroupEdit, ::onErrorGroupEdit)
    }


    private fun onSuccesGroupEdit() {
        var successMessage = if (bidType == "search") {
            getString(com.tokopedia.topads.common.R.string.bid_edit_search_successful)
        } else {
            getString(com.tokopedia.topads.common.R.string.bid_edit_browse_successful)
        }
        Toaster.build(
            findViewById(android.R.id.content),
            successMessage,
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_NORMAL
        ).show()
    }

    private fun onErrorGroupEdit(error: String?) {
        error?.let {
            Toaster.build(
                findViewById(android.R.id.content),
                it,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL
            ).show()
        }
    }

    private fun loadData() {
        viewModel.getGroupInfo(resources, groupId.toString(), GROUP_DETAIL_PAGE, ::onSuccessGroupInfo)
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString())
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(VIEW_GROUP_IKLAN, "")
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.daiyBudget
        if(data.strategies.isNotEmpty()) {
            autoBidStatus = data.strategies[0]
            per_click.visibility = View.GONE
            per_click_rekomendasi.visibility = View.GONE
            editpancarianBudget.visibility = View.GONE
            editRekomendasiBudget.visibility = View.GONE
            budgetPerClick.text = getString(com.tokopedia.topads.common.R.string.autobid_otomatis)
            budgetPerClick_rekomendasi.text = getString(com.tokopedia.topads.common.R.string.autobid_otomatis)
        } else {
            editpancarianBudget.visibility = View.VISIBLE
            editRekomendasiBudget.visibility = View.VISIBLE
            autoBidStatus = ""
            per_click.visibility = View.VISIBLE
            per_click_rekomendasi.visibility = View.VISIBLE
            data.bidSettings?.forEach {
                if(it.bidType.equals("product_search")) {
                    budgetPerClick.text = "Rp " + it.priceBid
                    searchBid = it.priceBid
                } else if(it.bidType.equals("product_browse")) {
                    budgetPerClick_rekomendasi.text = "Rp " + it.priceBid
                    rekommendedBid = it.priceBid
                }

            }

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
            Utils.convertMoneyToValue(priceSpent ?: "0").let {
                progress_bar.setValue(it, false)
            }
        }
        getBidForKeywords()
        renderTabAndViewPager()
    }

    private fun getBidForKeywords() {
        val suggestions = java.util.ArrayList<DataSuggestions>()
        val dummyId: MutableList<String> = mutableListOf()
        dummyId.add(groupId.toString())
        suggestions.add(DataSuggestions("group", dummyId))
        viewModel.getBidInfo(suggestions, this::onSuccessSuggestion)
    }


    private fun onSuccessSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            minSuggestKeyword = it.minBid
            maxSuggestKeyword = it.maxBid
            suggestedBid = it.suggestionBid
        }
    }


    private fun prepareBundle(forRekomenDasi : Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(MAX_BID, maxSuggestKeyword)
        bundle.putString(MIN_BID, minSuggestKeyword)
        bundle.putString(SUGGESTION_BID, suggestedBid)
        if(forRekomenDasi)
            bundle.putString(KEYWORD_NAME, getString(com.tokopedia.topads.common.R.string.topads_group_detail_budget_rekomendasi))
        else
            bundle.putString(KEYWORD_NAME, getString(com.tokopedia.topads.common.R.string.topads_group_detail_budget_pancarian))
        bundle.putBoolean(FROM_DETAIL, true)
        bundle.putString(ParamObject.GROUPID, groupId.toString())
        return bundle
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
            isChecked -> {
                viewModel.setGroupAction(ACTION_ACTIVATE, listOf(groupId.toString()), resources)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                    CLICK_TOGGLE_ON, "")
            }
            else -> viewModel.setGroupAction(ACTION_DEACTIVATE, listOf(groupId.toString()), resources)
        }
    }

    override fun getSelectedFilter(placementType: Int) {
        this.placementType = placementType
        loadData()
    }
}



