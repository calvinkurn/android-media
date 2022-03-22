package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.DAILY_BUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.ISWHITELISTEDUSER
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.sheet.TopAdsEditKeywordBidSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_BROWSE
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_SEARCH
import com.tokopedia.topads.common.view.TopadsAutoBidSwitchPartialLayout
import com.tokopedia.topads.common.view.sheet.BidInfoBottomSheet
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_ACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DEACTIVATE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.BID_TYPE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.FROM_DETAIL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.FROM_REKOMENDASI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_TOTAL
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KATA_KUNCI
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.KEYWORD_NAME
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.MAX_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.MIN_BID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NAME_EDIT
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.NEG_KATA_KUNCI
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
import com.tokopedia.topads.dashboard.view.sheet.BidSwitchManualBudgetBottomSheet
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
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
private const val CLICK_TAB_PRODUK = "click - tab produk"
private const val CLICK_TAB_KATA_KUNCI = "click - tab kata kunci"
private const val CLICK_TAB_NEG_KATA_KUNCI = "click - tab kata kunci negatif"
private const val CLICK_GROUP_EDIT_ICON = "click - edit form"
class TopAdsGroupDetailViewActivity : TopAdsBaseDetailActivity(), HasComponent<TopAdsDashboardComponent>, CompoundButton.OnCheckedChangeListener, ChangePlacementFilter {

    private lateinit var switchAutoBidLayout : TopadsAutoBidSwitchPartialLayout
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout
    private lateinit var viewPagerFrag : ViewPager
    private lateinit var appBarLayout2 : AppBarLayout
    private lateinit var collapsingToolbar2 : CollapsingToolbarLayout
    private lateinit var headerToolbar : HeaderUnify
    private lateinit var graphLayout : CardUnify
    private lateinit var topadsDashboardContent : LinearLayout
    private lateinit var tabLayout : TabsUnify
    private lateinit var editPancarianBudget : IconUnify
    private lateinit var editRekomendasiBudget : IconUnify
    private lateinit var perClickRekomendasi : Typography
    private lateinit var budgetperclickRekomendasi : Typography
    private lateinit var biayaRekommendasi : Typography
    private lateinit var biayaPencarian : Typography
    private lateinit var pager : ViewPager
    private lateinit var perClick : Typography
    private lateinit var budgetPerClick : Typography
    private lateinit var btnSwitch : SwitchUnify
    private lateinit var txtGroupName : Typography
    private lateinit var dailyBudgetSpent : Typography
    private lateinit var dailyBudget : Typography
    private lateinit var dailyBudgetProgressBar : ProgressBarUnify

    private var dataStatistic: DataStatistic? = null
    private var selectedStatisticType: Int = 0
    private var groupId: Int? = 0
    private var priceSpent: String? = "0"
    private var isWhiteListedUser: Boolean = false
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
    private val bidSwitchManualBottomSheet by lazy(LazyThreadSafetyMode.NONE) {
        BidSwitchManualBudgetBottomSheet(maxSuggestKeyword,minSuggestKeyword,suggestedBid,::onSaveClickedInManualBottomSheet)
    }
    private val bidInfoBottomSheet by lazy(LazyThreadSafetyMode.NONE) { BidInfoBottomSheet() }

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

    private var priceDaily = 0.0F
    private var groupTotal = 0
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
        viewPagerFrag.adapter = getViewPagerAdapter()
        viewPagerFrag.offscreenPageLimit = 3
        viewPagerFrag.currentItem = 0
        tabLayout.setupWithViewPager(viewPagerFrag)
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        val list: MutableList<FragmentTabItem> = mutableListOf()
        tabLayout?.getUnifyTabLayout()?.removeAllTabs()
        tabLayout?.addNewTab(PRODUK)
        if(autoBidStatus.isEmpty()) {
            tabLayout?.addNewTab(KATA_KUNCI)
            tabLayout?.addNewTab(NEG_KATA_KUNCI)
            tabLayout?.customTabMode = TabLayout.MODE_FIXED
        } else {
            tabLayout?.customTabMode = TabLayout.MODE_SCROLLABLE
        }
        val bundle = Bundle()
        bundle.putInt(GROUP_ID, groupId ?: 0)
        bundle.putString(GROUP_NAME, groupName)
        bundle.putInt(GROUP_TOTAL, groupTotal)
        bundle.putInt("placementType", placementType)
        bundle.putBoolean(ISWHITELISTEDUSER, isWhiteListedUser)
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

        initView()
        setClick()

        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        getBundleArguments()
        loadData()
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
        headerToolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
        headerToolbar.addRightIcon(0).apply {
            clearImage()
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.EDIT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)))
            setOnClickListener {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                    CLICK_GROUP_EDIT_ICON, "")
                val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, 2)
                    putExtra(TopAdsDashboardConstant.GROUPID, groupId.toString())
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, autoBidStatus)
                    putExtra(ISWHITELISTEDUSER, isWhiteListedUser)
                }
                startActivityForResult(intent, EDIT_GROUP_REQUEST_CODE)
            }
        }
        appBarLayout2.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
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

        editPancarianBudget.setOnClickListener {
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

        tabLayout?.getUnifyTabLayout()?.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position) {
                    CONST_0 -> {
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(CLICK_TAB_PRODUK, "")
                    }
                    CONST_1 -> {
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(CLICK_TAB_KATA_KUNCI, "")
                    }
                    CONST_2 -> {
                        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(
                            CLICK_TAB_NEG_KATA_KUNCI, "")
                    }
                }
            }
        })
    }

    private fun setClick() {
        switchAutoBidLayout?.let {
            it.onCheckBoxStateChanged = { isAutomatic ->
                if (isAutomatic) {
                    saveBidStateChangedData(true)
                }
                else {
                    //changing switch state to true(otomatic), will be changing to false if user saves bid in bottom sheet
                    switchAutoBidLayout.switchBidEditKeyword?.isChecked = true
                    bidSwitchManualBottomSheet.show(supportFragmentManager, "")
                }
            }

            it.onInfoClicked = {
                bidInfoBottomSheet.show(supportFragmentManager, "")
            }
        }
    }

    private fun saveBidStateChangedData(
        isAutomatic: Boolean, suggestedBid: String = "",
        bidPencarian: String = "", bidRecomendasi: String = "",
    ) {
        viewModel.changeBidState(
            isAutomatic, groupId ?: 0,
            suggestedBid.toFloatOrZero(),
            bidPencarian.toFloatOrZero(),
            bidRecomendasi.toFloatOrZero()
        ) {
            loadData()
        }
    }

    private fun onSaveClickedInManualBottomSheet(bidPencarian: String, bidRecomendasi: String) {
        switchAutoBidLayout.switchBidEditKeyword?.isChecked = false
        saveBidStateChangedData(false,suggestedBid,bidPencarian,bidRecomendasi)
    }

    private fun initView() {
        switchAutoBidLayout = findViewById(R.id.switchAutoBidLayout)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        pager = findViewById(R.id.pager)
        perClick = findViewById(R.id.per_click)
        budgetPerClick = findViewById(R.id.budgetPerClick)
        viewPagerFrag = findViewById(R.id.view_pager_frag)
        appBarLayout2 = findViewById(R.id.app_bar_layout_2)
        collapsingToolbar2 = findViewById(R.id.collapsing_toolbar_2)
        headerToolbar = findViewById(R.id.header_toolbar)
        graphLayout = findViewById(R.id.graph_layout)
        topadsDashboardContent = findViewById(R.id.topads_dashboard_content)
        tabLayout = findViewById(R.id.tab_layout)
        editPancarianBudget = findViewById(R.id.editpancarianBudget)
        editRekomendasiBudget = findViewById(R.id.editRekomendasiBudget)
        perClickRekomendasi = findViewById(R.id.per_click_rekomendasi)
        budgetperclickRekomendasi = findViewById(R.id.budgetPerClick_rekomendasi)
        biayaRekommendasi = findViewById(R.id.biaya_rekommendasi)
        biayaPencarian = findViewById(R.id.biaya_pencarian)
        btnSwitch = findViewById(R.id.btn_switch)
        txtGroupName = findViewById(R.id.group_name)
        dailyBudgetSpent = findViewById(R.id.daily_budget_spent)
        dailyBudget = findViewById(R.id.daily_budget)
        dailyBudgetProgressBar = findViewById(R.id.daily_budget_progress_bar)
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
                bidTypeData?.add(TopAdsBidSettingsModel(PRODUCT_BROWSE, bid.toFloat()))
                bidTypeData?.add(TopAdsBidSettingsModel(PRODUCT_SEARCH, searchBid))
            } else {
                bidTypeData?.clear()
                bidTypeData?.add(TopAdsBidSettingsModel(PRODUCT_SEARCH, bid.toFloat()))
                if (isWhiteListedUser) {
                    bidTypeData?.add(TopAdsBidSettingsModel(PRODUCT_BROWSE, rekommendedBid))
                } else {
                    bidTypeData?.add(TopAdsBidSettingsModel(PRODUCT_BROWSE, bid.toFloat()))
                }
            }
            dataMap[BID_TYPE] = bidTypeData
        } catch (e: NumberFormatException) {
        }

        val settings = listOf(
            GroupEditInput.Group.TopadsSuggestionBidSetting(PRODUCT_SEARCH, suggestedBid.toFloat()),
            GroupEditInput.Group.TopadsSuggestionBidSetting(PRODUCT_BROWSE, suggestedBid.toFloat())
        )
        val dataKey = HashMap<String,Any?>()
        dataKey[ParamObject.SUGGESTION_BID_SETTINGS] = settings
        viewModel.topAdsCreated(dataMap, dataKey, ::onSuccesGroupEdit, ::onErrorGroupEdit)
    }


    private fun onSuccesGroupEdit() {
        val successMessage = if (bidType == "search") {
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
        loadData()
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
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString(), placementType)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsGroupDetailEvent(VIEW_GROUP_IKLAN, "")
        groupStatus = data.status
        groupName = data.groupName
        groupTotal = data.groupTotal.toInt()
        priceDaily = data.daiyBudget

        if(isWhiteListedUser) {
            editRekomendasiBudget.visibility = View.VISIBLE
            perClickRekomendasi.visibility = View.VISIBLE
            budgetperclickRekomendasi.visibility = View.VISIBLE
            biayaRekommendasi.visibility = View.VISIBLE
            biayaPencarian.text = getString(R.string.topads_dash_biaya_pencarian)
        } else {
            editRekomendasiBudget.visibility = View.GONE
            perClickRekomendasi.visibility = View.GONE
            budgetperclickRekomendasi.visibility = View.GONE
            biayaRekommendasi.visibility = View.GONE
            biayaPencarian.text = getString(com.tokopedia.topads.common.R.string.topads_create_bs_title2)
        }
        if(data.strategies.isNotEmpty()) {
            autoBidStatus = data.strategies[0]
            switchAutoBidLayout.switchBidEditKeyword?.isChecked = true
            perClick.visibility = View.GONE
            perClickRekomendasi.visibility = View.GONE
            editPancarianBudget.visibility = View.GONE
            editRekomendasiBudget.visibility = View.GONE
            budgetPerClick.text = getString(com.tokopedia.topads.common.R.string.group_detail_bid_otomatis)
            budgetperclickRekomendasi.text = getString(com.tokopedia.topads.common.R.string.group_detail_bid_otomatis)
        } else {
            switchAutoBidLayout.switchBidEditKeyword?.isChecked = false
            editPancarianBudget.visibility = View.VISIBLE
            autoBidStatus = ""
            perClick.visibility = View.VISIBLE
            data.bidSettings?.forEach {
                if(it.bidType.equals(PRODUCT_SEARCH)) {
                    budgetPerClick.text = "Rp " + it.priceBid?.toInt()
                    searchBid = it.priceBid
                } else if(it.bidType.equals(PRODUCT_BROWSE)) {
                    budgetperclickRekomendasi.text = "Rp " + it.priceBid?.toInt()
                    rekommendedBid = it.priceBid
                }

            }

        }
        txtGroupName.text = groupName
        btnSwitch.setOnCheckedChangeListener(null)
        btnSwitch.isChecked = data.status == "published"
        btnSwitch.setOnCheckedChangeListener(this)
        if (priceDaily == 0.0F) {
            dailyBudgetSpent.text = TopAdsDashboardConstant.TIDAK_DIBATASI
            dailyBudget.visibility = View.GONE
            dailyBudgetProgressBar.visibility = View.GONE
        } else {
            dailyBudget.visibility = View.VISIBLE
            dailyBudget.text = String.format(resources.getString(com.tokopedia.topads.common.R.string.topads_dash_group_item_progress_status), priceDaily.toInt())
            dailyBudgetSpent.text = priceSpent
            dailyBudgetProgressBar.visibility = View.VISIBLE
            Utils.convertMoneyToValue(priceSpent ?: "0").let {
                dailyBudgetProgressBar.setValue(it, false)
            }
        }
        renderTabAndViewPager()
    }

    fun getBidForKeywords(list: MutableList<String>) {
        val suggestions = java.util.ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("", listOf(groupId.toString())))
        viewModel.getBidInfo(suggestions, GROUP_DETAIL_PAGE, this::onSuccessSuggestion)
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
        if(forRekomenDasi) {
            bundle.putString(
                KEYWORD_NAME,
                getString(com.tokopedia.topads.common.R.string.topads_group_detail_budget_rekomendasi)
            )
            bundle.putString(DAILY_BUDGET, budgetperclickRekomendasi.text.toString().removeCommaRawString())
            bundle.putBoolean(FROM_REKOMENDASI, true)
        }
        else {
            bundle.putString(
                KEYWORD_NAME,
                getString(com.tokopedia.topads.common.R.string.topads_group_detail_budget_pancarian)
            )
            bundle.putString(DAILY_BUDGET, budgetPerClick.text.toString().removeCommaRawString())
            bundle.putBoolean(FROM_REKOMENDASI, false)
        }
        bundle.putBoolean(FROM_DETAIL, true)
        bundle.putString(GROUPID, groupId.toString())
        return bundle
    }

    private fun onStateChanged(state: TopAdsProductIklanFragment.State?) {
        swipeRefreshLayout.isEnabled = state == TopAdsProductIklanFragment.State.EXPANDED
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
        groupId = intent?.extras?.getString(GROUP_ID)?.toIntOrZero()
        priceSpent = intent?.extras?.getString(TopAdsDashboardConstant.PRICE_SPEND)
        isWhiteListedUser = intent?.extras?.getBoolean(ISWHITELISTEDUSER)?:false
    }

    private fun loadStatisticsData() {
        if (startDate == null || endDate == null) return
        viewModel.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType, ::onSuccesGetStatisticsInfo, groupId.toString(), placementType)
    }

    private fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        swipeRefreshLayout.isRefreshing = false
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
        tabLayout?.getUnifyTabLayout()?.getTabAt(0)?.setCounter(size)
    }

    fun setKeywordCount(size: Int) {
        tabLayout?.getUnifyTabLayout()?.getTabAt(1)?.setCounter(size)
    }

    fun setNegKeywordCount(size: Int) {
        tabLayout?.getUnifyTabLayout()?.getTabAt(2)?.setCounter(size)
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
        loadStatisticsData()
    }
}



