package com.tokopedia.sellerhome.view.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.common.WidgetType
import com.tokopedia.sellerhome.common.utils.Utils
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.view.SellerHomeBottomSheetContent
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.*
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
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

        private const val TAG_TOOLTIP = "seller_home_tooltip"
        private const val TOAST_DURATION = 5000L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var pageRefreshListener: PageRefreshListener? = null

    private var widgetHasMap = hashMapOf<String, MutableList<BaseWidgetUiModel<*>>>()
    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val tooltipBottomSheet by lazy { BottomSheetUnify() }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }

    private var isFirstLoad = true
    private var isErrorToastShown = false

    private var hasLoadCardData = false
    private var hasLoadLineGraphData = false
    private var hasLoadProgressData = false
    private var hasLoadPostData = false
    private var hasLoadCarouselData = false

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sah, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideTooltipIfExist()
        setupView()

        observeWidgetLayoutLiveData()
        observeWidgetData(sellerHomeViewModel.cardWidgetData, WidgetType.CARD)
        observeWidgetData(sellerHomeViewModel.lineGraphWidgetData, WidgetType.LINE_GRAPH)
        observeWidgetData(sellerHomeViewModel.progressWidgetData, WidgetType.PROGRESS)
        observeWidgetData(sellerHomeViewModel.postListWidgetData, WidgetType.POST_LIST)
        observeWidgetData(sellerHomeViewModel.carouselWidgetData, WidgetType.CAROUSEL)
        observeTickerLiveData()
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstLoad)
            reloadPage()
    }

    fun setOnPageRefreshedListener(listener: PageRefreshListener) {
        this.pageRefreshListener = listener
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet is BottomSheetUnify)
            bottomSheet.dismiss()
    }

    private fun setupView() = view?.run {
        (activity as AppCompatActivity).setSupportActionBar(sahToolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            activity?.window?.statusBarColor = context.getResColor(R.color.Neutral_N0)
        activity?.setLightStatusBar(true)

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
            pageRefreshListener?.onRefreshPage()
        }

        sahGlobalError.setActionClickListener {
            reloadPage()
        }

        sahNestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            showToolbarShadow(scrollY != 0)
        }
    }

    private fun showToolbarShadow(isShown: Boolean) = view?.run {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return@run
        if (isShown) {
            sahToolbar.elevation = 10f
        } else {
            sahToolbar.elevation = 0f
        }
    }

    private fun reloadPage() = view?.run {
        hasLoadCardData = false
        hasLoadLineGraphData = false
        hasLoadProgressData = false
        hasLoadPostData = false
        hasLoadCarouselData = false

        val isAdapterNotEmpty = adapter.data.isNotEmpty()
        showGetWidgetProgress(!isAdapterNotEmpty)
        swipeRefreshLayout.isRefreshing = isAdapterNotEmpty

        sahGlobalError.gone()
        sellerHomeViewModel.getWidgetLayout()
    }

    fun setShopStatus(shopStatus: ShopStatus) = view?.run {
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
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    override fun getLineGraphData() {
        if (hasLoadLineGraphData) return
        hasLoadLineGraphData = true
        val dataKeys = Utils.getWidgetDataKeys<LineGraphWidgetUiModel>(adapter.data)
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    override fun getProgressData() {
        if (hasLoadProgressData) return
        hasLoadProgressData = true
        val dataKeys = Utils.getWidgetDataKeys<ProgressWidgetUiModel>(adapter.data)
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    override fun getPostData() {
        if (hasLoadPostData) return
        hasLoadPostData = true
        val dataKeys = Utils.getWidgetDataKeys<PostListWidgetUiModel>(adapter.data)
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    override fun getCarouselData() {
        if (hasLoadCarouselData) return
        hasLoadCarouselData = true
        val dataKeys = Utils.getWidgetDataKeys<CarouselWidgetUiModel>(adapter.data)
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

    private fun showGetWidgetProgress(isShown: Boolean) {
        view?.progressBarSah?.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun observeWidgetLayoutLiveData() {
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> setOnSuccessGetLayout(result.data)
                is Fail -> setOnErrorGetLayout()
            }
        })

        showGetWidgetProgress(true)
        sellerHomeViewModel.getWidgetLayout()
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

        showGetWidgetProgress(false)
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

    private fun setOnErrorGetLayout() = view?.run {
        if (adapter.data.isEmpty()) {
            sahGlobalError.visible()
        } else {
            showErrorToaster()
            sahGlobalError.gone()
        }
        view?.swipeRefreshLayout?.isRefreshing = false
        showGetWidgetProgress(false)
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
                is Fail -> view?.relTicker?.gone()
            }
        })
        sellerHomeViewModel.getTicker()
    }

    private inline fun <reified D : BaseDataUiModel> observeWidgetData(liveData: LiveData<Result<List<D>>>, type: String) {
        liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState<D, BaseWidgetUiModel<D>>(type)
            }
        })
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

    interface PageRefreshListener {
        fun onRefreshPage()
    }
}
