package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.SellerHomeTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CARD_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_CAROUSEL_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LINE_GRAPH_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_POST_LIST_TRACE
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_PROGRESS_TRACE
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.common.WidgetType
import com.tokopedia.sellerhome.common.exception.SellerHomeException
import com.tokopedia.sellerhome.common.utils.Utils
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
        private const val NOTIFICATION_BADGE_DELAY = 2000L
        private const val TAG_TOOLTIP = "seller_home_tooltip"
        private const val ERROR_LAYOUT = "Error get layout data."
        private const val ERROR_WIDGET = "Error get widget data."
        private const val ERROR_TICKER = "Error get ticker data."
        private const val TOAST_DURATION = 5000L

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
    private var performanceMonitoringSellerHomeCard: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeLineGraph: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeProgress: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomePostList: PerformanceMonitoring? = null
    private var performanceMonitoringSellerHomeCarousel: PerformanceMonitoring? = null


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
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad)
            reloadPage()
        if (userVisibleHint)
            SellerHomeTracking.sendScreen(screenName)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint)
            SellerHomeTracking.sendScreen(screenName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.sah_menu_toolbar_notification, menu)
        this.menu = menu
        showNotificationBadge()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == NOTIFICATION_MENU_ID) {
            RouteManager.route(requireContext(), ApplinkConst.SELLER_INFO)
            NavigationTracking.sendClickNotificationEvent()
        }
        return super.onOptionsItemSelected(item)
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
        recyclerView.layoutManager = gridLayoutManager
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)

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
        val dataKeys = Utils.getWidgetDataKeys<CardWidgetUiModel>(adapter.data)
        performanceMonitoringSellerHomeCard = PerformanceMonitoring.start(SELLER_HOME_CARD_TRACE)
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    override fun getLineGraphData() {
        if (hasLoadLineGraphData) return
        hasLoadLineGraphData = true
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(adapter.data)
        performanceMonitoringSellerHomeLineGraph = PerformanceMonitoring.start(SELLER_HOME_LINE_GRAPH_TRACE)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    override fun getProgressData() {
        if (hasLoadProgressData) return
        hasLoadProgressData = true
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(adapter.data)
        performanceMonitoringSellerHomeProgress = PerformanceMonitoring.start(SELLER_HOME_PROGRESS_TRACE)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    override fun getPostData() {
        if (hasLoadPostData) return
        hasLoadPostData = true
        val dataKeys = Utils.getWidgetDataKeys<PostListWidgetUiModel>(adapter.data)
        performanceMonitoringSellerHomePostList = PerformanceMonitoring.start(SELLER_HOME_POST_LIST_TRACE)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    override fun getCarouselData() {
        if (hasLoadCarouselData) return
        hasLoadCarouselData = true
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(adapter.data)
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
            stopPerformanceMonitoringSellerHomeLayout()
        })

        setProgressBarVisibility(true)
        sellerHomeViewModel.getWidgetLayout()
    }

    private fun stopPerformanceMonitoringSellerHomeLayout() {
        (activity as? SellerHomeActivity)?.stopPerformanceMonitoringSellerHomeLayout()
    }

    private fun setOnSuccessGetLayout(widgets: List<BaseWidgetUiModel<*>>) {
        view?.sahGlobalError?.gone()
        recyclerView.visible()

        adapter.data.clear()
        widgetHasMap.clear()

        adapter.data.addAll(widgets)
        widgets.forEach {
            if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                widgetHasMap[it.widgetType] = mutableListOf(it)
                return@forEach
            }
            widgetHasMap[it.widgetType]?.add(it)
        }

        renderWidgetOrGetWidgetDataFirst(widgets)

        setProgressBarVisibility(false)
    }

    /**
     * If first load then directly render widget so it show widget shimmer
     * Else it should get all widgets data then render the widget
     * */
    private fun renderWidgetOrGetWidgetDataFirst(widgets: List<BaseWidgetUiModel<*>>) {
        if (isFirstLoad)
            renderList(widgets)
        else
            getWidgetsData(widgets)

        isFirstLoad = false
    }

    private fun getWidgetsData(widgets: List<BaseWidgetUiModel<*>>) {
        widgets.forEachIndexed { i, widget ->
            when (widget.widgetType) {
                WidgetType.CARD -> getCardData()
                WidgetType.LINE_GRAPH -> getLineGraphData()
                WidgetType.PROGRESS -> getProgressData()
                WidgetType.CAROUSEL -> getCarouselData()
                WidgetType.POST_LIST -> getPostData()
                else -> adapter.notifyItemChanged(i)
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
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> {
                    logToCrashlytics(result.throwable, "$ERROR_WIDGET $type")
                    result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
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
        widgetHasMap[widgetType]?.forEachIndexed { i, widget ->
            if (widget is W && widget.dataKey == this[i].dataKey) {
                widget.data = this[i]
                notifyWidgetChanged(widget)
            }
        }
    }

    private inline fun <reified D : BaseDataUiModel, reified W : BaseWidgetUiModel<D>> Throwable.setOnErrorWidgetState(widgetType: String) {
        val message = this.message.orEmpty()
        widgetHasMap[widgetType]?.forEach { widget ->
            if (widget is W) {
                widget.data = D::class.java.newInstance().apply {
                    error = message
                }
                notifyWidgetChanged(widget)
            }
        }
        showErrorToaster()
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
