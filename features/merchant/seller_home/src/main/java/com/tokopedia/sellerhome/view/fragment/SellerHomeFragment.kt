package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.CardViewHolder
import com.tokopedia.sellerhome.view.viewholder.LineGraphViewHolder
import com.tokopedia.sellerhome.view.viewholder.ProgressViewHolder
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
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
        CardViewHolder.Listener, LineGraphViewHolder.Listener, ProgressViewHolder.Listener {

    companion object {
        @JvmStatic
        fun newInstance() = SellerHomeFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var widgetHasMap = hashMapOf<String, MutableList<BaseWidgetUiModel<*>>>()
    private val sellerHomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }
    private var hasLoadCardData = false
    private var hasLoadLineGraphData = false
    private var hasLoadProgressData = false

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

        setupView()
        getWidgetsLayout()
        getTickerView()
        observeCardLiveData()
        observeLineGraphLiveData()
        observeProgressLiveData()
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

        swipeRefreshLayout.setOnRefreshListener {
            refreshWidget()
        }
    }

    private fun refreshWidget() = view?.run {
        hasLoadCardData = false
        hasLoadLineGraphData = false
        widgetHasMap.clear()
        adapter.data.clear()
        adapter.notifyDataSetChanged()
        showGetWidgetShimmer(false)
        sellerHomeViewModel.getWidgetLayout()
    }

    override fun getAdapterTypeFactory(): SellerHomeAdapterTypeFactory {
        return SellerHomeAdapterTypeFactory(this, this, this)
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

    private fun showGetWidgetShimmer(isShown: Boolean) {
        view?.swipeRefreshLayout?.isRefreshing = isShown
    }

    private fun showSwipeProgress(isShown: Boolean) {
        view?.swipeRefreshLayout?.isRefreshing = isShown
    }

    private fun getWidgetsLayout() {
        sellerHomeViewModel.widgetLayout.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    showGetWidgetShimmer(false)
                    renderList(result.data)
                    result.data.forEach {
                        if (widgetHasMap[it.widgetType].isNullOrEmpty()) {
                            widgetHasMap[it.widgetType] = mutableListOf(it)
                            return@forEach
                        }
                        widgetHasMap[it.widgetType]?.add(it)
                    }
                }
                is Fail -> {
                    showGetWidgetShimmer(false)
                }
            }
        })
        showGetWidgetShimmer(true)
        sellerHomeViewModel.getWidgetLayout()
    }

    private fun getTickerView() {
        sellerHomeViewModel.homeTicker.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showSwipeProgress(false)
                    onSuccessGetTickers(it.data)
                }
                is Fail -> {
                    showSwipeProgress(false)
                    view?.tickerView?.visibility = View.GONE
                }
            }
        })
        showSwipeProgress(true)
        sellerHomeViewModel.getTicker()
    }

    private fun observeCardLiveData() {
        sellerHomeViewModel.cardWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.CARD
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type)
            }
        })
    }

    private fun observeLineGraphLiveData() {
        sellerHomeViewModel.lineGraphWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.LINE_GRAPH
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type)
            }
        })
    }

    private fun observeProgressLiveData() {
        sellerHomeViewModel.progressWidgetData.observe(viewLifecycleOwner, Observer { result ->
            val type = WidgetType.PROGRESS
            when (result) {
                is Success -> result.data.setOnSuccessWidgetState(type)
                is Fail -> result.throwable.setOnErrorWidgetState(type)
            }
        })
    }

    private fun List<BaseDataUiModel>.setOnSuccessWidgetState(type: String) {
        widgetHasMap[type]?.forEachIndexed { i, widget ->
            when (widget) {
                is CardWidgetUiModel -> widget.data = this[i] as CardDataUiModel
                is LineGraphWidgetUiModel -> widget.data = this[i] as LineGraphDataUiModel
                is ProgressWidgetUiModel -> widget.data = this[i] as ProgressDataUiModel
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun Throwable.setOnErrorWidgetState(type: String) {
        val message = this.message.orEmpty()
        widgetHasMap[type]?.forEach { widget ->
            when (widget) {
                is CardWidgetUiModel -> widget.data = CardDataUiModel(error = message)
                is LineGraphWidgetUiModel -> widget.data = LineGraphDataUiModel(error = message)
                is ProgressWidgetUiModel -> widget.data = ProgressDataUiModel(error = message)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun onSuccessGetTickers(tickers: List<TickerUiModel>) {

        fun getTickerType(hexColor: String): Int = when (hexColor) {
            "#cde4c3" -> Ticker.TYPE_ANNOUNCEMENT
            "#ecdb77" -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }

        val isTickerVisible = if (tickers.isEmpty()) View.GONE else View.VISIBLE

        view?.tickerView?.run {
            visibility = isTickerVisible

            val tickersData = tickers.map {
                TickerData(it.title, it.message, getTickerType(it.color))
            }

            val adapter = TickerPagerAdapter(activity, tickersData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    //implement listener on click
                }
            })

            tickerView.addPagerView(adapter, tickersData)
        }
    }

    private inline fun <reified T : BaseWidgetUiModel<*>> getWidgetDataKeys(): List<String> {
        return adapter.data.orEmpty().filterIsInstance<T>().map { it.dataKey }
    }
}
