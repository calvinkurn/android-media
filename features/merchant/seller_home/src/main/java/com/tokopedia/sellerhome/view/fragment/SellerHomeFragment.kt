package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationSearchTracking
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.common.WidgetType
import com.tokopedia.sellerhome.common.exception.SellerHomeException
import com.tokopedia.sellerhome.common.utils.Utils
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.domain.model.GetShopStatusResponse
import com.tokopedia.sellerhome.domain.model.PROVINCE_ID_EMPTY
import com.tokopedia.sellerhome.domain.model.ShippingLoc
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.view.SellerHomeBottomSheetContent
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.*
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.sellerhome.view.widget.toolbar.NotificationDotBadge
import com.tokopedia.unifycomponents.BottomSheetUnify
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
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeFragment : BaseListFragment<BaseWidgetUiModel<*>, SellerHomeAdapterTypeFactory>(),
        CardViewHolder.Listener, LineGraphViewHolder.Listener, ProgressViewHolder.Listener,
        SectionViewHolder.Listener, PostListViewHolder.Listener, CarouselViewHolder.Listener {

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
        private const val TOAST_DURATION = 5000L
        private const val DELAY_FETCH_VISIBLE_WIDGET_DATA = 500L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: SellerHomeRemoteConfig

    private var widgetHasMap = hashMapOf<String, MutableList<BaseWidgetUiModel<*>>>()
    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val tooltipBottomSheet by lazy { BottomSheetUnify() }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }

    private var sellerHomeListener: Listener? = null
    private var menu: Menu? = null
    private val notificationDotBadge: NotificationDotBadge? by lazy {
        NotificationDotBadge(context ?: return@lazy null)
    }

    private var notifCenterCount = 0
    private var isFirstLoad = true
    private var isErrorToastShown = false

    private var hasLoadCardData = false
    private var hasLoadLineGraphData = false
    private var hasLoadProgressData = false
    private var hasLoadPostData = false
    private var hasLoadCarouselData = false
    private var performanceMonitoringSellerHomePltCompleted = false
    private var performanceMonitoringSellerHomeCard: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeLineGraph: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeProgress: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomePostList: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeCarousel: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomePlt: HomeLayoutLoadTimeMonitoring? = null

    override fun getScreenName(): String = TrackingConstant.SCREEN_NAME_SELLER_HOME

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_sah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPltPerformanceMonitoring()
        hideTooltipIfExist()
        setupView()

        observeShopLocationLiveData()
        observeWidgetLayoutLiveData()
        observeWidgetData(sellerHomeViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(sellerHomeViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(sellerHomeViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(sellerHomeViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeTickerLiveData()
        observeShopStatusLiveData()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad)
            reloadPage()
        SellerHomeTracking.sendScreen(screenName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.sah_menu_toolbar_notification, menu)
        this.menu = menu
        showGlobalSearchIcon()
        showNotificationBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == NOTIFICATION_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SELLER_INFO)
            NavigationTracking.sendClickNotificationEvent()
        } else if(item.itemId == SEARCH_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SellerApp.SELLER_SEARCH)
            NavigationSearchTracking.sendClickSearchMenuEvent(userSession.userId.orEmpty())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initPltPerformanceMonitoring() {
        performanceMonitoringSellerHomePlt = if(remoteConfig.isNewSellerHomeDisabled()) {
            (activity as? com.tokopedia.sellerhome.view.oldactivity.SellerHomeActivity)?.performanceMonitoringSellerHomeLayoutPlt
        } else {
            (activity as? SellerHomeActivity)?.performanceMonitoringSellerHomeLayoutPlt
        }
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet is BottomSheetUnify)
            bottomSheet.dismiss()
    }

    private fun setupView() = view?.run {
        val gridLayoutManager = GridLayoutManager(context, 2).apply {
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
        }
        with (recyclerView) {
            layoutManager = gridLayoutManager

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        requestVisibleWidgetsData()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }

        swipeRefreshLayout.setOnRefreshListener {
            reloadPage()
            showNotificationBadge()
            sellerHomeViewModel.getShopStatus()
            sellerHomeListener?.getShopInfo()
        }

        sahGlobalError.setActionClickListener {
            reloadPage()
        }
    }

    private fun requestVisibleWidgetsData() {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()

        val visibleWidgets = mutableListOf<BaseWidgetUiModel<*>>()
        widgetHasMap.entries.forEach { pair ->
            if (pair.key == WidgetType.CARD) {
                visibleWidgets.addAll(pair.value.filter { !it.isLoaded })
            } else {
                pair.value.forEach { widget ->
                    val widgetIndexInRecyclerView = adapter.data.indexOf(widget)
                    if (widgetIndexInRecyclerView in firstVisible..lastVisible && !widget.isLoaded) {
                        visibleWidgets.add(widget)
                    }
                }
            }
        }

        if (visibleWidgets.isNotEmpty()) getWidgetsData(visibleWidgets)
    }

    private fun reloadPage() = view?.run {
        hasLoadCardData = false
        hasLoadLineGraphData = false
        hasLoadProgressData = false
        hasLoadPostData = false
        hasLoadCarouselData = false

        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        setProgressBarVisibility(!isAdapterNotEmpty)
        swipeRefreshLayout.isRefreshing = isAdapterNotEmpty

        sahGlobalError.gone()
        sellerHomeViewModel.getWidgetLayout()
        sellerHomeViewModel.getTicker()
    }

    override fun getAdapterTypeFactory(): SellerHomeAdapterTypeFactory {
        return SellerHomeAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {

    }

    override fun loadData(page: Int) {

    }

    override fun getCardData() {
        if (hasLoadCardData) return
        hasLoadCardData = true
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(adapter.data.filter { !it.data?.error.isNullOrBlank() })
        performanceMonitoringSellerHomeCard = PerformanceMonitoring.start(SELLER_HOME_CARD_TRACE)
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    private fun getCardData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(widgets)
        performanceMonitoringSellerHomeCard = PerformanceMonitoring.start(SELLER_HOME_CARD_TRACE)
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    override fun getLineGraphData() {
        if (hasLoadLineGraphData) return
        hasLoadLineGraphData = true
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(adapter.data.filter { !it.data?.error.isNullOrBlank() })
        performanceMonitoringSellerHomeLineGraph = PerformanceMonitoring.start(SELLER_HOME_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    private fun getLineGraphData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(widgets)
        performanceMonitoringSellerHomeLineGraph = PerformanceMonitoring.start(SELLER_HOME_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    override fun getProgressData() {
        if (hasLoadProgressData) return
        hasLoadProgressData = true
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(adapter.data.filter { !it.data?.error.isNullOrBlank() })
        performanceMonitoringSellerHomeProgress = PerformanceMonitoring.start(SELLER_HOME_PROGRESS_TRACE)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    private fun getProgressData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(widgets)
        performanceMonitoringSellerHomeProgress = PerformanceMonitoring.start(SELLER_HOME_PROGRESS_TRACE)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    override fun getPostData() {
        if (hasLoadPostData) return
        hasLoadPostData = true
        val dataKeys = Utils.getWidgetDataKeys<PostListWidgetUiModel>(adapter.data.filter { !it.data?.error.isNullOrBlank() })
        performanceMonitoringSellerHomePostList = PerformanceMonitoring.start(SELLER_HOME_POST_LIST_TRACE)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    private fun getPostData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<PostListWidgetUiModel>(widgets)
        performanceMonitoringSellerHomePostList = PerformanceMonitoring.start(SELLER_HOME_POST_LIST_TRACE)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    override fun getCarouselData() {
        if (hasLoadCarouselData) return
        hasLoadCarouselData = true
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(adapter.data.filter { !it.data?.error.isNullOrBlank() })
        performanceMonitoringSellerHomeCarousel = PerformanceMonitoring.start(SELLER_HOME_CAROUSEL_TRACE)
        sellerHomeViewModel.getCarouselWidgetData(dataKeys)
    }

    private fun getCarouselData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.onEach { it.isLoaded = true }
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(widgets)
        performanceMonitoringSellerHomeCarousel = PerformanceMonitoring.start(SELLER_HOME_CAROUSEL_TRACE)
        sellerHomeViewModel.getCarouselWidgetData(dataKeys)
    }

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        val bottomSheetContentView = SellerHomeBottomSheetContent(context ?: return)

        with(tooltipBottomSheet) {
            setTitle(tooltip.title)
            clearClose(false)
            clearHeader(false)
            setCloseClickListener {
                this.dismiss()
            }

            bottomSheetContentView.setTooltipData(tooltip)

            setChild(bottomSheetContentView)
            show(this@SellerHomeFragment.childFragmentManager, TAG_TOOLTIP)
        }
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
            widgetHasMap[widget.widgetType]?.remove(widget)
        }
    }

    override fun setOnErrorWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        showErrorToaster()
    }

    private fun showGlobalSearchIcon() {
        val menuItem = menu?.findItem(SEARCH_MENU_ID)
        menuItem?.isVisible = remoteConfig.isGlobalSearchEnabled()
    }

    private fun showNotificationBadge() {
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
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> setOnSuccessGetLayout(result.data)
                is Fail -> setOnErrorGetLayout(result.throwable)
            }
        })

        setProgressBarVisibility(true)
        startHomeLayoutNetworkMonitoring()
        sellerHomeViewModel.getWidgetLayout()
    }

    private fun startHomeLayoutNetworkMonitoring() {
        performanceMonitoringSellerHomePlt?.startNetworkPerformanceMonitoring()
    }

    private fun startHomeLayoutRenderMonitoring() {
        performanceMonitoringSellerHomePlt?.startRenderPerformanceMonitoring()
    }

    private fun stopHomeLayoutRenderMonitoring() {
        performanceMonitoringSellerHomePlt?.stopRenderPerformanceMonitoring()
        if(remoteConfig.isNewSellerHomeDisabled()) {
            (activity as? com.tokopedia.sellerhome.view.oldactivity.SellerHomeActivity)?.sellerHomeLoadTimeMonitoringListener?.onStopPltMonitoring()
        } else {
            (activity as? SellerHomeActivity)?.sellerHomeLoadTimeMonitoringListener?.onStopPltMonitoring()
        }
    }

    private fun stopPerformanceMonitoringSellerHomeLayout() {
        if(remoteConfig.isNewSellerHomeDisabled()) {
            (activity as? com.tokopedia.sellerhome.view.oldactivity.SellerHomeActivity)?.stopPerformanceMonitoringSellerHomeLayout()
        } else {
            (activity as? SellerHomeActivity)?.stopPerformanceMonitoringSellerHomeLayout()
        }
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        view?.sahGlobalError?.gone()
        recyclerView.visible()

        super.clearAllData()
        widgetHasMap.clear()

        super.renderList(widgets)
        adapter.notifyDataSetChanged()
        widgets.forEach {
            if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                widgetHasMap[it.widgetType] = mutableListOf(it)
                return@forEach
            }
            widgetHasMap[it.widgetType]?.add(it)
        }

        if (isFirstLoad) {
            recyclerView.post {
                requestVisibleWidgetsData()
            }
            isFirstLoad = false
        } else {
            requestVisibleWidgetsData()
        }

        setProgressBarVisibility(false)
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        val groupedWidgets = widgets.groupBy { it.widgetType }
        groupedWidgets[WidgetType.CARD]?.run { getCardData(this) }
        groupedWidgets[WidgetType.LINE_GRAPH]?.run { getLineGraphData(this) }
        groupedWidgets[WidgetType.PROGRESS]?.run { getProgressData(this) }
        groupedWidgets[WidgetType.CAROUSEL]?.run { getCarouselData(this) }
        groupedWidgets[WidgetType.POST_LIST]?.run { getPostData(this) }
        groupedWidgets[WidgetType.SECTION]?.run {
            forEach {
                if (!it.isLoaded) {
                    it.isLoaded = true
                    notifyWidgetChanged(it)
                }
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

    private fun showErrorToaster() = view?.run {
        if (isErrorToastShown) return@run
        isErrorToastShown = true

        Toaster.make(this, context.getString(R.string.sah_failed_to_get_information),
                TOAST_DURATION.toInt(), Toaster.TYPE_ERROR, context.getString(R.string.sah_reload),
                View.OnClickListener {
                    reloadPageOrLoadDataOfErrorWidget()
                }
        )

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
        adapter.data.forEachIndexed { index, widget ->
            if (!widget.data?.error.isNullOrBlank()) {
                when (widget.widgetType) {
                    WidgetType.CARD -> {
                        hasLoadCardData = false
                        getCardData()
                    }
                    WidgetType.LINE_GRAPH -> {
                        hasLoadLineGraphData = false
                        getLineGraphData()
                    }
                    WidgetType.PROGRESS -> {
                        hasLoadProgressData = false
                        getProgressData()
                    }
                    WidgetType.CAROUSEL -> {
                        hasLoadCarouselData = false
                        getCarouselData()
                    }
                    WidgetType.POST_LIST -> {
                        hasLoadPostData = false
                        getPostData()
                    }
                }
                widget.data?.error = ""
                widget.data = null
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun observeTickerLiveData() {
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetTickers(it.data)
                is Fail -> {
                    logToCrashlytics(it.throwable, ERROR_TICKER)
                    view?.relTicker?.gone()
                }
            }
        })
        sellerHomeViewModel.getTicker()
    }

    private fun observeShopStatusLiveData() {
        sellerHomeViewModel.shopStatus.observe(viewLifecycleOwner, Observer {
            if (it is Success)
                setOnSuccessGetShopStatus(it.data)
        })
        sellerHomeViewModel.getShopStatus()
    }

    private fun setOnSuccessGetShopStatus(goldPmOsStatus: GetShopStatusResponse) = view?.run {
        val mShopStatus = goldPmOsStatus.result.data
        val shopStatus: ShopStatus = when {
            mShopStatus.isOfficialStore() -> ShopStatus.OFFICIAL_STORE
            mShopStatus.isPowerMerchantActive() || mShopStatus.isPowerMerchantIdle() -> ShopStatus.POWER_MERCHANT
            else -> ShopStatus.REGULAR_MERCHANT
        }

        when (shopStatus) {
            ShopStatus.OFFICIAL_STORE -> {
                viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_official_store)
            }
            ShopStatus.POWER_MERCHANT -> {
                viewBgShopStatus.setBackgroundResource(R.drawable.sah_shop_state_bg_power_merchant)
            }
            else -> {
                viewBgShopStatus.setBackgroundColor(context.getResColor(android.R.color.transparent))
            }
        }
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            startHomeLayoutRenderMonitoring()
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> {
                    logToCrashlytics(result.throwable, "$ERROR_WIDGET $type")
                    result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
                }
            }
            if (!performanceMonitoringSellerHomePltCompleted) {
                performanceMonitoringSellerHomePltCompleted = true
                recyclerView.addOneTimeGlobalLayoutListener {
                    stopPerformanceMonitoringSellerHomeLayout()
                    stopHomeLayoutRenderMonitoring()
                }
            }
            stopSellerHomeFragmentWidgetPerformanceMonitoring(type)
        })
    }

    private fun stopSellerHomeFragmentWidgetPerformanceMonitoring(type: String) {
        when(type){
            WidgetType.CARD -> performanceMonitoringSellerHomeCard?.stopTrace()
            WidgetType.LINE_GRAPH -> performanceMonitoringSellerHomeLineGraph?.stopTrace()
            WidgetType.PROGRESS -> performanceMonitoringSellerHomeProgress?.stopTrace()
            WidgetType.POST_LIST -> performanceMonitoringSellerHomePostList?.stopTrace()
            WidgetType.CAROUSEL -> performanceMonitoringSellerHomeCarousel?.stopTrace()
        }
    }

    private inline fun <D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> List<D>.setOnSuccessWidgetState(widgetType: String) {
        forEach { widgetData ->
            widgetHasMap[widgetType]?.find { it.dataKey == widgetData.dataKey }?.let { widget ->
                if (widget is W) {
                    widget.data = widgetData
                    notifyWidgetChanged(widget)
                }
            }
        }
        view?.postDelayed({
            requestVisibleWidgetsData()
        }, DELAY_FETCH_VISIBLE_WIDGET_DATA)
    }

    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(widgetType: String) {
        val message = this.message.orEmpty()
        widgetHasMap[widgetType]?.forEach { widget ->
            if (widget is W && widget.data == null && widget.isLoaded) {
                widget.data = D::class.java.newInstance().apply {
                    error = message
                }
                notifyWidgetChanged(widget)
            }
        }
        showErrorToaster()
        view?.postDelayed({
            requestVisibleWidgetsData()
        }, DELAY_FETCH_VISIBLE_WIDGET_DATA)
    }

    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val widgetPosition = adapter.data.indexOf(widget)
        if (widgetPosition > -1) {
            adapter.notifyItemChanged(widgetPosition)
            view?.swipeRefreshLayout?.isRefreshing = false
        }
    }

    private fun onSuccessGetTickers(tickers: List<TickerUiModel>) {

        fun getTickerType(hexColor: String): Int = when (hexColor) {
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

            Crashlytics.logException(SellerHomeException(
                    message = exceptionMessage,
                    cause = throwable
            ))
        } else {
            throwable.printStackTrace()
        }
    }

    fun setNotifCenterCounter(count: Int) {
        this.notifCenterCount = count
        showNotificationBadge()
    }

    fun bindListener(listener: Listener?) {
        this.sellerHomeListener = listener
    }

    interface Listener {
        fun getShopInfo()
    }
}
