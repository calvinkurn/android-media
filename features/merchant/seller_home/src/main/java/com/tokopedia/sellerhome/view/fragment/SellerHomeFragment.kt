package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringActivity
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationSearchTracking
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_ANNOUNCEMENT_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_BAR_CHART_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_MULTI_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PIE_CHART_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
import com.tokopedia.sellerhome.analytic.performance.SellerHomePerformanceMonitoringConstant.SELLER_HOME_TABLE_TRACE
import com.tokopedia.sellerhome.common.exception.SellerHomeException
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.domain.model.PROVINCE_ID_EMPTY
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.newrelic.SellerHomeNewRelic
import com.tokopedia.sellerhome.view.SellerHomeDiffUtilCallback
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.sellerhome.view.viewhelper.KetupatLottieView
import com.tokopedia.sellerhome.view.viewhelper.SellerHomeLayoutManager
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhome.view.widget.toolbar.NotificationDotBadge
import com.tokopedia.sellerhomecommon.common.WidgetListener
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactoryImpl
import com.tokopedia.sellerhomecommon.presentation.model.*
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.PostFilterBottomSheet
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.TooltipBottomSheet
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.EmptyState
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_sah.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeFragment : BaseListFragment<BaseWidgetUiModel<*>, WidgetAdapterFactoryImpl>(), WidgetListener,
        CoroutineScope, KetupatLottieView.Listener{

    companion object {
        @JvmStatic
        fun newInstance() = SellerHomeFragment()

        val NOTIFICATION_MENU_ID = R.id.menu_sah_notification
        val SEARCH_MENU_ID = R.id.menu_sah_search
        private const val NOTIFICATION_BADGE_DELAY = 2000L
        private const val TAG_TOOLTIP = "seller_home_tooltip"
        private const val ERROR_LAYOUT = "Error get layout data."
        private const val ERROR_WIDGET = "Error get widget data."
        private const val ERROR_TICKER = "Error get ticker data."
        private const val ERROR_THEMATIC = "Error get thematic illustration."
        private const val TOAST_DURATION = 5000L

        private const val MIN_LOTTIE_ANIM_SPEED = 2f
        private const val MEDIUM_LOTTIE_ANIM_SPEED = 3f
        private const val MAX_LOTTIE_ANIM_SPEED = 4f
        private const val SCROLL_DELTA_MIN_Y_THRESHOLD = 20f
        private const val SCROLL_DELTA_MAX_Y_THRESHOLD = 100f
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: SellerHomeRemoteConfig

    @Inject
    lateinit var newRelic: SellerHomeNewRelic

    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val recyclerView: RecyclerView?
        get() =
            try {
                super.getRecyclerView(view)
            } catch (ex: Exception) {
                null
            }

    private var sellerHomeListener: Listener? = null
    private var menu: Menu? = null
    private val notificationDotBadge: NotificationDotBadge? by lazy {
        NotificationDotBadge(context ?: return@lazy null)
    }

    private val rvOnScrollListener by lazy {
        object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val yDelta = abs(dy)
                val animSpeed = getAnimationSpeedByVerticalDelta(yDelta.toFloat())
                ketupatLottie?.animateKetupat(canLoadLottieAnimation, animSpeed)
                super.onScrolled(recyclerView, dx, dy)
            }
        }
    }

    private var notifCenterCount = 0
    private var isFirstLoad = true
    private var isErrorToastShown = false
    private var canLoadLottieAnimation = true

    private var performanceMonitoringSellerHomePltCompleted = false
    private var performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring? = null

    private var ketupatLottie: KetupatLottieView? = null
    private var emptyState: EmptyStateUnify? = null

    override fun getScreenName(): String = TrackingConstant.SCREEN_NAME_SELLER_HOME
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPltPerformanceMonitoring()
        startHomeLayoutNetworkMonitoring()
        startHomeLayoutCustomMetric()
        sellerHomeViewModel.getWidgetLayout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_sah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideTooltipIfExist()
        setupView()

        observeWidgetLayoutLiveData()
        observeShopLocationLiveData()
        observeWidgetData(sellerHomeViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(sellerHomeViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(sellerHomeViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(sellerHomeViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeWidgetData(sellerHomeViewModel.tableWidgetData, WidgetType.TABLE)
        observeWidgetData(sellerHomeViewModel.pieChartWidgetData, WidgetType.PIE_CHART)
        observeWidgetData(sellerHomeViewModel.barChartWidgetData, WidgetType.BAR_CHART)
        observeWidgetData(sellerHomeViewModel.multiLineGraphWidgetData, WidgetType.MULTI_LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.announcementWidgetData, WidgetType.ANNOUNCEMENT)
        observeTickerLiveData()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun onResume() {
        super.onResume()
        canLoadLottieAnimation = true
        if (!isFirstLoad)
            reloadPage()
    }

    override fun onPause() {
        super.onPause()
        hideTooltipIfExist()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            SellerHomeTracking.sendScreen(screenName)
            view?.post {
                requestVisibleWidgetsData()
            }
            recyclerView?.post {
                resetWidgetImpressionHolder()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.sah_menu_home_toolbar, menu)
        this.menu = menu
        showNotificationBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == NOTIFICATION_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SELLER_INFO)
            NavigationTracking.sendClickNotificationEvent()
        } else if (item.itemId == SEARCH_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SellerApp.SELLER_SEARCH)
            NavigationSearchTracking.sendClickSearchMenuEvent(userSession.userId.orEmpty())
        }
        return super.onOptionsItemSelected(item)
    }

    fun showNotificationBadge() {
        Handler().postDelayed({
            context?.let {
                val menuItem = menu?.findItem(NOTIFICATION_MENU_ID)
                if (notifCenterCount > 0) {
                    notificationDotBadge?.showBadge(menuItem ?: return@let)
                } else {
                    notificationDotBadge?.removeBadge(menuItem ?: return@let)
                }
            }
        }, NOTIFICATION_BADGE_DELAY)
    }

    private fun initPltPerformanceMonitoring() {
        performanceMonitoringSellerHomePlt = (activity as? SellerHomeActivity)?.performanceMonitoringSellerHomeLayoutPlt
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet is BottomSheetUnify)
            bottomSheet.dismiss()
    }

    private fun setupView() = view?.run {
        ketupatLottie = findViewById<KetupatLottieView>(R.id.lottie_seller_home_ketupat)?.apply {
            loadAnimationFromUrl()
            setListener(this@SellerHomeFragment)
        }
        emptyState = findViewById(R.id.empty_state_seller_home)

        val sellerHomeLayoutManager = SellerHomeLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return try {
                        val isCardWidget = adapter.data[position].widgetType != WidgetType.CARD
                        if (isCardWidget) spanCount else 1
                    } catch (e: IndexOutOfBoundsException) {
                        spanCount
                    }
                }
            }

            setOnVerticalScrollListener {
                requestVisibleWidgetsData()
            }
        }
        recyclerView?.run {
            layoutManager = sellerHomeLayoutManager
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            canLoadLottieAnimation = true
            addOnScrollListener(rvOnScrollListener)
        }

        swipeRefreshLayout.setOnRefreshListener {
            reloadPage()
            showNotificationBadge()
            sellerHomeListener?.getShopInfo()
        }

        sahGlobalError.setActionClickListener {
            reloadPage()
        }

        setViewBackground()
    }

    /**
     * load only visible widget on screen, except card widget should load all directly
     * */
    private fun requestVisibleWidgetsData() {
        val layoutManager = recyclerView?.layoutManager as? GridLayoutManager ?: return
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()

        val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        adapter.data.forEachIndexed { index, widget ->
            if (!widget.isLoaded) {
                if (widget.widgetType == WidgetType.CARD) {
                    visibleWidgets.add(widget)
                } else {
                    if (index in firstVisible..lastVisible) {
                        visibleWidgets.add(widget)
                    }
                }
            }
        }

        if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
    }

    private fun reloadPage() = view?.run {
        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshLayout.isRefreshing = isAdapterNotEmpty

        sahGlobalError.gone()
        sellerHomeViewModel.getWidgetLayout()
        sellerHomeViewModel.getTicker()
    }

    override fun getAdapterTypeFactory(): WidgetAdapterFactoryImpl {
        return WidgetAdapterFactoryImpl(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {

    }

    override fun loadData(page: Int) {

    }

    private fun List<BaseWidgetUiModel<*>>.setLoading() {
        forEach {
            it.isLoading = true
            it.isLoaded = true
        }
    }

    private fun getCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_CARD_TRACE)
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    private fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun getProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_PROGRESS_TRACE)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    private fun getPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys: List<Pair<String, String>> = widgets.filterIsInstance<PostListWidgetUiModel>().map {
            val postFilter = it.postFilter.find { filter -> filter.isSelected }?.value.orEmpty()
            return@map Pair(it.dataKey, postFilter)
        }
        startCustomMetric(SELLER_HOME_POST_LIST_TRACE)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    private fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_CAROUSEL_TRACE)
        sellerHomeViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun getTableData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<TableWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_TABLE_TRACE)
        sellerHomeViewModel.getTableWidgetData(dataKeys)
    }

    private fun getPieChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<PieChartWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_PIE_CHART_TRACE)
        sellerHomeViewModel.getPieChartWidgetData(dataKeys)
    }

    private fun getBarChartData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.setLoading()
        val dataKeys = Utils.getWidgetDataKeys<BarChartWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_BAR_CHART_TRACE)
        sellerHomeViewModel.getBarChartWidgetData(dataKeys)
    }

    private fun getMultiLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<MultiLineGraphWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_MULTI_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getMultiLineGraphWidgetData(dataKeys)
    }

    private fun getAnnouncementData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<AnnouncementWidgetUiModel>(widgets)
        startCustomMetric(SELLER_HOME_ANNOUNCEMENT_TRACE)
        sellerHomeViewModel.getAnnouncementWidgetData(dataKeys)
    }

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        if (!isAdded || context == null) return
        val tooltipBottomSheet = (childFragmentManager.findFragmentByTag(TAG_TOOLTIP) as? TooltipBottomSheet)
                ?: TooltipBottomSheet.createInstance()
        tooltipBottomSheet.init(requireContext(), tooltip)
        tooltipBottomSheet.show(childFragmentManager, TAG_TOOLTIP)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView?.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        showErrorToaster()
    }

    override fun sendCardImpressionEvent(model: CardWidgetUiModel) {
        SellerHomeTracking.sendImpressionCardEvent(model.dataKey,
                model.data?.state.orEmpty(), model.data?.value ?: "0")
    }

    override fun sendCardClickTracking(model: CardWidgetUiModel) {
        SellerHomeTracking.sendClickCardEvent(model.dataKey,
                model.data?.state.orEmpty(), model.data?.value ?: "0")
    }

    override fun sendCarouselImpressionEvent(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        SellerHomeTracking.sendImpressionCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselClickTracking(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {
        SellerHomeTracking.sendClickCarouselItemBannerEvent(dataKey, carouselItems, position)
    }

    override fun sendCarouselCtaClickEvent(dataKey: String) {
        SellerHomeTracking.sendClickCarouselCtaEvent(dataKey)
    }

    override fun sendDescriptionImpressionEvent(descriptionTitle: String) {
        SellerHomeTracking.sendImpressionDescriptionEvent(descriptionTitle)
    }

    override fun sendDescriptionCtaClickEvent(descriptionTitle: String) {
        SellerHomeTracking.sendClickDescriptionEvent(descriptionTitle)
    }

    override fun sendLineGraphImpressionEvent(model: LineGraphWidgetUiModel) {
        SellerHomeTracking.sendImpressionLineGraphEvent(model.dataKey, model.data?.header.orEmpty())
    }

    override fun sendLineGraphCtaClickEvent(dataKey: String, chartValue: String) {
        SellerHomeTracking.sendClickLineGraphEvent(dataKey, chartValue)
    }

    override fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendImpressionPostEvent(element, userSession.userId)
    }

    override fun sendPosListItemClickEvent(dataKey: String, title: String) {
        SellerHomeTracking.sendClickPostItemEvent(dataKey, title)
    }

    override fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendClickPostSeeMoreEvent(element, userSession.userId)
    }

    override fun sendPostListFilterClick(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendPostListFilterClick(element, userSession.userId)
    }

    override fun sendPostListEmptyStateCtaClickEvent(element: PostListWidgetUiModel) {
        SellerHomeTracking.sendPostEmptyStateCtaClick(element, userSession.userId)
    }

    override fun sendProgressImpressionEvent(dataKey: String, stateColor: String, valueScore: Int) {
        SellerHomeTracking.sendImpressionProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendProgressCtaClickEvent(dataKey: String, stateColor: String, valueScore: Int) {
        SellerHomeTracking.sendClickProgressBarEvent(dataKey, stateColor, valueScore)
    }

    override fun sendTableImpressionEvent(model: TableWidgetUiModel, slideNumber: Int, isSlideEmpty: Boolean) {
        val position = adapter.data.indexOf(model)
        SellerHomeTracking.sendTableImpressionEvent(model, position, slideNumber, isSlideEmpty)
    }

    override fun sendTableHyperlinkClickEvent(dataKey: String, url: String, isEmpty: Boolean) {
        SellerHomeTracking.sendTableClickHyperlinkEvent(dataKey, url, isEmpty, userSession.userId)
    }

    override fun sendTableEmptyStateCtaClickEvent(element: TableWidgetUiModel) {
        SellerHomeTracking.sendTableEmptyStateCtaClick(element, userSession.userId)
    }

    override fun sendPieChartImpressionEvent(model: PieChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        SellerHomeTracking.sendPieChartImpressionEvent(model, position)
    }

    override fun sendBarChartImpressionEvent(model: BarChartWidgetUiModel) {
        val position = adapter.data.indexOf(model)
        SellerHomeTracking.sendBarChartImpressionEvent(model, position)
    }

    override fun sendMultiLineGraphImpressionEvent(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphImpressionEvent(element, userSession.userId)
    }

    override fun sendMultiLineGraphMetricClick(element: MultiLineGraphWidgetUiModel, metric: MultiLineMetricUiModel) {
        SellerHomeTracking.sendMultiLineGraphMetricClick(element, metric, userSession.userId)
    }

    override fun sendMultiLineGraphCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphCtaClick(element, userSession.userId)
    }

    override fun sendMultiLineGraphEmptyStateCtaClick(element: MultiLineGraphWidgetUiModel) {
        SellerHomeTracking.sendMultiLineGraphEmptyStateCtaClick(element, userSession.userId)
    }

    override fun sendAnnouncementImpressionEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementImpressionEvent(element, userSession.userId)
    }

    override fun sendAnnouncementClickEvent(element: AnnouncementWidgetUiModel) {
        SellerHomeTracking.sendAnnouncementClickEvent(element, userSession.userId)
    }

    override fun showPostFilter(element: PostListWidgetUiModel, adapterPosition: Int) {
        if (!isAdded || context == null) return

        val postFilterBottomSheet = (childFragmentManager.findFragmentByTag(PostFilterBottomSheet.TAG) as? PostFilterBottomSheet)
                ?: PostFilterBottomSheet.newInstance()
        postFilterBottomSheet.init(requireContext(), element.postFilter) {
            recyclerView?.post {
                val copiedWidget = element.copy().apply { data = null }
                notifyWidgetChanged(copiedWidget)
                getPostData(listOf(element))
            }
        }.show(childFragmentManager)
    }

    override fun onAnimationStarted() {
        canLoadLottieAnimation = false
    }

    override fun onAnimationFinished() {
        canLoadLottieAnimation = true
    }

    override fun onDownloadAnimationFailed(ex: Throwable) {
        logToCrashlytics(ex, ERROR_THEMATIC)
    }

    private fun setProgressBarVisibility(isShown: Boolean) {
        view?.progressBarSah?.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun observeShopLocationLiveData() {
        sellerHomeViewModel.shopLocation.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> setOnSuccessGetShopLocation(result.data)
                is Fail -> {
                    result.throwable.printStackTrace()
                }
            }
            setProgressBarVisibility(false)
        })

        setProgressBarVisibility(true)
        sellerHomeViewModel.getShopLocation()
    }

    private fun setOnSuccessGetShopLocation(data: ShippingLoc) {
        if (data.provinceID == PROVINCE_ID_EMPTY) {
            activity?.let {
                RouteManager.route(it, ApplinkConst.CREATE_SHOP)
                it.finish()
            }
        }
    }

    private fun observeWidgetLayoutLiveData() {
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, { result ->
            recyclerView?.post {
                when (result) {
                    is Success -> {
                        stopLayoutCustomMetric(result.data)
                        setOnSuccessGetLayout(result.data)
                    }
                    is Fail -> {
                        stopCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE, true)
                        setOnErrorGetLayout(result.throwable)
                    }
                }
            }
        })

        view?.swipeRefreshLayout?.isRefreshing = true
        setProgressBarVisibility(true)
    }

    private fun stopLayoutCustomMetric(widgets: List<BaseWidgetUiModel<*>>) {
        val isFromCache = widgets.firstOrNull()?.isFromCache == true
        stopCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE, isFromCache)
    }

    private fun startHomeLayoutCustomMetric() {
        startCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE)
    }

    private fun startHomeLayoutNetworkMonitoring() {
        performanceMonitoringSellerHomePlt?.startNetworkPerformanceMonitoring()
    }

    private fun startHomeLayoutRenderMonitoring() {
        performanceMonitoringSellerHomePlt?.startRenderPerformanceMonitoring()
    }

    @Suppress("UNCHECKED_CAST")
    private fun stopHomeLayoutRenderMonitoring(fromCache: Boolean) {
        performanceMonitoringSellerHomePlt?.addDataSourceAttribution(fromCache)
        performanceMonitoringSellerHomePlt?.stopRenderPerformanceMonitoring()
        activity?.let {
            (it as LoadTimeMonitoringActivity).loadTimeMonitoringListener?.onStopPltMonitoring()
            newRelic.sendSellerHomeNewRelicData(it.application, screenName, userSession.userId, performanceMonitoringSellerHomePlt)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        view?.sahGlobalError?.gone()
        recyclerView?.visible()

        adapter.hideLoading()
        hideSnackBarRetry()
        updateScrollListenerState(false)

        val newWidgetFromCache = widgets.first().isFromCache
        val newWidgets = if (adapter.data.isEmpty()) {
            widgets as List<BaseWidgetUiModel<BaseDataUiModel>>
        } else {
            if (newWidgetFromCache) {
                return
            } else {
                val oldWidgets = adapter.data as List<BaseWidgetUiModel<BaseDataUiModel>>
                val newWidgets = arrayListOf<BaseWidgetUiModel<*>>()
                widgets.forEach { newWidget ->
                    oldWidgets.find { isTheSameWidget(it, newWidget) }.let { oldWidget ->
                        if (oldWidget == null) {
                            newWidgets.add(newWidget)
                        } else {
                            if (oldWidget.isFromCache && !oldWidget.needToRefreshData(newWidget as BaseWidgetUiModel<BaseDataUiModel>)) {
                                newWidget.apply {
                                    data = oldWidget.data
                                    isLoaded = oldWidget.isLoaded
                                    isLoading = oldWidget.isLoading
                                }
                                val widgetData = newWidget.data
                                if (widgetData == null || !shouldRemoveWidget(newWidget, widgetData)) {
                                    newWidgets.add(newWidget)
                                }
                                Unit
                            } else {
                                newWidgets.add(newWidget)
                                Unit
                            }
                        }
                    }
                }
                newWidgets
            }
        }

        updateWidgets(newWidgets as List<BaseWidgetUiModel<BaseDataUiModel>>)

        val isAnyWidgetFromCache = adapter.data.any { it.isFromCache }
        if (!isAnyWidgetFromCache) {
            view?.swipeRefreshLayout?.isEnabled = true
            view?.swipeRefreshLayout?.isRefreshing = false
        }

        recyclerView?.post {
            requestVisibleWidgetsData()
        }

        setProgressBarVisibility(false)
    }

    private fun isTheSameWidget(oldWidget: BaseWidgetUiModel<*>, newWidget: BaseWidgetUiModel<*>): Boolean {
        return oldWidget.widgetType == newWidget.widgetType && oldWidget.title == newWidget.title &&
                oldWidget.subtitle == newWidget.subtitle && oldWidget.appLink == newWidget.appLink &&
                oldWidget.tooltip == newWidget.tooltip && oldWidget.ctaText == newWidget.ctaText &&
                oldWidget.dataKey == newWidget.dataKey && oldWidget.isShowEmpty == newWidget.isShowEmpty &&
                oldWidget.emptyState == newWidget.emptyState
    }

    @Suppress("UNCHECKED_CAST")
    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.ANNOUNCEMENT]?.run { getAnnouncementData(this) }
        groupedWidgets[WidgetType.CARD]?.run { getCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { getLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { getProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { getCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { getPostData(this) }
        groupedWidgets[WidgetType.TABLE]?.run { getTableData(this) }
        groupedWidgets[WidgetType.PIE_CHART]?.run { getPieChartData(this) }
        groupedWidgets[WidgetType.BAR_CHART]?.run { getBarChartData(this) }
        groupedWidgets[WidgetType.MULTI_LINE_GRAPH]?.run { getMultiLineGraphData(this) }
        groupedWidgets[WidgetType.SECTION]?.run {
            recyclerView?.post {
                val newWidgetList = adapter.data.toMutableList()
                forEach { section ->
                    newWidgetList.indexOf(section).takeIf { it > -1 }?.let { index ->
                        newWidgetList[index] = section.copy().apply { isLoaded = true }
                    }
                }
                updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
                checkLoadingWidgets()
            }
        }
    }

    private fun setOnErrorGetLayout(throwable: Throwable) = view?.run {
        if (adapter.data.isEmpty()) {
            sahGlobalError.visible()
        } else {
            showErrorToaster()
            sahGlobalError.gone()
        }
        view?.swipeRefreshLayout?.isRefreshing = false
        setProgressBarVisibility(false)

        logToCrashlytics(throwable, ERROR_LAYOUT)
    }

    private fun showErrorViewByException(throwable: Throwable) = view?.run {

    }

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.build(this, context.getString(R.string.sah_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.sah_reload)
        ) {
            reloadPageOrLoadDataOfErrorWidget()
        }.show()

        Handler().postDelayed({
            isErrorToastShown = false
        }, TOAST_DURATION)
    }

    /**
     * if any widget that failed when load their data, the action should be load the widget data
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

    private fun observeTickerLiveData() {
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    stopCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE, it.data.firstOrNull()?.isFromCache
                            ?: false)
                    onSuccessGetTickers(it.data)
                }
                is Fail -> {
                    stopCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE, false)
                    logToCrashlytics(it.throwable, ERROR_TICKER)
                    view?.relTicker?.gone()
                }
            }
        })
        startCustomMetric(SellerHomePerformanceMonitoringConstant.SELLER_HOME_TICKER_TRACE)
        sellerHomeViewModel.getTicker()
    }

    private fun setViewBackground() = view?.run {
        val isOfficialStore = userSession.isShopOfficialStore
        val isPowerMerchant = userSession.isPowerMerchantIdle || userSession.isGoldMerchant
        when {
            isOfficialStore -> viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_official_store)
            isPowerMerchant -> viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_power_merchant)
            else -> viewBgShopStatus.setBackgroundColor(context.getResColor(android.R.color.transparent))
        }
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            recyclerView?.post {
                startHomeLayoutRenderMonitoring()
                when (result) {
                    is Success -> result.data.setOnSuccessWidgetState(type)
                    is Fail -> {
                        stopSellerHomeFragmentWidgetPerformanceMonitoring(type, isFromCache = false)
                        stopPltMonitoringIfNotCompleted(fromCache = false)
                        logToCrashlytics(result.throwable, "$ERROR_WIDGET $type")
                        result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
                    }
                }
            }
        })
    }

    private fun stopSellerHomeFragmentWidgetPerformanceMonitoring(type: String, isFromCache: Boolean) {
        when (type) {
            WidgetType.CARD -> stopCustomMetric(SELLER_HOME_CARD_TRACE, isFromCache)
            WidgetType.LINE_GRAPH -> stopCustomMetric(SELLER_HOME_LINE_GRAPH_TRACE, isFromCache)
            WidgetType.PROGRESS -> stopCustomMetric(SELLER_HOME_PROGRESS_TRACE, isFromCache)
            WidgetType.POST_LIST -> stopCustomMetric(SELLER_HOME_POST_LIST_TRACE, isFromCache)
            WidgetType.CAROUSEL -> stopCustomMetric(SELLER_HOME_CAROUSEL_TRACE, isFromCache)
            WidgetType.TABLE -> stopCustomMetric(SELLER_HOME_TABLE_TRACE, isFromCache)
            WidgetType.PIE_CHART -> stopCustomMetric(SELLER_HOME_PIE_CHART_TRACE, isFromCache)
            WidgetType.BAR_CHART -> stopCustomMetric(SELLER_HOME_BAR_CHART_TRACE, isFromCache)
            WidgetType.MULTI_LINE_GRAPH -> stopCustomMetric(SELLER_HOME_MULTI_LINE_GRAPH_TRACE, isFromCache)
            WidgetType.ANNOUNCEMENT -> stopCustomMetric(SELLER_HOME_ANNOUNCEMENT_TRACE, isFromCache)
        }
    }

    private fun startCustomMetric(tag: String) {
        performanceMonitoringSellerHomePlt?.startCustomMetric(tag)
    }

    private fun stopCustomMetric(tag: String, isFromCache: Boolean) {
        performanceMonitoringSellerHomePlt?.addDataSourceAttribution(isFromCache)
        performanceMonitoringSellerHomePlt?.stopCustomMetric(tag)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(widgetType: String) {
        val isFromCache = firstOrNull()?.isFromCache == true
        stopSellerHomeFragmentWidgetPerformanceMonitoring(widgetType, isFromCache)
        stopPltMonitoringIfNotCompleted(isFromCache)
        val newWidgetList = adapter.data.toMutableList()
        forEach { widgetData ->
            newWidgetList.indexOfFirst {
                it.dataKey == widgetData.dataKey && it.widgetType == widgetType
            }.takeIf { it > -1 }?.let { index ->
                val widget = newWidgetList.getOrNull(index)
                if (widget is W) {
                    if (shouldRemoveWidget(widget, widgetData)) {
                        newWidgetList.removeAt(index)
                        removeEmptySections(newWidgetList, index)
                    } else {
                        val copiedWidget = widget.copy()
                        copiedWidget.data = widgetData
                        copiedWidget.isLoading = widget.data?.isFromCache ?: false
                        newWidgetList[index] = copiedWidget
                    }
                }
            }
        }
        updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        view?.addOneTimeGlobalLayoutListener {
            recyclerView?.post {
                checkLoadingWidgets()
                requestVisibleWidgetsData()
            }
        }
    }

    private fun shouldRemoveWidget(widget: BaseWidgetUiModel<*>, widgetData: BaseDataUiModel): Boolean {
        return !widget.isFromCache && !widgetData.isFromCache && (!widgetData.showWidget || (!widget.isShowEmpty && widgetData.shouldRemove()))
    }

    private fun removeEmptySections(newWidgetList: MutableList<BaseWidgetUiModel<*>>, removedWidgetIndex: Int) {
        val previousWidget = newWidgetList.getOrNull(removedWidgetIndex - 1)
        val widgetReplacement = newWidgetList.getOrNull(removedWidgetIndex)
        if ((widgetReplacement == null || widgetReplacement is SectionWidgetUiModel) && previousWidget is SectionWidgetUiModel) {
            newWidgetList.removeAt(removedWidgetIndex - 1)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(widgetType: String) {
        val message = this.message.orEmpty()
        val newWidgetList = adapter.data.map { widget ->
            val isSameWidgetType = widget.widgetType == widgetType
            if (widget is W && widget.data == null && widget.isLoaded && isSameWidgetType) {
                widget.copy().apply {
                    data = D::class.java.newInstance().apply {
                        error = message
                    }
                    isLoading = widget.data?.isFromCache ?: false
                }
            } else {
                widget
            }
        }
        updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        showErrorToaster()
        view?.addOneTimeGlobalLayoutListener {
            requestVisibleWidgetsData()
            checkLoadingWidgets()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val newWidgetList = adapter.data.map {
            if (it.dataKey == widget.dataKey && it.widgetType == widget.widgetType) widget
            else it
        }
        updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        checkLoadingWidgets()
    }

    private fun onSuccessGetTickers(tickers: List<TickerItemUiModel>) {

        fun getTickerType(hexColor: String?): Int = when (hexColor) {
            context?.getString(R.string.sah_ticker_warning) -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }

        view?.relTicker?.visibility = if (tickers.isEmpty()) View.GONE else View.VISIBLE
        view?.tickerView?.run {
            val tickersData = tickers.map {
                TickerData(it.title, it.message, getTickerType(it.color), true, it)
            }

            val adapter = TickerPagerAdapter(context, tickersData)
            addPagerView(adapter, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    if (!RouteManager.route(context, linkUrl.toString())) {
                        if (itemData is TickerUiModel)
                            RouteManager.route(context, itemData.redirectUrl)
                    }
                }
            })
        }
    }

    private fun logToCrashlytics(throwable: Throwable, message: String) {
        if (!BuildConfig.DEBUG) {
            val exceptionMessage = "$message - ${throwable.localizedMessage}"

            FirebaseCrashlytics.getInstance().recordException(SellerHomeException(
                    message = exceptionMessage,
                    cause = throwable
            ))
        } else {
            throwable.printStackTrace()
        }
    }

    private fun stopPltMonitoringIfNotCompleted(fromCache: Boolean) {
        if (!performanceMonitoringSellerHomePltCompleted) {
            performanceMonitoringSellerHomePltCompleted = true
            recyclerView?.addOneTimeGlobalLayoutListener {
                stopHomeLayoutRenderMonitoring(fromCache)
            }
        }
    }

    fun setNotifCenterCounter(count: Int) {
        this.notifCenterCount = count
        showNotificationBadge()
    }

    @Suppress("UNCHECKED_CAST")
    private fun resetWidgetImpressionHolder() {
        val newWidgetList = adapter.data.map { widget ->
            val isInvoked = widget.impressHolder.isInvoke
            when (widget) {
                !is SectionWidgetUiModel,
                !is TickerWidgetUiModel,
                !is WhiteSpaceUiModel -> {
                    if (isInvoked) {
                        widget.copy().apply { impressHolder = ImpressHolder() }
                    } else {
                        widget
                    }
                }
                else -> widget
            }
        }
        updateWidgets(newWidgetList as List<BaseWidgetUiModel<BaseDataUiModel>>)
        checkLoadingWidgets()
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateWidgets(newWidgets: List<BaseWidgetUiModel<BaseDataUiModel>>) {
        val diffUtilCallback = SellerHomeDiffUtilCallback(adapter.data as List<BaseWidgetUiModel<BaseDataUiModel>>, newWidgets)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        adapter.data.clear()
        adapter.data.addAll(newWidgets)
        diffUtilResult.dispatchUpdatesTo(adapter)
    }

    private fun checkLoadingWidgets() {
        val isAnyLoadingWidget = adapter.data.any { it.isLoading || (it.isLoaded && it.isFromCache) }
        if (!isAnyLoadingWidget) {
            view?.swipeRefreshLayout?.isRefreshing = false
            hideLoading()
        }
    }

    private fun getAnimationSpeedByVerticalDelta(deltaY: Float): Float {
        return when {
            deltaY <= SCROLL_DELTA_MIN_Y_THRESHOLD -> MIN_LOTTIE_ANIM_SPEED
            deltaY > SCROLL_DELTA_MAX_Y_THRESHOLD -> MAX_LOTTIE_ANIM_SPEED
            else -> MEDIUM_LOTTIE_ANIM_SPEED
        }
    }

    interface Listener {
        fun getShopInfo()
    }
}
