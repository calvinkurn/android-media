package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeWidgetListener
import com.tokopedia.sellerhome.WidgetType
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.bottomsheet.view.SellerHomeBottomSheetContent
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
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

class SellerHomeFragment : BaseListFragment<BaseWidgetUiModel<*>, SellerHomeAdapterTypeFactory>(), SellerHomeWidgetListener {

    companion object {
        @JvmStatic
        fun newInstance() = SellerHomeFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val mViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SellerHomeViewModel::class.java)
    }
    private val recyclerView: RecyclerView by lazy { super.getRecyclerView(view) }

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
    }

    override fun getAdapterTypeFactory(): SellerHomeAdapterTypeFactory {
        return SellerHomeAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: BaseWidgetUiModel<*>?) {

    }

    override fun loadData(page: Int) {

    }

    override fun onInfoTooltipClicked(tooltip: TooltipUiModel) {
        showTooltip(tooltip)
    }

    private fun showTooltip(tooltip: TooltipUiModel) {
        hideSoftKeyboardIfPresent()

        val bottomSheet = BottomSheetUnify()
        bottomSheet.setTitle(tooltip.title)
        bottomSheet.clearClose(false)
        bottomSheet.clearHeader(false)
        bottomSheet.setCloseClickListener { bottomSheet.dismiss() }
        
        val bottomSheetContentView = SellerHomeBottomSheetContent(context!!)
        bottomSheetContentView.setTooltipData(tooltip)

        bottomSheet.setChild(bottomSheetContentView)
        bottomSheet.show(childFragmentManager, "Seller Home Bottom Sheet")
    }

    private fun hideSoftKeyboardIfPresent() {
        activity?.let {
            CommonUtils.hideKeyboard(it, it.currentFocus)
        }
    }

    private fun getWidgetsLayout() {
        mViewModel.widgetLayout.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    showGetWidgetShimmer(false)
                    renderList(it.data)
                }
                is Fail -> {
                    showGetWidgetShimmer(false)
                }
            }
        })
        showGetWidgetShimmer(true)
        mViewModel.getWidgetLayout()
    }

    private fun getTickerView() {
        mViewModel.homeTicker.observe(viewLifecycleOwner, Observer {
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
        mViewModel.getTicker()
    }

    private fun showGetWidgetShimmer(isShown: Boolean) {

    }

    private fun showSwipeProgress(isShown: Boolean) {
        view?.swipeRefreshLayout?.isRefreshing = isShown
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
}
