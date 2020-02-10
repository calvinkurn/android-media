package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.ShopStatus
import com.tokopedia.sellerhome.common.WidgetType
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.util.getResColor
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.view.SellerHomeBottomSheetContent
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.*
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
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
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var widgetHasMap = hashMapOf<String, MutableList<BaseWidgetUiModel<*>>>()
    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val tooltipBottomSheet by lazy { BottomSheetUnify() }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }

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

        observeTickerLiveData()
        observeWidgetLayoutLiveData()
        observeCardLiveData()
        observeLineGraphLiveData()
        observeProgressLiveData()
        observePostLiveData()
        observeCarouselLiveData()
    }

    private fun hideTooltipIfExist() {
        val bottomSheet = childFragmentManager.findFragmentByTag(TAG_TOOLTIP)
        if (bottomSheet != null) {
            (bottomSheet as BottomSheetUnify).dismiss()
        }
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
        ViewCompat.setNestedScrollingEnabled(recyclerView, false)
        recyclerView.layoutManager = gridLayoutManager

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            refreshWidget()
        }
    }

    private fun refreshWidget() = view?.run {
        hasLoadCardData = false
        hasLoadLineGraphData = false
        hasLoadProgressData = false
        hasLoadPostData = false
        hasLoadCarouselData = false
        widgetHasMap.clear()
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        showGetWidgetShimmer(true)
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
        val dataKeys = getWidgetDataKeys<CardWidgetUiModel>()
        sellerHomeViewModel.getCardWidgetData(dataKeys)
    }

    override fun getLineGraphData() {
        if (hasLoadLineGraphData) return
        hasLoadLineGraphData = true
        val dataKeys = getWidgetDataKeys<LineGraphWidgetUiModel>()
        sellerHomeViewModel.getLineGraphWidgetData(dataKeys)
    }

    override fun getProgressData() {
        if (hasLoadProgressData) return
        hasLoadProgressData = true
        val dataKeys = getWidgetDataKeys<ProgressWidgetUiModel>()
        sellerHomeViewModel.getProgressWidgetData(dataKeys)
    }

    override fun getPostData() {
        if (hasLoadPostData) return
        hasLoadPostData = true
        val dataKeys = getWidgetDataKeys<PostListWidgetUiModel>()
        sellerHomeViewModel.getPostWidgetData(dataKeys)
    }

    override fun getCarouselData() {
        if (hasLoadCarouselData) return
        hasLoadCarouselData = true
        val dataKeys = getWidgetDataKeys<CarouselWidgetUiModel>()
        sellerHomeViewModel.getCarouselWidgetData(dataKeys)
    }

    override fun removeWidget(position: Int, widget: BaseWidgetUiModel<*>) {
        recyclerView.post {
            adapter.data.remove(widget)
            adapter.notifyItemRemoved(position)
            widgetHasMap[widget.widgetType]?.remove(widget)
        }
    }

    private fun showGetWidgetShimmer(isShown: Boolean) {
        view?.progressBarSah?.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    private fun observeWidgetLayoutLiveData() {
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    recyclerView.visible()
                    renderList(result.data)
                    result.data.forEach {
                        if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                            widgetHasMap[it.widgetType] = mutableListOf(it)
                            return@forEach
                        }
                        widgetHasMap[it.widgetType]?.add(it)
                    }
                    showGetWidgetShimmer(false)
                }
                is Fail -> {
                    Toast.makeText(context, "Oops : ${result.throwable.message}", Toast.LENGTH_LONG).show()
                    showGetWidgetShimmer(false)
                }
            }
        })

        showGetWidgetShimmer(true)
        sellerHomeViewModel.getWidgetLayout()
    }

    override fun onTooltipClicked(tooltip: TooltipUiModel) {
        val bottomSheetContentView = SellerHomeBottomSheetContent(context ?: return)

        with(tooltipBottomSheet) tooltip@{
            setTitle(tooltip.title)
            clearClose(false)
            clearHeader(false)
            setCloseClickListener {
                this.dismiss()
            }

            bottomSheetContentView.setTooltipData(tooltip)

            setChild(bottomSheetContentView)
            this@tooltip.show(this@SellerHomeFragment.childFragmentManager, TAG_TOOLTIP)
        }
    }

    private fun observeTickerLiveData() {
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetTickers(it.data)
                is Fail -> view?.relTicker?.visibility = View.GONE
            }
        })
        sellerHomeViewModel.getTicker()
    }

    private fun observeCardLiveData() {
        sellerHomeViewModel.cardWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.CARD
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type, CardDataUiModel::class.java)
            }
        })
    }

    private fun observeLineGraphLiveData() {
        sellerHomeViewModel.lineGraphWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.LINE_GRAPH
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type, LineGraphDataUiModel::class.java)
            }
        })
    }

    private fun observePostLiveData() {
        sellerHomeViewModel.postWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.POST
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type, PostListDataUiModel::class.java)
            }
        })
    }

    private fun observeProgressLiveData() {
        sellerHomeViewModel.progressWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.PROGRESS
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type, ProgressDataUiModel::class.java)
            }
        })
    }

    private fun observeCarouselLiveData() {
        sellerHomeViewModel.carouselWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.CAROUSEL
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type, CarouselDataUiModel::class.java)
            }
        })
    }

    private inline fun <reified WIDGET : BaseWidgetUiModel<DATA>, DATA : BaseDataUiModel> List<DATA>.setOnSuccessWidgetState(widgetType: String) {
        widgetHasMap[widgetType]?.forEachIndexed { i, widget ->
            if (widget is WIDGET) {
                widget.data = this[i]
                notifyWidgetChanged(widget)
            }
        }
    }

    private inline fun <reified WIDGET : BaseWidgetUiModel<DATA>, DATA : BaseDataUiModel> Throwable.setOnErrorWidgetState(widgetType: String, data: Class<DATA>) {
        val message = this.message.orEmpty()
        widgetHasMap[widgetType]?.forEach { widget ->
            if (widget is WIDGET) {
                widget.data = data.newInstance().apply {
                    error = message
                }
                notifyWidgetChanged(widget)
            }
        }
    }

    private fun notifyWidgetChanged(widget: BaseWidgetUiModel<*>) {
        val widgetPosition = adapter.data.indexOf(widget)
        adapter.notifyItemChanged(widgetPosition)
    }

    private fun onSuccessGetTickers(tickers: List<TickerUiModel>) {

        fun getTickerType(hexColor: String): Int = when (hexColor) {
            "#cde4c3" -> Ticker.TYPE_ANNOUNCEMENT
            "#ecdb77" -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }

        val isTickerVisible = if (tickers.isEmpty()) View.GONE else View.VISIBLE

        view?.relTicker?.visibility = isTickerVisible
        view?.tickerView?.run {
            val tickersData = tickers.map {
                TickerData(it.title, it.message, getTickerType(it.color))
            }

            val adapter = TickerPagerAdapter(activity, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    //implement tooltipClickListener on click
                }
            })

            addPagerView(adapter, tickersData)
        }
    }

    private inline fun <reified T : BaseWidgetUiModel<*>> getWidgetDataKeys(): List<String> {
        return adapter.data.orEmpty().filterIsInstance<T>().map { it.dataKey }
    }
}
