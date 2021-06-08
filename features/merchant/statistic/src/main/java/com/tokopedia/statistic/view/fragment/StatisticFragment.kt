package com.tokopedia.statistic.view.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.WidgetFilterBottomSheet
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.analytics.TrackingHelper
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.BAR_CHART_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.CARD_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.CAROUSEL_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.LINE_GRAPH_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.PIE_CHART_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.POST_LIST_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.PROGRESS_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.TABLE_WIDGET_TRACE
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.common.Const
import com.tokopedia.statistic.common.StatisticPageHelper
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.statistic.common.utils.logger.StatisticLogger
import com.tokopedia.statistic.di.StatisticComponent
import com.tokopedia.statistic.view.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.statistic.view.bottomsheet.DateFilterBottomSheet
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticLayoutManager
import com.tokopedia.statistic.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener {

    companion object {
        private const val DEFAULT_START_DATE_NON_REGULAR_MERCHANT = 1L
        private const val DEFAULT_START_DATE_REGULAR_MERCHANT = 7L
        private const val DEFAULT_END_DATE_NON_REGULAR_MERCHANT = 0L
        private const val DEFAULT_END_DATE_REGULAR_MERCHANT = 1L
        private const val TOAST_DURATION = 5000L
        private const val SCREEN_NAME = "statistic_page_fragment"
        private const val TAG_TOOLTIP = "statistic_tooltip"
        private const val TICKER_NAME = "statistic_page_ticker"
        private const val STATISTIC_PAGE = "statistic_page_source"

        fun newInstance(page: StatisticPageUiModel): StatisticFragment {
            return StatisticFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(STATISTIC_PAGE, page)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val mViewModel: StatisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticViewModel::class.java)
    }
    private val mLayoutManager by lazy { StatisticLayoutManager(context, spanCount = 2) }
    private val recyclerView by lazy { super.getRecyclerView(view) }
    private var dateFilterBottomSheet: DateFilterBottomSheet? = null
    private val defaultStartDate by lazy {
        val defaultStartDate = if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                statisticPage?.pageTitle != getString(R.string.stc_shop)) {
            Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DATE_REGULAR_MERCHANT))
        } else Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DATE_NON_REGULAR_MERCHANT))
        return@lazy statisticPage?.dateFilters?.firstOrNull { it.isSelected }?.startDate
                ?: defaultStartDate
    }
    private val defaultEndDate by lazy {
        val defaultEndDate = if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                statisticPage?.pageTitle != getString(R.string.stc_shop)) {
            Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_END_DATE_REGULAR_MERCHANT))
        } else Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_END_DATE_NON_REGULAR_MERCHANT))
        return@lazy statisticPage?.dateFilters?.firstOrNull { it.isSelected }?.endDate
                ?: defaultEndDate
    }
    private val tickerWidget: TickerWidgetUiModel by getTickerWidget()

    private var statisticPage: StatisticPageUiModel? = null
    private var isFirstLoad = true
    private var isErrorToastShown = false
    private var headerSubTitle: String = ""
    private var selectedWidget: String = "" //format should be : widgetType-widgetId, ex: section-109
    private val dateFilterImpressHolder = ImpressHolder()
    private val otherMenuImpressHolder = ImpressHolder()

    private var isPltMonitoringCompleted = false
    private var performanceMonitoringCardWidget: PerformanceMonitoring? = null
    private var performanceMonitoringLineGraphWidget: PerformanceMonitoring? = null
    private var performanceMonitoringProgressWidget: PerformanceMonitoring? = null
    private var performanceMonitoringPostListWidget: PerformanceMonitoring? = null
    private var performanceMonitoringCarouselWidget: PerformanceMonitoring? = null
    private var performanceMonitoringTableWidget: PerformanceMonitoring? = null
    private var performanceMonitoringPieChartWidget: PerformanceMonitoring? = null
    private var performanceMonitoringBarChartWidget: PerformanceMonitoring? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statisticPage = getPageFromArgs()
        statisticPage?.let { page ->
            lifecycleScope.launch(Dispatchers.Unconfined) {
                startLayoutNetworkPerformanceMonitoring()
                mViewModel.getWidgetLayout(page.pageSource)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_stc_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hideTooltipIfExist()
        setupView()
        setDefaultDynamicParameter()

        observeWidgetLayoutLiveData()
        observeUserRole()
        observeWidgetData(mViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(mViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(mViewModel.multiLineGraphWidgetData, WidgetType.MULTI_LINE_GRAPH)
        observeWidgetData(mViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(mViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(mViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeWidgetData(mViewModel.tableWidgetData, WidgetType.TABLE)
        observeWidgetData(mViewModel.pieChartWidgetData, WidgetType.PIE_CHART)
        observeWidgetData(mViewModel.barChartWidgetData, WidgetType.BAR_CHART)
        observeTickers()
    }

    override fun onResume() {
        super.onResume()
        setHeaderSubTitle(headerSubTitle)
        if (userVisibleHint)
            StatisticTracker.sendScreen(screenName)
    }

    override fun onPause() {
        super.onPause()
        hideTooltipIfExist()
        hideMonthPickerIfExist()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        initDateFilterBottomSheet()

        inflater.inflate(R.menu.menu_stc_action_calendar, menu)

        setMenuItemVisibility(menu)
        sendActionBarMenuImpressionEvent(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
            R.id.actionStcSelectDate -> selectDateRange()
            R.id.actionStcOtherMenu -> setupActionMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvStcWidgets

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl {
        return WidgetAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(StatisticComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {}

    override fun loadData(page: Int) {}

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        if (!isAdded || context == null) return
        val tooltipBottomSheet = (childFragmentManager.findFragmentByTag(TAG_TOOLTIP) as? TooltipBottomSheet)
                ?: TooltipBottomSheet.createInstance()
        tooltipBottomSheet.init(requireContext(), tooltip)
        tooltipBottomSheet.show(childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        // No-Op
        // We are not removing any widget in this page as we want the functionality to
        // show/hide widget only according to filter result
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>, error: String) {
        showErrorToaster()
    }

    override fun sendCardClickTracking(model: CardWidgetUiModel) {
        StatisticTracker.sendClickCardEvent(model)
    }

    override fun sendCardImpressionEvent(model: CardWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendCardImpressionEvent(model, position)
    }

    override fun sendLineGraphCtaClickEvent(dataKey: String, chartValue: String) {
        StatisticTracker.sendClickLineGraphEvent(dataKey, chartValue)
    }

    override fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendImpressionLineGraphEvent(model, position)
    }

    override fun sendLineChartEmptyStateCtaClickEvent(model: LineGraphWidgetUiModel) {
        StatisticTracker.sendEmptyStateCtaClickLineGraphEvent(model)
    }

    override fun sendCarouselImpressionEvent(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        StatisticTracker.sendImpressionCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselClickTracking(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        StatisticTracker.sendClickCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselCtaClickEvent(dataKey: String) {
        StatisticTracker.sendClickCarouselCtaEvent(dataKey)
    }

    override fun sendCarouselEmptyStateCtaClickEvent(element: CarouselWidgetUiModel) {
        // TODO: Add tracking
    }

    override fun sendPosListItemClickEvent(dataKey: String, title: String) {
        StatisticTracker.sendClickPostItemEvent(dataKey, title)
    }

    override fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {
        StatisticTracker.sendClickPostSeeMoreEvent(element.dataKey)
    }

    override fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {
        StatisticTracker.sendImpressionPostEvent(element.dataKey)
    }

    override fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Int) {
        StatisticTracker.sendImpressionProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Int) {
        StatisticTracker.sendClickProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendDescriptionCtaClickEvent(descriptionTitle: String) {
        StatisticTracker.sendClickDescriptionEvent(descriptionTitle)
    }

    override fun sendDescriptionImpressionEvent(descriptionTitle: String) {
        StatisticTracker.sendImpressionDescriptionEvent(descriptionTitle)
    }

    override fun sendTableImpressionEvent(model: TableWidgetUiModel, slideNumber: Int, maxSlidePosition: Int, isSlideEmpty: Boolean) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendTableImpressionEvent(model, position, slideNumber, isSlideEmpty)
        getCategoryPage()?.let { categoryPage ->
            StatisticTracker.sendTableSlideEvent(categoryPage, slideNumber + 1, maxSlidePosition)
        }
    }

    override fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendPieChartImpressionEvent(model, position)
    }

    override fun sendPieChartEmptyStateCtaClickEvent(model: PieChartWidgetUiModel) {
        // TODO: Add tracking
    }

    override fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendBarChartImpressionEvent(model, position)
    }

    override fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {
        StatisticTracker.sendSectionTooltipClickEvent(model.title)
    }

    override fun sendTableHyperlinkClickEvent(dataKey: String, url: String, isEmpty: Boolean) {}

    override fun sendTableFilterClick(element: TableWidgetUiModel) {
        element.tableFilters.find { it.isSelected }?.let { filterOption ->
            getCategoryPage()?.let { categoryPage ->
                StatisticTracker.sendTableFilterClickEvent(categoryPage, filterOption.name)
            }
        }
    }

    override fun sendTableFilterImpression(element: TableWidgetUiModel) {
        getCategoryPage()?.let { categoryPage ->
            StatisticTracker.sendTableFilterImpressionEvent(categoryPage)
        }
    }

    override fun showTableFilter(element: TableWidgetUiModel, adapterPosition: Int) {
        if (!isAdded || context == null) return

        val tableFilterBottomSheet = (childFragmentManager.findFragmentByTag(WidgetFilterBottomSheet.TABLE_FILTER_TAG) as? WidgetFilterBottomSheet)
                ?: WidgetFilterBottomSheet.newInstance()
        tableFilterBottomSheet.init(requireContext(), com.tokopedia.sellerhomecommon.R.string.shc_select_statistic_data, element.tableFilters) {
            recyclerView?.post {
                val copiedWidget = adapter.data
                        ?.find { it.dataKey == element.dataKey }
                        ?.apply { data = null } as? TableWidgetUiModel
                copiedWidget?.let {
                    notifyWidgetChanged(it)
                    fetchTableData(listOf(copiedWidget))
                }
            }
        }.show(childFragmentManager, WidgetFilterBottomSheet.TABLE_FILTER_TAG)
    }

    fun setSelectedWidget(widget: String) {
        this.selectedWidget = widget
    }

    private fun setupView() = view?.run {
        setDefaultRange()
        setupRecyclerView()

        swipeRefreshStc.setOnRefreshListener {
            reloadPage()
        }

        globalErrorStc.setActionClickListener {
            reloadPage()
        }
    }

    private fun getPageFromArgs(): StatisticPageUiModel? {
        return arguments?.getParcelable(STATISTIC_PAGE)
    }

    private fun setDefaultDynamicParameter() {
        statisticPage?.let { page ->
            page.dateFilters.firstOrNull { it.isSelected }.let { dateFilter ->
               if (dateFilter != null) {
                   mViewModel.setDateFilter(page.pageSource, defaultStartDate, defaultEndDate, dateFilter.getDateFilterType())
               } else if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                       statisticPage?.pageTitle == getString(R.string.stc_buyer)) {
                   mViewModel.setDateFilter(page.pageSource, defaultStartDate, defaultEndDate, DateFilterType.DATE_TYPE_DAY)
               } else {
                   mViewModel.setDateFilter(page.pageSource, defaultStartDate, defaultEndDate, DateFilterType.DATE_TYPE_TODAY)
               }
            }
        }
    }

    private fun setDefaultRange() = view?.run {
        statisticPage?.dateFilters?.firstOrNull { it.isSelected }.let {
            val headerSubtitle = if (it != null) {
                it.getHeaderSubTitle(requireContext())
            } else if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                    statisticPage?.pageTitle != getString(R.string.stc_shop)) {
                val headerSubTitle: String = getString(R.string.stc_last_n_days_cc, Const.DAYS_7)
                val startEndDateFmt = DateFilterFormatUtil.getDateRangeStr(defaultStartDate, defaultEndDate)
                "$headerSubTitle ($startEndDateFmt)"
            } else {
                val startDateMillis = defaultStartDate.time
                val dateStr = DateTimeUtil.format(startDateMillis, "dd MMMM")
                val hourStr = DateTimeUtil.format(System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)), "HH:00")
                getString(R.string.stc_today_fmt, dateStr, hourStr)
            }
            this@StatisticFragment.headerSubTitle = headerSubtitle
        }
    }

    private fun setHeaderSubTitle(subTitle: String) {
        this.headerSubTitle = subTitle
        (activity as? FragmentListener)?.setHeaderSubTitle(subTitle)
    }

    private fun setupRecyclerView() = view?.run {
        with(mLayoutManager) {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        val isCardWidget = adapter.data[position].widgetType == WidgetType.CARD
                        if (isCardWidget) 1 else spanCount
                    } catch (e: IndexOutOfBoundsException) {
                        spanCount
                    }
                }
            }

            setOnScrollVertically {
                requestVisibleWidgetsData()
            }
        }

        recyclerView?.run {
            layoutManager = mLayoutManager
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        (bottomSheet as? BottomSheetUnify)?.dismiss()
    }

    private fun hideMonthPickerIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(Const.BottomSheet.TAG_MONTH_PICKER)
        (bottomSheet as? BottomSheetUnify)?.dismiss()
    }

    private fun fetchCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        performanceMonitoringCardWidget = PerformanceMonitoring.start(CARD_WIDGET_TRACE)
        mViewModel.getCardWidgetData(dataKeys)
    }

    private fun fetchLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        performanceMonitoringLineGraphWidget = PerformanceMonitoring.start(LINE_GRAPH_WIDGET_TRACE)
        mViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun fetchMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        mViewModel.getMultiLineGraphWidgetData(dataKeys)
    }

    private fun fetchProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        performanceMonitoringProgressWidget = PerformanceMonitoring.start(PROGRESS_WIDGET_TRACE)
        mViewModel.getProgressWidgetData(dataKeys)
    }

    private fun fetchPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<Pair<String, String>> = widgets.filterIsInstance<PostListWidgetUiModel>().map {
            val postFilter = it.postFilter.find { filter -> filter.isSelected }?.value.orEmpty()
            return@map Pair(it.dataKey, postFilter)
        }
        performanceMonitoringPostListWidget = PerformanceMonitoring.start(POST_LIST_WIDGET_TRACE)
        mViewModel.getPostWidgetData(dataKeys)
    }

    private fun fetchCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        performanceMonitoringCarouselWidget = PerformanceMonitoring.start(CAROUSEL_WIDGET_TRACE)
        mViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun fetchTableData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<Pair<String, String>> = widgets.filterIsInstance<TableWidgetUiModel>().map {
            val tableFilter = it.tableFilters.find { filter -> filter.isSelected }?.value.orEmpty()
            return@map Pair(it.dataKey, tableFilter)
        }
        performanceMonitoringTableWidget = PerformanceMonitoring.start(TABLE_WIDGET_TRACE)
        mViewModel.getTableWidgetData(dataKeys)
    }

    private fun fetchPieChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        performanceMonitoringPieChartWidget = PerformanceMonitoring.start(PIE_CHART_WIDGET_TRACE)
        mViewModel.getPieChartWidgetData(dataKeys)
    }

    private fun fetchBarChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        performanceMonitoringBarChartWidget = PerformanceMonitoring.start(BAR_CHART_WIDGET_TRACE)
        mViewModel.getBarChartWidgetData(dataKeys)
    }

    private fun selectDateRange() {
        if (!isAdded || context == null) return
        StatisticTracker.sendDateFilterEvent(userSession)
        dateFilterBottomSheet?.setFragmentManager(childFragmentManager)?.setOnApplyChanges {
            setHeaderSubTitle(it.getHeaderSubTitle(requireContext()))
            applyDateRange(it)
        }?.show()

        val tabName = statisticPage?.pageTitle.orEmpty()
        StatisticTracker.sendCalendarClickEvent(userSession.userId, tabName, headerSubTitle)
    }

    private fun applyDateRange(item: DateFilterItem) {
        val startDate = item.startDate ?: return
        val endDate = item.endDate ?: return
        statisticPage?.let {
            mViewModel.setDateFilter(it.pageSource, startDate, endDate, item.getDateFilterType())
        }

        StatisticTracker.sendSetDateFilterEvent(item.label)
        adapter.data.forEach {
            if (it !is TickerWidgetUiModel) {
                it.isLoaded = false
                it.data = null
            }
        }
        adapter.notifyDataSetChanged()
        requestVisibleWidgetsData()
    }

    private fun requestVisibleWidgetsData() {
        val firstVisible: Int = mLayoutManager.findFirstVisibleItemPosition()
        val lastVisible: Int = mLayoutManager.findLastVisibleItemPosition()
        lifecycleScope.launch(Dispatchers.Unconfined) {
            val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
            adapter.data.forEachIndexed { index, widget ->
                if (index in firstVisible..lastVisible && !widget.isLoaded) {
                    visibleWidgets.add(widget)
                }
            }

            if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
        }
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView?.visible()
        view?.globalErrorStc?.gone()

        val mWidgetList = mutableListOf<BaseWidgetUiModel<*>>()
        mWidgetList.add(tickerWidget)
        mWidgetList.addAll(widgets)
        mWidgetList.add(WhiteSpaceUiModel())
        adapter.data.clear()
        super.renderList(mWidgetList)

        scrollToWidgetBySelectedDataKey()

        if (isFirstLoad) {
            recyclerView?.post {
                requestVisibleWidgetsData()
            }
            isFirstLoad = false
        } else {
            requestVisibleWidgetsData()
        }
    }

    private fun scrollToWidgetBySelectedDataKey() {
        if (selectedWidget.isBlank()) return

        try {
            val separator = "-"
            val widgetType = selectedWidget.substring(0, selectedWidget.indexOf(separator))
            val widgetId = selectedWidget.replace(widgetType + separator, "")
            val index = adapter.data.indexOfFirst {
                it.widgetType == widgetType && it.id == widgetId
            }
            val invalidIndex = -1
            if (index != invalidIndex) {
                recyclerView?.post {
                    val offset = 0
                    mLayoutManager.scrollToPositionWithOffset(index, offset)
                }
            }
        } catch (e: StringIndexOutOfBoundsException) {
            Timber.e(e)
        }

        selectedWidget = ""
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets: Map<String, List<BaseWidgetUiModel<*>>> = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { fetchCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { fetchLineGraphData(this) }
        groupedWidgets[WidgetType.MULTI_LINE_GRAPH]?.run { fetchMultiLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { fetchProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { fetchCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { fetchPostData(this) }
        groupedWidgets[WidgetType.TABLE]?.run { fetchTableData(this) }
        groupedWidgets[WidgetType.PIE_CHART]?.run { fetchPieChartData(this) }
        groupedWidgets[WidgetType.BAR_CHART]?.run { fetchBarChartData(this) }
    }

    private fun setOnErrorGetLayout(throwable: Throwable) = view?.run {
        if (adapter.data.isEmpty()) {
            globalErrorStc.visible()
            recyclerView?.gone()
        } else {
            showErrorToaster()
            globalErrorStc.gone()
        }
        StatisticLogger.logToCrashlytics(throwable, StatisticLogger.ERROR_LAYOUT)
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.build(this, context.getString(R.string.stc_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.stc_reload)) {
            reloadPageOrLoadDataOfErrorWidget()
        }.show()

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    /**
     * if any widget that failed when load their data, the action should be load the widget data.
     * else, reload the page like pull refresh
     * */
    private fun reloadPageOrLoadDataOfErrorWidget() {
        val isAnyErrorWidget = adapter.data.any { !it.data?.error.isNullOrBlank() }
        if (!isAnyErrorWidget) {
            reloadPage()
            return
        }

        isErrorToastShown = false

        val errorWidgets: List<BaseWidgetUiModel<*>> = adapter.data.filterIndexed { index, widget ->
            val isWidgetError = !widget.data?.error.isNullOrBlank()
            if (isWidgetError) {
                //set data to null then notify adapter to show the widget shimmer
                widget.data = null
                adapter.notifyItemChanged(index)
            }
            return@filterIndexed isWidgetError
        }

        if (errorWidgets.isNotEmpty()) {
            getWidgetsData(errorWidgets)
        }
    }

    private fun reloadPage() = view?.run {
        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshStc.isRefreshing = isAdapterNotEmpty

        globalErrorStc.gone()
        statisticPage?.let {
            mViewModel.getWidgetLayout(it.pageSource)
        }
    }

    private fun setProgressBarVisibility(isShown: Boolean) = view?.run {
        if (isShown) {
            globalErrorStc.gone()
            progressBarStc.visible()
        } else {
            progressBarStc.gone()
        }
    }

    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(widgetType: String) {
        val message = this.message.orEmpty()
        adapter.data.filter { it.widgetType == widgetType }
                .forEach { widget ->
                    if (widget is W) {
                        val widgetData = D::class.java.newInstance().apply {
                            error = message
                        }
                        widget.data = widgetData
                        notifyWidgetChanged(widget)
                    }
                }

        showErrorToaster()
        recyclerView?.post {
            requestVisibleWidgetsData()
        }
        StatisticLogger.logToCrashlytics(this, "${StatisticLogger.ERROR_WIDGET} $widgetType")
    }

    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(widgetType: String) {
        forEach { widgetData ->
            adapter.data.find {
                val isSameDataKey = it.dataKey == widgetData.dataKey
                val isSameWidgetType = it.widgetType == widgetType
                return@find isSameDataKey && isSameWidgetType
            }?.let { widget ->
                if (widget is W) {
                    widget.data = widgetData
                    notifyWidgetChanged(widget)
                }
            }
        }
        recyclerView?.post {
            requestVisibleWidgetsData()
        }
    }

    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        recyclerView?.post {
            val widgetPosition = adapter.data.indexOf(widget)
            if (widgetPosition != RecyclerView.NO_POSITION) {
                adapter.notifyItemChanged(widgetPosition)
                view?.swipeRefreshStc?.isRefreshing = false
            }
        }
    }

    private fun observeWidgetLayoutLiveData() {
        mViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->

            when (result) {
                is Success -> setOnSuccessGetLayout(result.data)
                is Fail -> setOnErrorGetLayout(result.throwable)
            }

            setProgressBarVisibility(false)
        })

        setProgressBarVisibility(true)
    }

    private fun observeUserRole() {
        mViewModel.userRole.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> checkUserRole(it.data)
                is Fail -> StatisticLogger.logToCrashlytics(it.throwable, StatisticLogger.ERROR_SELLER_ROLE)
            }
        })
        mViewModel.getUserRole()
    }

    private fun observeTickers() {
        statisticPage?.let {
            mViewModel.getTickers(it.tickerPageName)
        }
        mViewModel.tickers.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> showTickers(it.data)
                is Fail -> StatisticLogger.logToCrashlytics(it.throwable, StatisticLogger.ERROR_TICKER)
            }
        })
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            startLayoutRenderingPerformanceMonitoring()

            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
            }

            stopPLTPerformanceMonitoring()
            stopWidgetPerformanceMonitoring(type)
        })
    }

    private fun checkUserRole(roles: List<String>) {
        val manageShopStatsRole = "MANAGE_SHOPSTATS"
        if (!roles.contains(manageShopStatsRole)) {
            showToaster()
            activity?.finish()
        }
    }

    private fun showToaster() = view?.run {
        val toaster = Toast.makeText(context, context.getString(R.string.stc_you_havent_access_this_page), Toast.LENGTH_LONG)
        val countDownInterval = 1000L
        val toastCountDown = object : CountDownTimer(TOAST_DURATION, countDownInterval) {
            override fun onTick(p0: Long) {
                toaster.show()
            }

            override fun onFinish() {
                toaster.cancel()
            }
        }
        toaster.show()
        toastCountDown.start()
    }

    private fun showTickers(tickers: List<TickerItemUiModel>) {
        tickerWidget.data?.tickers = tickers
        notifyWidgetChanged(tickerWidget)
    }

    private fun setupActionMenu() {
        statisticPage?.actionMenu?.let { menus ->
            //we can't show bottom sheet if FragmentManager's state has already been saved
            if (childFragmentManager.isStateSaved) return

            val pageName = statisticPage?.pageTitle.orEmpty()
            val actionMenuBottomSheet = ActionMenuBottomSheet.createInstance(pageName, userSession.userId, menus)

            //to prevent IllegalStateException: Fragment already added
            if (actionMenuBottomSheet.isAdded) return

            actionMenuBottomSheet.show(childFragmentManager)

            StatisticTracker.sendThreeDotsClickEvent(userSession.userId)
        }
    }

    private fun getTickerWidget(): Lazy<TickerWidgetUiModel> = lazy {
        val tickerData = TickerDataUiModel(tickers = emptyList(), dataKey = TICKER_NAME)
        return@lazy TickerWidgetUiModel(data = tickerData, title = TICKER_NAME, dataKey = TICKER_NAME)
    }

    private fun initDateFilterBottomSheet() {
        if (dateFilterBottomSheet == null) {
            val dateFilters: List<DateFilterItem> = statisticPage?.dateFilters.orEmpty()
            dateFilterBottomSheet = DateFilterBottomSheet.newInstance(dateFilters)
        }
    }

    private fun setMenuItemVisibility(menu: Menu) {
        val shouldShowActionMenu = !statisticPage?.actionMenu.isNullOrEmpty()
        menu.findItem(R.id.actionStcOtherMenu)?.isVisible = shouldShowActionMenu

        val minFilterSize = 1
        val shouldShowFilterMenu = statisticPage?.dateFilters?.size.orZero() > minFilterSize
        menu.findItem(R.id.actionStcSelectDate)?.isVisible = shouldShowFilterMenu
    }

    private fun sendActionBarMenuImpressionEvent(menu: Menu) {
        //send impression for calendar filter action menu
        menu.findItem(R.id.actionStcSelectDate)?.let {
            view?.addOnImpressionListener(dateFilterImpressHolder) {
                val tabName = statisticPage?.pageTitle.orEmpty()
                val chosenPeriod = headerSubTitle
                StatisticTracker.sendCalendarImpressionEvent(userSession.userId, tabName, chosenPeriod)
            }
        }

        //send impression for 3 dots action menu
        menu.findItem(R.id.actionStcOtherMenu)?.let {
            view?.addOnImpressionListener(otherMenuImpressHolder) {
                StatisticTracker.sendThreeDotsImpressionEvent(userSession.userId)
            }
        }
    }

    private fun startLayoutNetworkPerformanceMonitoring() {
        (activity as? StatisticPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
    }

    private fun startLayoutRenderingPerformanceMonitoring() {
        (activity as? StatisticPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
    }

    private fun stopPLTPerformanceMonitoring() {
        if (!isPltMonitoringCompleted) {
            isPltMonitoringCompleted = true
            recyclerView?.addOneTimeGlobalLayoutListener {
                (activity as? StatisticPerformanceMonitoringListener)?.stopPerformanceMonitoring()
            }
        }
    }

    private fun stopWidgetPerformanceMonitoring(type: String) {
        when (type) {
            WidgetType.CARD -> performanceMonitoringCardWidget?.stopTrace()
            WidgetType.LINE_GRAPH -> performanceMonitoringLineGraphWidget?.stopTrace()
            WidgetType.PROGRESS -> performanceMonitoringProgressWidget?.stopTrace()
            WidgetType.POST_LIST -> performanceMonitoringPostListWidget?.stopTrace()
            WidgetType.CAROUSEL -> performanceMonitoringCarouselWidget?.stopTrace()
            WidgetType.TABLE -> performanceMonitoringTableWidget?.stopTrace()
            WidgetType.PIE_CHART -> performanceMonitoringPieChartWidget?.stopTrace()
            WidgetType.BAR_CHART -> performanceMonitoringBarChartWidget?.stopTrace()
        }
    }

    private fun getCategoryPage(): String? {
        return context?.let {
            TrackingHelper.getCategoryPage(it, statisticPage?.pageTitle.orEmpty())
        }
    }
}