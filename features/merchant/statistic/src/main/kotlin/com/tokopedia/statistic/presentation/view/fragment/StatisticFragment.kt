package com.tokopedia.statistic.presentation.view.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.common.const.DateFilterType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.StatisticTracker
import com.tokopedia.statistic.di.DaggerStatisticComponent
import com.tokopedia.statistic.presentation.view.bottomsheet.DateFilterBottomSheet
import com.tokopedia.statistic.presentation.view.model.DateFilterItem
import com.tokopedia.statistic.presentation.view.viewhelper.StatisticLayoutManager
import com.tokopedia.statistic.presentation.view.viewhelper.setOnTabSelectedListener
import com.tokopedia.statistic.presentation.view.viewmodel.StatisticViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_stc_statistic.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/06/20
 */

class StatisticFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener {

    companion object {
        private const val DEFAULT_START_DAYS = 6L
        private const val TOAST_DURATION = 5000L
        private const val SCREEN_NAME = "statistic_page_fragment"
        private const val TAG_TOOLTIP = "statistic_tooltip"
        private const val TICKER_NAME = "statistic_page_ticker"

        fun newInstance(): StatisticFragment {
            return StatisticFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val mViewModel: StatisticViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(StatisticViewModel::class.java)
    }
    private val mLayoutManager by lazy { StatisticLayoutManager(context, 2) }
    private val recyclerView by lazy { super.getRecyclerView(view) }
    private val dateFilterBottomSheet by lazy { DateFilterBottomSheet.newInstance() }
    private val defaultStartDate = Date(DateTimeUtil.getNPastDaysTimestamp(DEFAULT_START_DAYS))
    private val defaultEndDate = Date()
    private val job = Job()
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.Unconfined + job) }
    private val tickerWidget: TickerWidgetUiModel by getTickerWidget()

    private var tabItems = emptyList<Pair<String, String>>()
    private var isFirstLoad = true
    private var isErrorToastShown = false
    private var canSelectTabEnabled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_stc_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        hideTooltipIfExist()
        setupView()

        mViewModel.setDateFilter(defaultStartDate, defaultEndDate, DateFilterType.DATE_TYPE_DAY)

        observeWidgetLayoutLiveData()
        observeUserRole()
        observeWidgetData(mViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(mViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(mViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(mViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(mViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeWidgetData(mViewModel.tableWidgetData, WidgetType.TABLE)
        observeWidgetData(mViewModel.pieChartWidgetData, WidgetType.PIE_CHART)
        observeWidgetData(mViewModel.barChartWidgetData, WidgetType.BAR_CHART)
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad) {
            view?.appBarStc?.gone()
            reloadPage()
        }
        if (userVisibleHint)
            StatisticTracker.sendScreen(screenName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (!getRegularMerchantStatus()) {
            inflater.inflate(R.menu.menu_stc_action_calendar, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
            R.id.actionStcSelectDate -> selectDateRange()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvStcWidgets

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl {
        return WidgetAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        DaggerStatisticComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {}

    override fun loadData(page: Int) {}

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        if (!isAdded || context == null) return
        TooltipBottomSheet(requireContext(), tooltip)
                .show(this@StatisticFragment.childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {
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

    override fun sendCarouselImpressionEvent(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        StatisticTracker.sendImpressionCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselClickTracking(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        StatisticTracker.sendClickCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselCtaClickEvent(dataKey: String) {
        StatisticTracker.sendClickCarouselCtaEvent(dataKey)
    }

    override fun sendPosListItemClickEvent(dataKey: String, title: String) {
        StatisticTracker.sendClickPostItemEvent(dataKey, title)
    }

    override fun sendPostListCtaClickEvent(dataKey: String) {
        StatisticTracker.sendClickPostSeeMoreEvent(dataKey)
    }

    override fun sendPostListImpressionEvent(dataKey: String) {
        StatisticTracker.sendImpressionPostEvent(dataKey)
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

    override fun sendTableImpressionEvent(model: TableWidgetUiModel, slideNumber: Int, isSlideEmpty: Boolean) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendTableImpressionEvent(model, position, slideNumber, isSlideEmpty)
    }

    override fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendPieChartImpressionEvent(model, position)
    }

    override fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        StatisticTracker.sendBarChartImpressionEvent(model, position)
    }

    override fun sendSectionTooltipClickEvent(model: SectionWidgetUiModel) {
        StatisticTracker.sendSectionTooltipClickEvent(model.title)
    }

    private fun setupView() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(headerStcStatistic)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = activity.getString(R.string.stc_shop_statistic)
        }

        setDefaultRange()
        setupRecyclerView()

        tabLayoutStc.customTabMode = TabLayout.MODE_SCROLLABLE
        tabLayoutStc.tabLayout.setOnTabSelectedListener {
            setOnTabSelected()
        }

        swipeRefreshStc.setOnRefreshListener {
            reloadPage()
        }

        globalErrorStc.setActionClickListener {
            reloadPage()
        }
    }

    private fun getRegularMerchantStatus(): Boolean {
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        val isOfficialStore = userSession.isShopOfficialStore
        return !isPowerMerchant && !isOfficialStore
    }

    private fun setDefaultRange() = view?.run {
        val headerSubTitle: String = context.getString(R.string.stc_last_n_days_cc, DEFAULT_START_DAYS.plus(1))
        val startDateStr: String = DateTimeUtil.format(defaultStartDate.time, "dd")
        val endDateStr: String = DateTimeUtil.format(defaultEndDate.time, "dd MMM yyyy")
        val subTitle = "$headerSubTitle ($startDateStr - $endDateStr)"

        setHeaderSubTitle(subTitle)
    }

    private fun setHeaderSubTitle(subTitle: String) {
        view?.headerStcStatistic?.headerSubTitle = subTitle
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
                showTabLayout()
                selectTabOnScrolling()
                requestVisibleWidgetsData()
            }
        }

        with(recyclerView) {
            layoutManager = mLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    canSelectTabEnabled = newState == RecyclerView.SCROLL_STATE_IDLE
                }
            })

            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        (bottomSheet as? BottomSheetUnify)?.dismiss()
    }

    private fun fetchCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        mViewModel.getCardWidgetData(dataKeys)
    }

    private fun fetchLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        mViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun fetchProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        mViewModel.getProgressWidgetData(dataKeys)
    }

    private fun fetchPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<PostListWidgetUiModel>(widgets)
        mViewModel.getPostWidgetData(dataKeys)
    }

    private fun fetchCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        mViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun fetchTableData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<TableWidgetUiModel>(widgets)
        mViewModel.getTableWidgetData(dataKeys)
    }

    private fun fetchPieChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        mViewModel.getPieChartWidgetData(dataKeys)
    }

    private fun fetchBarChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEach { it.isLoaded = true }
        val dataKeys: List<String> = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        mViewModel.getBarChartWidgetData(dataKeys)
    }

    private fun selectDateRange() {
        if (!isAdded || context == null) return
        StatisticTracker.sendDateFilterEvent(userSession)
        dateFilterBottomSheet
                .setFragmentManager(childFragmentManager)
                .setOnApplyChanges {
                    setHeaderSubTitle(it.getHeaderSubTitle(requireContext()))
                    applyDateRange(it)
                }
                .show()
    }

    private fun applyDateRange(item: DateFilterItem) {
        StatisticTracker.sendSetDateFilterEvent(item.label)
        val startDate = item.startDate ?: return
        val endDate = item.endDate ?: return
        mViewModel.setDateFilter(startDate, endDate, item.getDateFilterType())
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
        coroutineScope.launch {
            val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
            adapter.data.forEachIndexed { index, widget ->
                if (index in firstVisible..lastVisible && !widget.isLoaded) {
                    visibleWidgets.add(widget)
                }
            }

            if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
        }
    }

    private fun showTabLayout() = view?.run {
        val firstVisibleIndex: Int = mLayoutManager.findFirstVisibleItemPosition()
        var shouldShowTabLayout = firstVisibleIndex > 0
        try {
            val firstVisibleWidget = adapter.data[0]
            val isTickerWidget = firstVisibleWidget is TickerWidgetUiModel
            if (isTickerWidget) {
                shouldShowTabLayout = firstVisibleIndex > 1
            }
        } catch (i: IndexOutOfBoundsException) {
        }

        if (shouldShowTabLayout) {
            appBarStc.visible()
        } else {
            appBarStc.gone()
        }
    }

    private fun selectTabOnScrolling() {
        val firstVisible: Int = mLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (firstVisible == RecyclerView.NO_POSITION) return

        val mostTopVisibleWidget: BaseWidgetUiModel<*> = adapter.data[firstVisible]
        val widgetPair: Pair<String, String> = Pair(mostTopVisibleWidget.title, mostTopVisibleWidget.dataKey)
        val tabPair: Pair<String, String> = tabItems.firstOrNull {
            it.second == widgetPair.second || it.second == widgetPair.first
        } ?: return
        val tabIndex: Int = tabItems.map { it.first }.distinct().indexOfFirst { it == tabPair.first }
        val tab = view?.tabLayoutStc?.tabLayout?.getTabAt(tabIndex)
        tab?.select()
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        recyclerView.visible()
        view?.globalErrorStc?.gone()

        val mWidgetList = mutableListOf<BaseWidgetUiModel<*>>()
        mWidgetList.add(tickerWidget)
        mWidgetList.addAll(widgets)
        mWidgetList.add(WhiteSpaceUiModel())
        adapter.data.clear()
        super.renderList(mWidgetList)

        setupTabItems()

        if (isFirstLoad) {
            recyclerView.post {
                requestVisibleWidgetsData()
            }
            isFirstLoad = false
        } else {
            requestVisibleWidgetsData()
        }

        showTabLayout()
    }

    private fun setupTabItems() = view?.run {
        tabLayoutStc.tabLayout.removeAllTabs()
        var sectionTitle = ""

        tabItems = adapter.data.filter { it !is TickerWidgetUiModel }
                .map {
                    return@map if (it.widgetType == WidgetType.SECTION) {
                        tabLayoutStc.addNewTab(it.title)
                        sectionTitle = it.title
                        Pair(it.title, it.title)
                    } else {
                        Pair(sectionTitle, it.dataKey)
                    }
                }
        selectTabOnScrolling()
    }

    private fun setOnTabSelected() = view?.run {
        if (!canSelectTabEnabled) return@run

        val selectedTabIndex = tabLayoutStc.tabLayout.selectedTabPosition
        val tabTitle: String = try {
            tabItems.map { it.first }.distinct()[selectedTabIndex]
        } catch (e: IndexOutOfBoundsException) {
            ""
        }

        val adapterIndex: Int = adapter.data.indexOfFirst { it.title == tabTitle }

        if (adapterIndex != RecyclerView.NO_POSITION) {
            val tabLayoutHeight: Int = if (selectedTabIndex != 0) tabLayoutStc.height else 0
            val widgetPosition: Int = if (selectedTabIndex != 0) adapterIndex else 0
            mLayoutManager.scrollToPositionWithOffset(widgetPosition, tabLayoutHeight)
            recyclerView.post {
                requestVisibleWidgetsData()
            }
        }

        if (selectedTabIndex == 0) {
            appBarStc.gone()
        }

        StatisticTracker.sendSelectSectionTabEvent(tabTitle)
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets: Map<String, List<BaseWidgetUiModel<*>>> = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { fetchCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { fetchLineGraphData(this) }
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
            recyclerView.gone()
        } else {
            showErrorToaster()
            globalErrorStc.gone()
        }
    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.make(this, context.getString(R.string.stc_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.stc_reload),
                View.OnClickListener {
                    reloadPageOrLoadDataOfErrorWidget()
                }
        )

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
        mViewModel.getWidgetLayout()
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
        this.printStackTrace()
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
        recyclerView.post {
            requestVisibleWidgetsData()
        }
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
        recyclerView.post {
            requestVisibleWidgetsData()
        }
    }

    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val widgetPosition = adapter.data.indexOf(widget)
        if (widgetPosition > -1) {
            adapter.notifyItemChanged(widgetPosition)
            view?.swipeRefreshStc?.isRefreshing = false
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
        mViewModel.getWidgetLayout()
    }

    private fun observeUserRole() {
        mViewModel.userRole.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                checkUserRole(it.data)
            }
        })
        mViewModel.getUserRole()
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
            }
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

    private fun getTickerWidget(): Lazy<TickerWidgetUiModel> = lazy {
        val tickerUrl = "https://docs.google.com/forms/d/1t-KeapZJwOeYOBnbXDEmzRJiUqMBicE9cQIauc40qMU"
        val title = context?.getString(R.string.stc_ticker_title).orEmpty()
        val message = context?.getString(R.string.stc_ticker_message, tickerUrl).orEmpty()
        val tickerItems = listOf(TickerItemUiModel(title = title, message = message, redirectUrl = tickerUrl))
        val tickerData = TickerDataUiModel(tickers = tickerItems, dataKey = TICKER_NAME)
        return@lazy TickerWidgetUiModel(data = tickerData, title = TICKER_NAME, dataKey = TICKER_NAME)
    }
}