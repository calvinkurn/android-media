package com.tokopedia.topads.dashboard.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.text.Html
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.design.component.Menus
import com.tokopedia.design.label.LabelView
import com.tokopedia.design.utils.DateLabelUtils
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.datepicker.range.view.activity.DatePickerActivity
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant
import com.tokopedia.design.component.Tooltip
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.topads.common.TopAdsMenuBottomSheets
import com.tokopedia.topads.common.constant.TopAdsAddingOption
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.TopAdsWebViewActivity
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.view.adapter.TopAdsOptionMenuAdapter
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter
import com.tokopedia.topads.dashboard.TopAdsDashboardTracking
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardTrackerConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DashboardPopulateResponse
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.data.utils.TopAdsDatePeriodUtil
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.SellerCenterActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.credit.history.view.activity.TopAdsCreditHistoryActivity
import com.tokopedia.topads.dashboard.view.adapter.TopAdsStatisticPagerAdapter
import com.tokopedia.topads.dashboard.view.adapter.TopAdsTabAdapter
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAutoTopUpActivity
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import kotlinx.android.synthetic.main.fragment_top_ads_dashboard.*
import kotlinx.android.synthetic.main.partial_top_ads_dashboard_statistics.*
import kotlinx.android.synthetic.main.partial_top_ads_shop_info.*
import kotlinx.android.synthetic.main.top_ads_dashboard_empty_layout.*

import java.util.Calendar
import java.util.Date

import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/18.
 */

class TopAdsDashboardFragment : BaseDaggerFragment(), TopAdsDashboardView {
    internal var isShowAutoAddPromo = false
    internal var isAutoTopUpActive = false

    val shopInfoLayout: View?
        get() = view_group_deposit

    val groupSummaryLabelView: LabelView?
        get() = label_view_group_summary

    private val pagerAdapter: TopAdsStatisticPagerAdapter? by lazy {
        val fragmentList = listOf(
                TopAdsStatisticImprFragment.createInstance(),
                TopAdsStatisticKlikFragment.createInstance(),
                TopAdsStatisticSpentFragment.createInstance(),
                TopAdsStatisticIncomeFragment.createInstance(),
                TopAdsStatisticCtrFragment.createInstance(),
                TopAdsStatisticConversionFragment.createInstance(),
                TopAdsStatisticAvgFragment.createInstance(),
                TopAdsStatisticSoldFragment.createInstance())
        context?.run { TopAdsStatisticPagerAdapter(this, childFragmentManager, fragmentList) }

    }
    val contentStatisticsView: View?
        get() = topads_content_statistics

    private val topAdsTabAdapter: TopAdsTabAdapter? by lazy {
        context?.run { TopAdsTabAdapter(this) }
    }

    val viewGroupPromo: View?
        get() = view_group_promo

    private var snackbarRetry: SnackbarRetry? = null

    internal var startDate: Date? = null
    internal var endDate: Date? = null

    internal var dataStatistic: DataStatistic? = null

    @TopAdsStatisticsType
    internal var selectedStatisticType: Int = TopAdsStatisticsType.PRODUCT_ADS

    private var totalProductAd: Int = 0
    private var totalGroupAd: Int = 0

    private var callback: Callback? = null

    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    internal var tracker: TopAdsDashboardTracking? = null
    internal var router: TopAdsDashboardRouter? = null

    val scrollView: ScrollView?
        get() = scroll_view

    val isContentVisible: Boolean
        get() = topads_dashboard_content.visibility == View.VISIBLE

    val buttonAddPromo: View?
        get() = button_topads_add_promo

    protected val currentStatisticsFragment: TopAdsDashboardStatisticFragment?
        get() = pagerAdapter?.instantiateItem(pager, topAdsTabAdapter?.selectedTabPosition ?: 0) as? TopAdsDashboardStatisticFragment

    private val conversionFragment: TopAdsStatisticConversionFragment?
        get() = pagerAdapter?.getItem(5) as? TopAdsStatisticConversionFragment

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.let {
            GraphqlClient.init(it)
            if (it.application is TopAdsDashboardRouter) {
                router = it.application as TopAdsDashboardRouter
                tracker = TopAdsDashboardTracking(router!!)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAdsDashboardPresenter.attachView(this)
        topAdsDashboardPresenter.resetDate()
        selectedStatisticType = TopAdsStatisticsType.PRODUCT_ADS
        totalProductAd = Integer.MIN_VALUE
        val refresh = RefreshHandler(activity, swipe_refresh_layout, RefreshHandler.OnRefreshHandlerListener {
            topAdsDashboardPresenter.clearStatisticsCache()
            topAdsDashboardPresenter.clearTotalAdCache()
            loadData()
        })
        initTicker()
        initShopInfoComponent()
        initSummaryComponent()
        initStatisticComponent()
        initEmptyStateView()
        button_topads_add_promo.button.setOnClickListener {
            activity?.let {
                if (GlobalConfig.isSellerApp()) {
                    startActivityForResult(router?.getTopAdsAddingPromoOptionIntent(it), REQUEST_CODE_AD_OPTION)
                } else {
                    if (isShowAutoAddPromo){
                        startActivity(TopAdsWebViewActivity.createIntent(it, TopAdsDashboardConstant.URL_ONECLICKPROMO))
                    } else {
                        router?.openTopAdsDashboardApplink(it)
                    }
                }}}

        snackbarRetry = NetworkErrorHelper.createSnackbarWithAction(activity) { loadData() }
        snackbarRetry?.setColorActionRetry(ContextCompat.getColor(activity!!, R.color.green_400))
        setHasOptionsMenu(true)
    }

    private fun initTicker() {
        ticker_view.setListMessage(arrayListOf())
        ticker_view.setOnPartialTextClickListener { _, messageClick ->
            context?.let { startActivity(TopAdsWebViewActivity.createIntent(it, messageClick)) }}
        ticker_view.buildView()
    }

    private fun initEmptyStateView() {
        context?.run {
            no_result_image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_top_ads_dashboard_empty))
        }

        text_view_empty_title_text.setText(R.string.topads_dashboard_empty_usage_title)
        text_view_empty_content_text.setText(R.string.topads_dashboard_empty_usage_desc)
        button_add_promo.visibility = View.GONE
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        }
    }

    private fun initStatisticComponent() {
        //label_view_statistics.setOnClickListener { showBottomSheetStatisticTypeOptions() }
        val tabLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerview_tabLayout.layoutManager = tabLayoutManager
        topAdsTabAdapter?.setListener(object : TopAdsTabAdapter.OnRecyclerTabItemClick {
            override fun onTabItemClick(position: Int) {
                pager.currentItem = position
            }
        })
        recyclerview_tabLayout.adapter = topAdsTabAdapter
        val smoothScroller = object : LinearSmoothScroller(activity!!) {
            override fun getHorizontalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }

        pager.offscreenPageLimit = TopAdsDashboardConstant.OFFSCREEN_PAGE_LIMIT
        initTabLayouTitles()
        initTopAdsStatisticPagerAdapter()
        pager.adapter = pagerAdapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                smoothScroller.targetPosition = position
                tabLayoutManager.startSmoothScroll(smoothScroller)
                topAdsTabAdapter?.selected(position)
                trackingStatisticBar(position)
                currentStatisticsFragment?.updateDataStatistic(dataStatistic)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun trackingStatisticBar(position: Int) {
        when (position) {
            0 -> onImpressionSelected()
            1 -> onClickSelected()
            2 -> onCtrSelected()
            3 -> onConversionSelected()
            4 -> onAverageConversionSelected()
            5 -> onCostSelected()
            else -> {
            }
        }
    }

    private fun initTabLayouTitles() {
        topAdsTabAdapter?.setSummary(null, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
    }

    private fun initTopAdsStatisticPagerAdapter() {}

    private fun initSummaryComponent() {
        label_view_group_summary.setOnClickListener { onSummaryGroupClicked() }
        label_view_item_summary.setOnClickListener { onSummaryProductClicked() }
        label_view_keyword.setOnClickListener { onSummaryKeywordClicked() }
    }

    private fun onSummaryKeywordClicked() {
        tracker?.eventTopAdsProductClickKeywordDashboard()
        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                val intent = router?.getTopAdsKeywordListIntent(it)?.apply {
                    if (totalGroupAd >= 0) {
                        putExtra(TopAdsDashboardConstant.EXTRA_TOTAL_GROUP_ADS, totalGroupAd)
                    }
                }
                startActivityForResult(intent, REQUEST_CODE_AD_STATUS)
            } else {
                router?.openTopAdsDashboardApplink(it)
            }
        }

    }

    private fun onSummaryProductClicked() {
        tracker?.eventTopAdsProductClickProductDashboard()
        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                startActivityForResult(router?.getTopAdsProductAdListIntent(it), REQUEST_CODE_AD_STATUS)
            } else {
                router?.openTopAdsDashboardApplink(it)
            }}
    }

    private fun onSummaryGroupClicked() {
        tracker?.eventTopAdsProductClickGroupDashboard()
        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                val intent = router?.getTopAdsGroupAdListIntent(it)?.apply {
                    if (totalProductAd >= 0) {
                        putExtra(TopAdsDashboardConstant.EXTRA_TOTAL_PRODUCT_ADS, totalProductAd)
                    }
                }
                startActivityForResult(intent, REQUEST_CODE_AD_STATUS)
            } else {
                router?.openTopAdsDashboardApplink(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (topAdsDashboardPresenter.isDateUpdated(startDate, endDate)) {
            startDate = topAdsDashboardPresenter.startDate
            endDate = topAdsDashboardPresenter.endDate
            loadData()
        }
    }

    private fun loadData() {
        ticker_view.clearMessage()
        swipe_refresh_layout.isRefreshing = true
        topAdsDashboardPresenter.getAutoTopUpStatus(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        topAdsDashboardPresenter.getPopulateDashboardData(GraphqlHelper.loadRawString(resources, R.raw.gql_get_deposit))
        topAdsDashboardPresenter.getShopInfo()
        topAdsDashboardPresenter.getTickerTopAds(resources)
    }

    protected fun loadStatisticsData() {
        label_view_statistics.setContent(getStatisticsTypeTitle(selectedStatisticType))
        updateLabelDateView(startDate, endDate)
        if (startDate == null || endDate == null) return
        topAdsDashboardPresenter.getTopAdsStatistic(startDate!!, endDate!!, selectedStatisticType)
    }

    private fun getStatisticsTypeTitle(selectedStatisticType: Int): CharSequence {
        val resString = when (selectedStatisticType) {
            TopAdsStatisticsType.ALL_ADS ->  R.string.topads_dashboard_all_promo_menu
            TopAdsStatisticsType.PRODUCT_ADS ->  R.string.top_ads_title_product
            TopAdsStatisticsType.SHOP_ADS ->  R.string.title_top_ads_store
            else -> -1
        }
        return getString(resString)
    }

    private fun initShopInfoComponent() {
        text_view_add_deposit.setOnClickListener { goToAddCredit() }
        date_label_view.setOnClickListener { onDateLayoutClicked() }
        text_view_credit_history.setOnClickListener { goToCreditHistory() }
    }

    private fun goToCreditHistory() {
        context?.let {
            startActivityForResult(TopAdsCreditHistoryActivity.createInstance(it), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    private fun onDateLayoutClicked() {
        if (startDate == null || endDate == null)
            return
        val intent = getDatePickerIntent(activity, startDate!!, endDate!!)
        startActivityForResult(intent, DatePickerConstant.REQUEST_CODE_DATE)
    }

    private fun getDatePickerIntent(context: Context?, start: Date, end: Date): Intent {
        val intent = Intent(context, DatePickerActivity::class.java)
        val maxCalendar = Calendar.getInstance()
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23)
        maxCalendar.set(Calendar.MINUTE, 59)
        maxCalendar.set(Calendar.SECOND, 59)

        val minCalendar = Calendar.getInstance()
        minCalendar.add(Calendar.YEAR, -1)
        minCalendar.set(Calendar.HOUR_OF_DAY, 0)
        minCalendar.set(Calendar.MINUTE, 0)
        minCalendar.set(Calendar.SECOND, 0)
        minCalendar.set(Calendar.MILLISECOND, 0)

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, start.time)
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, end.time)

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.timeInMillis)
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, TopAdsCommonConstant.MAX_DATE_RANGE)

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, TopAdsDatePeriodUtil.getPeriodRangeList(activity!!))
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, topAdsDashboardPresenter.lastSelectionDatePickerIndex)
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, topAdsDashboardPresenter.lastSelectionDatePickerType)

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, activity!!.getString(R.string.title_date_picker))
        return intent
    }

    internal fun goToAddCredit() {
        tracker?.eventTopAdsProductAddBalance()
        val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_ADD_CREDIT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_AD_STATUS && data != null) {
            checkAdChanged(data)
        } else if (requestCode == REQUEST_CODE_ADD_CREDIT) {
            loadData()
        } else if (requestCode == DatePickerConstant.REQUEST_CODE_DATE) {
            if (data != null) {
                handlingResultDateSelection(data)
            }
        } else if (requestCode == REQUEST_CODE_AD_OPTION) {
            if (data != null) {
                val option = data.getIntExtra(TopAdsDashboardConstant.EXTRA_SELECTED_OPTION, -1)
                when (option) {
                    TopAdsAddingOption.GROUP_OPT -> onSummaryGroupClicked()
                    TopAdsAddingOption.PRODUCT_OPT -> gotoCreateProductAd()
                    TopAdsAddingOption.KEYWORDS_OPT -> gotoCreateKeyword()
                    else -> {
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_ADD_PRODUCT && data != null) {
            checkAdChanged(data)
            onSummaryProductClicked()
        } else if (requestCode == REQUEST_CODE_ADD_KEYWORD && data != null) {
            checkAdChanged(data)
            onSummaryKeywordClicked()
        } else if (requestCode == REQUEST_CODE_SET_AUTO_TOPUP && resultCode == Activity.RESULT_OK){
            topAdsDashboardPresenter.getAutoTopUpStatus(GraphqlHelper
                    .loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        }
    }

    private fun checkAdChanged(data: Intent) {
        if (startDate == null || endDate == null) {
            return
        }
        val adStatusChanged = data.getBooleanExtra(TopAdsDashboardConstant.EXTRA_AD_CHANGED, false)
        if (adStatusChanged) {
            topAdsDashboardPresenter.clearTotalAdCache()
            loadData()
        }
    }

    private fun gotoCreateProductAd() {
        topAdsDashboardPresenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_DASHBOARD_PRODUCT)
        activity?.let {
            if (GlobalConfig.isSellerApp()) {
                startActivityForResult(router?.getTopAdsGroupNewPromoIntent(it), REQUEST_CODE_ADD_PRODUCT)
            } else {
                router?.openTopAdsDashboardApplink(it)
            }}
    }

    private fun gotoCreateKeyword() {
        topAdsDashboardPresenter.saveSourceTagging(TopAdsSourceOption.SA_MANAGE_KEYWORD_POSITIVE)
        if (activity == null || router == null) return
        if (GlobalConfig.isSellerApp())
            startActivityForResult(router!!.getTopAdsKeywordNewChooseGroupIntent(activity!!, true, null),
                REQUEST_CODE_ADD_KEYWORD)
        else
            router!!.openTopAdsDashboardApplink(activity!!)
    }

    private fun handlingResultDateSelection(data: Intent) {
        val sDate = data.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1)
        val eDate = data.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1)
        val lastSelection = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1)
        val selectionType = data.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE)
        if (sDate != -1L && eDate != -1L) {
            startDate = Date(sDate)
            endDate = Date(eDate)
            topAdsDashboardPresenter.saveDate(startDate!!, endDate!!)
            topAdsDashboardPresenter.saveSelectionDatePicker(selectionType, lastSelection)
            trackingDateTopAds(lastSelection, selectionType)
            loadStatisticsData()
        }
    }

    private fun trackingDateTopAds(lastSelection: Int, selectionType: Int) {
        if (selectionType == DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE) {
            tracker?.eventTopAdsShopChooseDateCustom()
        } else if (selectionType == DatePickerConstant.SELECTION_TYPE_PERIOD_DATE) {
            when (lastSelection) {
                0 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_TODAY)
                1 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_YESTERDAY)
                2 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_LAST_7_DAY)
                3 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_LAST_1_MONTH)
                4 -> tracker?.eventTopAdsShopDatePeriod(TopAdsDashboardTrackerConstant.PERIOD_OPTION_THIS_MONTH)
                else -> {
                }
            }
        }
    }

    override fun onLoadTopAdsShopDepositError(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onLoadTopAdsShopDepositSuccess(dataDeposit: DataDeposit) {
        snackbarRetry?.hideRetrySnackbar()
        val freeDeposit = dataDeposit.freeDeposit
        text_view_deposit_value.text = dataDeposit.amountFmt

        if (freeDeposit.status in 1..2){
            val valueFmt = if (freeDeposit.status == 2) freeDeposit.usageFmt else freeDeposit.nominalFmt
            if (valueFmt.isNotBlank() && !valueFmt.trim().equals("0")) {
                ticker_view.addMessage(0, getString(R.string.top_ads_template_credit_bonus,
                        valueFmt,
                        freeDeposit.remainingDays.toString() + ""))
                ticker_view.visibility = View.VISIBLE
            }
        }
    }

    override fun onErrorGetShopInfo(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        ImageHandler.LoadImage(image_view_shop_icon, shopInfo.info.shopAvatar)

        text_view_shop_title.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(shopInfo.info.shopName, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(shopInfo.info.shopName)
        }
    }

    override fun onErrorPopulateTotalAds(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onSuccessPopulateTotalAds(totalAd: TotalAd) {
        snackbarRetry?.hideRetrySnackbar()
        swipe_refresh_layout.isRefreshing = false
        totalProductAd = totalAd.totalProductAd
        totalGroupAd = totalAd.totalProductGroupAd
        label_view_group_summary.setContent(totalAd.totalProductGroupAd.toString())
        label_view_item_summary.setContent(totalAd.totalProductAd.toString())
        label_view_keyword.setContent(totalAd.totalKeyword.toString())
    }

    override fun onErrorGetStatisticsInfo(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onSuccesGetStatisticsInfo(dataStatistic: DataStatistic) {
        snackbarRetry?.hideRetrySnackbar()
        this.dataStatistic = dataStatistic
        if (this.dataStatistic != null) {
            topAdsTabAdapter?.setSummary(dataStatistic.summary, resources.getStringArray(R.array.top_ads_tab_statistics_labels))
        }
        val fragment = pager.adapter?.instantiateItem(pager, pager.currentItem) as? Fragment
        if (fragment != null && fragment is TopAdsDashboardStatisticFragment) {
            fragment.updateDataStatistic(this.dataStatistic)
        }
    }

    override fun onErrorPopulateData(throwable: Throwable) {
        swipe_refresh_layout.isRefreshing = false
        snackbarRetry?.showRetrySnackbar()
    }

    override fun onSuccessPopulateData(dashboardPopulateResponse: DashboardPopulateResponse) {
        val isUsageExists = dashboardPopulateResponse.dataDeposit.isAdUsage
        val isAdExists = getTotalAd(dashboardPopulateResponse.totalAd) > 0
        snackbarRetry?.hideRetrySnackbar()
        swipe_refresh_layout.isRefreshing = false
        onLoadTopAdsShopDepositSuccess(dashboardPopulateResponse.dataDeposit)
        if (isUsageExists || isAdExists) {
            onSuccessPopulateTotalAds(dashboardPopulateResponse.totalAd)
            loadStatisticsData()
            topads_dashboard_empty.visibility = View.GONE
            topads_dashboard_content.visibility = View.VISIBLE
        } else {
            topads_dashboard_empty.visibility = View.VISIBLE
            topads_dashboard_content.visibility = View.GONE
            isShowAutoAddPromo = GlobalConfig.isCustomerApp()
        }
    }

    override fun onSuccessGetTicker(message: List<String>) {
        ticker_view.addAllMessage(message)
        ticker_view.visibility = View.VISIBLE
    }

    override fun onErrorGetTicker(e: Throwable) {}

    override fun onSuccessGetAutoTopUpStatus(data: AutoTopUpStatus) {
        isAutoTopUpActive = (data.status.toIntOrNull() ?: 0) != TopAdsDashboardConstant.AUTO_TOPUP_INACTIVE
        text_view_deposit_desc.setDrawableRight(if (isAutoTopUpActive) R.drawable.ic_repeat_green else R.drawable.ic_repeat_grey)
        text_view_deposit_desc.setOnClickListener {
            Tooltip(it.context).apply {
                setTitle(getString(R.string.label_topads_automatic_topup) + ": " + data.statusDesc)
                setDesc(getString(R.string.tooltip_auto_topup_descr))
                setWithIcon(false)
                setTextButton(getString(R.string.label_manage))
                btnAction.setOnClickListener { gotoAutoTopup(); dismiss() }
            }.show()
        }
    }

    private fun gotoAutoTopup() {
        activity?.let {
            startActivityForResult(TopAdsAutoTopUpActivity.createInstance(it), REQUEST_CODE_SET_AUTO_TOPUP)
        }
    }

    override fun onErrorGetAutoTopUpStatus(throwable: Throwable) {}

    private fun getTotalAd(totalAd: TotalAd): Int {
        return totalAd.totalShopAd + totalAd.totalKeyword + totalAd.totalProductAd + totalAd.totalProductGroupAd
    }

    fun updateLabelDateView(startDate: Date?, endDate: Date?) {
        if (startDate == null || endDate == null) return
        date_label_view.setContent(DateLabelUtils.getRangeDateFormatted(activity, startDate.time, endDate.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        topAdsDashboardPresenter.detachView()
    }

    protected fun showBottomSheetStatisticTypeOptions() {
        val checkedBottomSheetMenu = TopAdsMenuBottomSheets()
                .setMode(TopAdsOptionMenuAdapter.MODE_CHECKABLE)
                .setTitle(getString(R.string.drawer_title_statistic))

        checkedBottomSheetMenu.setMenuItemSelected(TopAdsMenuBottomSheets.OnMenuItemSelected { itemId ->
            checkedBottomSheetMenu.dismiss()
            if (!isAdded) {
                return@OnMenuItemSelected
            }
            selectedStatisticType = itemId
            topAdsTabAdapter?.setStatisticsType(selectedStatisticType)
            conversionFragment?.updateTitle(selectedStatisticType)
            loadStatisticsData()
        })

        checkedBottomSheetMenu.addItem(TopAdsStatisticsType.ALL_ADS, getString(R.string.topads_dashboard_all_promo_menu),
                selectedStatisticType == TopAdsStatisticsType.ALL_ADS)
        checkedBottomSheetMenu.addItem(TopAdsStatisticsType.PRODUCT_ADS, getString(R.string.top_ads_title_product),
                selectedStatisticType == TopAdsStatisticsType.PRODUCT_ADS)
        checkedBottomSheetMenu.addItem(TopAdsStatisticsType.SHOP_ADS, getString(R.string.title_top_ads_store),
                selectedStatisticType == TopAdsStatisticsType.SHOP_ADS)

        checkedBottomSheetMenu.show(activity!!.supportFragmentManager, javaClass.simpleName)
    }

    fun startShowCase() {
        callback?.startShowCase()
    }

    private fun onCostSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CPC)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CPC)
            else -> {
            }
        }
    }

    private fun onAverageConversionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_AVERAGE_CONVERSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_AVERAGE_CONVERSION)
            else -> {
            }
        }
    }

    private fun onConversionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CONVERSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CONVERSION)
            else -> {
            }
        }
    }

    private fun onCtrSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CTR)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CTR)
            else -> {
            }
        }
    }

    private fun onClickSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CLICK)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_CLICK)
            else -> {
            }
        }
    }

    private fun onImpressionSelected() {
        when (selectedStatisticType) {
            TopAdsStatisticsType.PRODUCT_ADS -> tracker?.eventTopAdsProductStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_IMPRESSION)
            TopAdsStatisticsType.SHOP_ADS -> tracker?.eventTopAdsShopStatisticBar(TopAdsDashboardTrackerConstant.STATISTIC_OPTION_IMPRESSION)
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_top_ads_dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_more) {
            showMoreBottomSheetDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showMoreBottomSheetDialog() {
        activity?.let {
            val menus = Menus(it)
            menus.setItemMenuList(R.array.top_ads_dashboard_menu_more)
            menus.setActionText(getString(R.string.close))
            menus.setOnActionClickListener { menus.dismiss() }

            menus.setOnItemMenuClickListener { itemMenus, pos ->
                when (pos) {
                    0 -> {
                        scroll_view.scrollTo(0, 0)
                        startShowCase()
                        menus.dismiss()
                    }
                    1 -> {
                        menus.dismiss()
                        startActivity(Intent(it, SellerCenterActivity::class.java))
                    }
                    else -> {
                    }
                }
            }

            menus.show()
        }
    }

    interface Callback {
        fun startShowCase()
    }

    companion object {
        private val MILLISECONDS_PER_INCH = 200f
        private val REQUEST_CODE_ADD_CREDIT = 1
        val REQUEST_CODE_AD_STATUS = 2
        val REQUEST_CODE_AD_OPTION = 3
        val REQUEST_CODE_ADD_PRODUCT = 4
        val REQUEST_CODE_ADD_KEYWORD = 5
        private const val REQUEST_CODE_SET_AUTO_TOPUP = 6

        fun createInstance(): TopAdsDashboardFragment {
            return TopAdsDashboardFragment()
        }
    }
}
