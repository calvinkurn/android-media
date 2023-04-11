package com.tokopedia.statistic.view.fragment

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.gms.common.util.DeviceProperties
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.common.const.WidgetGridSize
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.AnnouncementWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BarChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MultiLineGraphWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PieChartWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.SectionWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TickerWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetFilterUiModel
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.WidgetFilterBottomSheet
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.analytics.TrackingHelper
import com.tokopedia.statistic.analytics.performance.StatisticPagePerformanceTraceNameConst.ANNOUNCEMENT_WIDGET_TRACE
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
import com.tokopedia.statistic.databinding.FragmentStcStatisticBinding
import com.tokopedia.statistic.di.StatisticComponent
import com.tokopedia.statistic.view.bottomsheet.ActionMenuBottomSheet
import com.tokopedia.statistic.view.bottomsheet.DateFilterBottomSheet
import com.tokopedia.statistic.view.model.StatisticPageUiModel
import com.tokopedia.statistic.view.viewhelper.FragmentListener
import com.tokopedia.statistic.view.viewhelper.StatisticItemDecoration
import com.tokopedia.statistic.view.viewhelper.StatisticLayoutManager
import com.tokopedia.statistic.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(),
    WidgetListener {

    companion object {
        private const val DEFAULT_START_DATE_NON_REGULAR_MERCHANT = 1L
        private const val DEFAULT_START_DATE_REGULAR_MERCHANT = 7L
        private const val DEFAULT_END_DATE_NON_REGULAR_MERCHANT = 0L
        private const val DEFAULT_END_DATE_REGULAR_MERCHANT = 1L
        private const val TOAST_DURATION = 5000L
        private const val SCREEN_NAME = "statistic_page_fragment"
        private const val TAG_TOOLTIP = "statistic_tooltip"
        private const val TICKER_NAME = "statistic_page_ticker"
        private const val KEY_STATISTIC_PAGE = "statistic_page_source"
        private const val KEY_SHOULD_LOAD_DATA_ON_CREATE = "key_should_load_data_on_create"

        fun newInstance(
            page: StatisticPageUiModel,
            shouldLoadDataOnCreate: Boolean
        ): StatisticFragment {
            return StatisticFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_STATISTIC_PAGE, page)
                    putBoolean(KEY_SHOULD_LOAD_DATA_ON_CREATE, shouldLoadDataOnCreate)
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
    private var mLayoutManager: StatisticLayoutManager? = null
    private val recyclerView by lazy { super.getRecyclerView(view) }
    private val defaultStartDate by lazy {
        val defaultStartDate = if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
            statisticPage?.pageTitle != getString(R.string.stc_shop)
        ) {
            Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DATE_REGULAR_MERCHANT))
        } else Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DATE_NON_REGULAR_MERCHANT))
        return@lazy statisticPage?.dateFilters?.firstOrNull { it.isSelected }?.startDate
            ?: defaultStartDate
    }
    private val defaultEndDate by lazy {
        val defaultEndDate = if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
            statisticPage?.pageTitle != getString(R.string.stc_shop)
        ) {
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

    //format should be : widgetType-widgetId, ex: section-109
    private var selectedWidget: String = ""
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
    private var performanceMonitoringAnnouncementWidget: PerformanceMonitoring? = null

    private var binding by autoClearedNullable<FragmentStcStatisticBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statisticPage = getPageFromArgs()
        loadInitialLayoutData(savedInstanceState) {
            statisticPage?.let { page ->
                startLayoutNetworkPerformanceMonitoring()
                mViewModel.getWidgetLayout(page.pageSource)
                mViewModel.getTickers(page.tickerPageName)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStcStatisticBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hideTooltipIfExist()
        setupView()
        setDefaultDynamicParameter()

        observeWidgetLayoutLiveData()
        observeWidgetData(mViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(mViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(mViewModel.multiLineGraphWidgetData, WidgetType.MULTI_LINE_GRAPH)
        observeWidgetData(mViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(mViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(mViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeWidgetData(mViewModel.tableWidgetData, WidgetType.TABLE)
        observeWidgetData(mViewModel.pieChartWidgetData, WidgetType.PIE_CHART)
        observeWidgetData(mViewModel.barChartWidgetData, WidgetType.BAR_CHART)
        observeWidgetData(mViewModel.announcementWidgetData, WidgetType.ANNOUNCEMENT)
        observeTickers()
    }

    override fun onResume() {
        super.onResume()
        setHeaderSubTitle(headerSubTitle)
        if (!isHidden) {
            StatisticTracker.sendScreen(screenName)
        }
    }

    override fun onPause() {
        super.onPause()
        hideTooltipIfExist()
        hideMonthPickerIfExist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLayoutManager = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_stc_action_calendar, menu)

        for (i in Int.ZERO until menu.size()) {
            menu.getItem(i)?.let { menuItem ->
                menuItem.actionView?.setOnClickListener {
                    onOptionsItemSelected(menuItem)
                }
            }
        }

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
        val tooltipBottomSheet =
            (childFragmentManager.findFragmentByTag(TAG_TOOLTIP) as? TooltipBottomSheet)
                ?: TooltipBottomSheet.createInstance()
        tooltipBottomSheet.init(requireContext(), tooltip)
        tooltipBottomSheet.setOnShowListener {
            statisticPage?.let {
                StatisticTracker.sendTooltipImpressionEvent(it)
            }
        }
        tooltipBottomSheet.show(childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView?.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
            checkForSectionToBeRemoved(position)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>, error: String) {
        showErrorToaster()
    }

    override fun getIsShouldRemoveWidget(): Boolean = false

    override fun onRemoveWidget(position: Int) {
        recyclerView?.post {
            checkForSectionToBeRemoved(position)
        }
    }

    override fun onReloadWidget(widget: BaseWidgetUiModel<*>) {
        val widgets = mutableListOf<BaseWidgetUiModel<*>>()
        adapter.data.forEach {
            val isTheSameWidget = it.widgetType == widget.widgetType
            if (isTheSameWidget) {
                it.showLoadingState = true
                notifyWidgetChanged(it)
                widgets.add(it)
            }
        }
        getWidgetsData(widgets)
    }

    override fun sendCardClickTracking(model: CardWidgetUiModel) {
        StatisticTracker.sendClickCardEvent(model)
    }

    override fun sendCardImpressionEvent(model: CardWidgetUiModel) {
        StatisticTracker.sendCardImpressionEvent(
            statisticPage?.pageSource.orEmpty(), model
        )
    }

    override fun sendLineGraphCtaClickEvent(model: LineGraphWidgetUiModel) {
        StatisticTracker.sendClickLineGraphEvent(model.dataKey, model.data?.header.orEmpty())
    }

    override fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {
        StatisticTracker.sendImpressionLineGraphEvent(
            statisticPage?.pageSource.orEmpty(),
            model
        )
    }

    override fun sendLineChartEmptyStateCtaClickEvent(model: LineGraphWidgetUiModel) {
        StatisticTracker.sendEmptyStateCtaClickLineGraphEvent(model)
    }

    override fun sendPosListItemClickEvent(element: PostListWidgetUiModel, post: PostItemUiModel) {
        StatisticTracker.sendClickPostItemEvent(element.dataKey, post.title)
    }

    override fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {
        StatisticTracker.sendClickPostSeeMoreEvent(element.dataKey)
    }

    override fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {
        StatisticTracker.sendImpressionPostEvent(element.dataKey)
    }

    override fun sendProgressImpressionEvent(
        dataKey: String,
        stateColor: String,
        valueScore: Long
    ) {
        StatisticTracker.sendImpressionProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Long) {
        StatisticTracker.sendClickProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendDescriptionCtaClickEvent(model: DescriptionWidgetUiModel) {
        StatisticTracker.sendClickDescriptionEvent(model.title)
    }

    override fun sendDescriptionImpressionEvent(model: DescriptionWidgetUiModel) {
        StatisticTracker.sendImpressionDescriptionEvent(model.title)
    }

    override fun sendTableImpressionEvent(
        model: TableWidgetUiModel,
        position: Int,
        slidePosition: Int,
        maxSlidePosition: Int,
        isSlideEmpty: Boolean
    ) {
        StatisticTracker.sendTableImpressionEvent(
            statisticPage?.pageSource.orEmpty(), model.dataKey, isSlideEmpty
        )

        StatisticTracker.sendTableOnSwipeEvent(
            statisticPage?.pageSource.orEmpty(),
            slidePosition, maxSlidePosition
        )
    }

    override fun sendTableOnSwipeEvent(
        element: TableWidgetUiModel,
        slidePosition: Int,
        maxSlidePosition: Int,
        isSlideEmpty: Boolean
    ) {
        StatisticTracker.sendTableOnSwipeEvent(
            statisticPage?.pageSource.orEmpty(),
            slidePosition,
            maxSlidePosition
        )
    }

    override fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        StatisticTracker.sendPieChartImpressionEvent(
            statisticPage?.pageSource.orEmpty(), model
        )
    }

    override fun sendPieChartEmptyStateCtaClickEvent(element: PieChartWidgetUiModel) {
        StatisticTracker.sendPieChartEmptyStateCtaClickEvent(element)
    }

    override fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        StatisticTracker.sendBarChartImpressionEvent(
            statisticPage?.pageSource.orEmpty(),
            model
        )
    }

    override fun sendBarChartEmptyStateCtaClick(model: BarChartWidgetUiModel) {
        StatisticTracker.sendBarChartEmptyStateCtaClickEvent(model)
    }

    override fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {
        StatisticTracker.sendSectionTooltipClickEvent(
            statisticPage?.pageSource.orEmpty(), model.title
        )
    }

    override fun sendMultiLineGraphImpressionEvent(element: MultiLineGraphWidgetUiModel) {
        statisticPage?.let {
            StatisticTracker.sendMultiLineGraphImpressionEvent(it, element)
        }
    }

    override fun sendTickerImpression(tickers: List<TickerItemUiModel>) {
        statisticPage?.let {
            StatisticTracker.sendTickerImpressionEvent(it)
        }
    }

    override fun sendTickerCtaClickEvent(ticker: TickerItemUiModel) {
        statisticPage?.let {
            StatisticTracker.sendTickerCtaClickEvent(it)
        }
    }

    override fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {
        statisticPage?.let {
            StatisticTracker.sendAnnouncementImpressionEvent(it)
        }
    }

    override fun setOnAnnouncementWidgetCtaClicked(element: AnnouncementWidgetUiModel) {
        statisticPage?.let {
            val isRouting = RouteManager.route(context, element.data?.appLink.orEmpty())
            if (isRouting) {
                StatisticTracker.sendAnnouncementCtaClickEvent(it)
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

        val tableFilterBottomSheet = (
                childFragmentManager.findFragmentByTag(
                    WidgetFilterBottomSheet.TABLE_FILTER_TAG
                ) as? WidgetFilterBottomSheet) ?: WidgetFilterBottomSheet.newInstance()
        tableFilterBottomSheet.init(
            requireContext(),
            com.tokopedia.sellerhomecommon.R.string.shc_select_statistic_data,
            element.tableFilters
        ) { filter ->
            recyclerView?.post {
                val copiedWidget = element.copy(data = null)
                copiedWidget.let { widget ->
                    notifyWidgetChanged(widget)
                    fetchTableData(listOf(copiedWidget))
                }
            }
            sendSelectedFilterClickEvent(filter, element)
        }.show(childFragmentManager, WidgetFilterBottomSheet.TABLE_FILTER_TAG)

        statisticPage?.let {
            StatisticTracker.sendShowTableTableFilterClickEvent(it, element)
        }
    }

    fun setSelectedWidget(widget: String) {
        this.selectedWidget = widget
    }

    private fun sendSelectedFilterClickEvent(
        filter: WidgetFilterUiModel,
        model: TableWidgetUiModel
    ) {
        val isEmpty = model.data?.dataSet?.all { it.rows.isEmpty() }.orTrue()
        StatisticTracker.sendTableFilterClickEvent(
            statisticPage?.pageSource.orEmpty(),
            model.dataKey,
            filter.name,
            isEmpty
        )
    }

    private fun loadInitialLayoutData(savedInstanceState: Bundle?, action: () -> Unit) {
        val shouldLoadDataOnCreate = arguments?.getBoolean(KEY_SHOULD_LOAD_DATA_ON_CREATE).orFalse()
        if (shouldLoadDataOnCreate) {
            if (savedInstanceState == null) {
                action()
            }
        } else {
            lifecycleScope.launchWhenResumed {
                if (isVisible && isFirstLoad) {
                    action()
                }
            }
        }
    }

    private fun setupView() = binding?.run {
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
        return arguments?.getParcelable(KEY_STATISTIC_PAGE)
    }

    private fun setDefaultDynamicParameter() {
        statisticPage?.let { page ->
            page.dateFilters.firstOrNull { it.isSelected }.let { dateFilter ->
                if (dateFilter != null) {
                    mViewModel.setDateFilter(
                        page.pageSource,
                        defaultStartDate,
                        defaultEndDate,
                        dateFilter.getDateFilterType()
                    )
                } else if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                    statisticPage?.pageTitle == getString(R.string.stc_buyer)
                ) {
                    mViewModel.setDateFilter(
                        page.pageSource,
                        defaultStartDate,
                        defaultEndDate,
                        DateFilterType.DATE_TYPE_DAY
                    )
                } else {
                    mViewModel.setDateFilter(
                        page.pageSource,
                        defaultStartDate,
                        defaultEndDate,
                        DateFilterType.DATE_TYPE_TODAY
                    )
                }
            }
        }
    }

    private fun setDefaultRange() {
        statisticPage?.dateFilters?.firstOrNull { it.isSelected }.let {
            val headerSubtitle = it?.getHeaderSubTitle(requireContext())
                ?: if (StatisticPageHelper.getRegularMerchantStatus(userSession) ||
                    statisticPage?.pageTitle != getString(R.string.stc_shop)
                ) {
                    val headerSubTitle: String =
                        getString(R.string.stc_last_n_days_cc, Const.DAYS_7)
                    val startEndDateFmt =
                        DateFilterFormatUtil.getDateRangeStr(defaultStartDate, defaultEndDate)
                    "$headerSubTitle ($startEndDateFmt)"
                } else {
                    val startDateMillis = defaultStartDate.time
                    val dateStr = DateTimeUtil.format(startDateMillis, "dd MMMM")
                    val hourStr = DateTimeUtil.format(
                        System.currentTimeMillis().minus(TimeUnit.HOURS.toMillis(1)), "HH:00"
                    )
                    getString(R.string.stc_today_fmt, dateStr, hourStr)
                }
            this@StatisticFragment.headerSubTitle = headerSubtitle
        }
    }

    private fun setHeaderSubTitle(subTitle: String) {
        this.headerSubTitle = subTitle
        (activity as? FragmentListener)?.setHeaderSubTitle(subTitle)
    }

    private fun setupRecyclerView() = binding?.run {
        val statisticSpanCount = getWidgetSpanCountByDeviceType()
        val isTablet = (statisticSpanCount == WidgetGridSize.GRID_SIZE_4)
        mLayoutManager = StatisticLayoutManager(context, statisticSpanCount)
        mLayoutManager?.run {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return getWidgetSpanSize(position, isTablet, spanCount)
                }
            }

            setOnScrollVertically {
                requestVisibleWidgetsData()
            }
        }

        recyclerView?.run {
            addItemDecoration(StatisticItemDecoration())
            layoutManager = mLayoutManager
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    private fun getWidgetSpanSize(
        position: Int,
        isTablet: Boolean,
        defaultSpanCount: Int
    ): Int {
        return try {
            val widget = adapter.data[position]
            return if (isTablet) {
                setupLayoutForTable(widget, defaultSpanCount)
            } else {
                val isCardWidget = widget.widgetType == WidgetType.CARD
                if (isCardWidget) WidgetGridSize.GRID_SIZE_1 else defaultSpanCount
            }
        } catch (e: IndexOutOfBoundsException) {
            Timber.e(e)
            defaultSpanCount
        }
    }

    private fun setupLayoutForTable(widget: BaseWidgetUiModel<*>, defaultSpanCount: Int): Int {
        val orientation = activity?.resources?.configuration?.orientation
            ?: Configuration.ORIENTATION_PORTRAIT
        val isPortrait = orientation == Configuration.ORIENTATION_PORTRAIT
        return if (isPortrait) {
            when (widget) {
                is CardWidgetUiModel -> WidgetGridSize.GRID_SIZE_2
                is PieChartWidgetUiModel -> widget.gridSize
                else -> defaultSpanCount
            }
        } else {
            when (widget) {
                is CardWidgetUiModel -> WidgetGridSize.GRID_SIZE_1
                is SectionWidgetUiModel -> defaultSpanCount
                else -> widget.gridSize
            }
        }
    }

    private fun getWidgetSpanCountByDeviceType(): Int {
        return if (DeviceProperties.isTablet(requireActivity().resources)) {
            WidgetGridSize.GRID_SIZE_4
        } else {
            WidgetGridSize.GRID_SIZE_2
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
        val dataKeys: List<TableAndPostDataKey> =
            widgets.filterIsInstance<PostListWidgetUiModel>().map {
                val postFilter = it.postFilter.find { filter -> filter.isSelected }?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, postFilter, it.maxData, it.maxDisplay)
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
        val dataKeys: List<TableAndPostDataKey> = widgets.filterIsInstance<TableWidgetUiModel>()
            .map {
                val tableFilter = it.tableFilters
                    .find { filter -> filter.isSelected }?.value.orEmpty()
                return@map TableAndPostDataKey(it.dataKey, tableFilter, it.maxData, it.maxDisplay)
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

    private fun fetchAnnouncementData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        performanceMonitoringAnnouncementWidget =
            PerformanceMonitoring.start(ANNOUNCEMENT_WIDGET_TRACE)
        mViewModel.getAnnouncementWidgetData(dataKeys)
    }

    private fun selectDateRange() {
        if (!isAdded || context == null) return
        StatisticTracker.sendDateFilterEvent(userSession)

        val dateFilters: List<DateFilterItem> = statisticPage?.dateFilters.orEmpty()
        val identifierDescription = statisticPage?.exclusiveIdentifierDateFilterDesc.orEmpty()
        DateFilterBottomSheet.newInstance(
            dateFilters,
            identifierDescription,
            statisticPage?.pageSource.orEmpty()
        )
            .setOnApplyChanges {
                setHeaderSubTitle(it.getHeaderSubTitle(requireContext()))
                applyDateRange(it)
            }.show(childFragmentManager)

        val tabName = statisticPage?.pageTitle.orEmpty()
        StatisticTracker.sendCalendarClickEvent(userSession.userId, tabName, headerSubTitle)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun applyDateRange(item: DateFilterItem) {
        val startDate = item.startDate ?: return
        val endDate = item.endDate ?: return
        statisticPage?.let {
            mViewModel.setDateFilter(it.pageSource, startDate, endDate, item.getDateFilterType())
        }

        StatisticTracker.sendSetDateFilterEvent(
            statisticPage?.pageSource.orEmpty(),
            item.getDateFilterType(),
            item.startDate,
            item.endDate
        )
        adapter.data.forEach {
            when (it) {
                is SectionWidgetUiModel -> {
                    it.shouldShow = true
                }
                else -> {
                    it.isLoaded = false
                    it.data = null
                }
            }
        }
        adapter.notifyDataSetChanged()
        requestVisibleWidgetsData()
    }

    private fun requestVisibleWidgetsData() {
        mLayoutManager?.let {
            val firstVisible: Int = it.findFirstVisibleItemPosition()
            val lastVisible: Int = it.findLastVisibleItemPosition()
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
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView?.visible()
        binding?.globalErrorStc?.gone()

        val mWidgetList = mutableListOf<BaseWidgetUiModel<*>>()
        mWidgetList.add(tickerWidget)
        mWidgetList.addAll(widgets)
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
                    mLayoutManager?.scrollToPositionWithOffset(index, offset)
                }
            }
        } catch (e: StringIndexOutOfBoundsException) {
            Timber.e(e)
        }

        selectedWidget = ""
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets: Map<String, List<BaseWidgetUiModel<*>>> =
            widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { fetchCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { fetchLineGraphData(this) }
        groupedWidgets[WidgetType.MULTI_LINE_GRAPH]?.run { fetchMultiLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { fetchProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { fetchCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { fetchPostData(this) }
        groupedWidgets[WidgetType.TABLE]?.run { fetchTableData(this) }
        groupedWidgets[WidgetType.PIE_CHART]?.run { fetchPieChartData(this) }
        groupedWidgets[WidgetType.BAR_CHART]?.run { fetchBarChartData(this) }
        groupedWidgets[WidgetType.ANNOUNCEMENT]?.run { fetchAnnouncementData(this) }
    }

    private fun setOnErrorGetLayout(throwable: Throwable) = view?.run {
        if (adapter.data.isEmpty()) {
            binding?.globalErrorStc?.visible()
            recyclerView?.gone()
        } else {
            showErrorToaster()
            binding?.globalErrorStc?.gone()
        }
        binding?.swipeRefreshStc?.isRefreshing = false
        StatisticLogger.logToCrashlytics(throwable, StatisticLogger.ERROR_LAYOUT)
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.build(
            this, context.getString(R.string.stc_failed_to_get_information),
            TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.stc_reload)
        ) {
            reloadPageOrLoadDataOfErrorWidget()
        }.show()

        Handler(Looper.getMainLooper()).postDelayed({
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

    private fun reloadPage() = binding?.run {
        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshStc.isRefreshing = isAdapterNotEmpty

        globalErrorStc.gone()
        statisticPage?.let {
            mViewModel.getWidgetLayout(it.pageSource)
        }
    }

    private fun setProgressBarVisibility(isShown: Boolean) = binding?.run {
        if (isShown) {
            globalErrorStc.gone()
            progressBarStc.visible()
        } else {
            progressBarStc.gone()
        }
    }

    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(
        widgetType: String
    ) {
        val message = this.message.orEmpty()
        adapter.data.forEach { widget ->
            if (widget is W && widget.widgetType == widgetType) {
                val widgetData = D::class.java.newInstance().apply {
                    error = message
                }
                widget.data = widgetData
                widget.showLoadingState = false
                notifyWidgetChanged(widget)
            }
        }

        showErrorToaster()
        recyclerView?.post {
            requestVisibleWidgetsData()
        }
        StatisticLogger.logToCrashlytics(this, "${StatisticLogger.ERROR_WIDGET} $widgetType")
    }

    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(
        widgetType: String
    ) {
        forEach { widgetData ->
            adapter.data.find {
                val isSameDataKey = it.dataKey == widgetData.dataKey
                val isSameWidgetType = it.widgetType == widgetType
                return@find isSameDataKey && isSameWidgetType
            }?.let { widget ->
                if (widget is W) {
                    widget.data = widgetData
                    widget.showLoadingState = false
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
                binding?.swipeRefreshStc?.isRefreshing = false
            }
        }
    }

    private fun observeWidgetLayoutLiveData() {
        mViewModel.widgetLayout.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> setOnSuccessGetLayout(result.data)
                is Fail -> setOnErrorGetLayout(result.throwable)
            }
            setProgressBarVisibility(false)
        }
        setProgressBarVisibility(true)
    }

    private fun observeTickers() {
        mViewModel.tickers.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> showTickers(it.data)
                is Fail -> StatisticLogger.logToCrashlytics(
                    it.throwable,
                    StatisticLogger.ERROR_TICKER
                )
            }
        }
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(
        liveData: LiveData<Result<List<D>>>,
        type: String
    ) {
        liveData.observe(viewLifecycleOwner) { result ->
            startLayoutRenderingPerformanceMonitoring()

            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
            }

            stopPLTPerformanceMonitoring()
            stopWidgetPerformanceMonitoring(type)
        }
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
            val actionMenuBottomSheet =
                ActionMenuBottomSheet.createInstance(pageName, userSession.userId, menus)

            //to prevent IllegalStateException: Fragment already added
            if (actionMenuBottomSheet.isAdded) return

            actionMenuBottomSheet.show(childFragmentManager)

            StatisticTracker.sendThreeDotsClickEvent(statisticPage?.pageSource.orEmpty())
        }
    }

    private fun getTickerWidget(): Lazy<TickerWidgetUiModel> = lazy {
        val tickerData = TickerDataUiModel(tickers = emptyList(), dataKey = TICKER_NAME)
        return@lazy TickerWidgetUiModel(
            data = tickerData,
            title = TICKER_NAME,
            dataKey = TICKER_NAME
        )
    }

    private fun setMenuItemVisibility(menu: Menu) {
        val shouldShowActionMenu = !statisticPage?.actionMenu.isNullOrEmpty()
        menu.findItem(R.id.actionStcOtherMenu)?.isVisible = shouldShowActionMenu

        val minFilterSize = 1
        val shouldShowFilterMenu = statisticPage?.dateFilters?.size.orZero() >= minFilterSize
        menu.findItem(R.id.actionStcSelectDate)?.isVisible = shouldShowFilterMenu
    }

    private fun sendActionBarMenuImpressionEvent(menu: Menu) {
        //send impression for calendar filter action menu
        menu.findItem(R.id.actionStcSelectDate)?.let {
            view?.addOnImpressionListener(dateFilterImpressHolder) {
                val dateFilter = statisticPage?.dateFilters?.firstOrNull { it.isSelected }
                StatisticTracker.sendCalendarImpressionEvent(
                    statisticPage?.pageSource.orEmpty(),
                    dateFilter
                )
            }
        }

        //send impression for 3 dots action menu
        menu.findItem(R.id.actionStcOtherMenu)?.let {
            view?.addOnImpressionListener(otherMenuImpressHolder) {
                StatisticTracker.sendThreeDotsImpressionEvent(statisticPage?.pageSource.orEmpty())
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
            WidgetType.ANNOUNCEMENT -> performanceMonitoringAnnouncementWidget?.stopTrace()
        }
    }

    private fun getCategoryPage(): String? {
        return context?.let {
            TrackingHelper.getCategoryPage(it, statisticPage?.pageTitle.orEmpty())
        }
    }

    private fun checkForSectionToBeRemoved(removedPosition: Int) {
        val previousWidget = adapter.data.getOrNull(removedPosition.minus(Int.ONE))
        if (previousWidget is SectionWidgetUiModel) {
            if (adapter.data.getOrNull(removedPosition.plus(Int.ONE)) == null) {
                removeEmptySection(removedPosition.minus(Int.ONE))
            } else {
                removeSectionIfEmpty(removedPosition)
            }
        }
    }

    private fun removeSectionIfEmpty(removedPosition: Int) {
        var shouldRemoveSection = false
        adapter.data?.drop(removedPosition.plus(Int.ONE))?.forEach { widget ->
            when {
                widget.isShowEmpty || !widget.isEmpty() -> {
                    // If we found that the next widget should be shown, then we should not remove the section
                    return@forEach
                }
                widget is SectionWidgetUiModel -> {
                    shouldRemoveSection = true
                    return@forEach
                }
            }
        }
        if (shouldRemoveSection) {
            removeEmptySection(removedPosition.minus(Int.ONE))
        }
    }

    private fun removeEmptySection(position: Int) {
        (adapter.data.getOrNull(position) as? SectionWidgetUiModel)?.shouldShow = false
        adapter.notifyItemChanged(position)
    }

}
