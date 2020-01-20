package com.tokopedia.sellerhome.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.util.toast
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory
import com.tokopedia.sellerhome.view.model.BaseSellerHomeUiModel
import com.tokopedia.sellerhome.view.model.TickerUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeViewModel
import com.tokopedia.unifycomponents.ticker.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_seller_home.view.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeFragment : BaseListFragment<BaseSellerHomeUiModel, SellerHomeAdapterTypeFactory>() {

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
        return inflater.inflate(R.layout.fragment_seller_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTickerView()
    }

    override fun getAdapterTypeFactory(): SellerHomeAdapterTypeFactory {
        return SellerHomeAdapterTypeFactory()
    }

    override fun onItemClicked(t: BaseSellerHomeUiModel?) {

    }

    override fun loadData(page: Int) {

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
            adapter.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    context?.toast("Click : $linkUrl")
                }

                override fun onDismiss() {

                }
            })

            tickerView.addPagerView(adapter, tickersData)
        }
    }
}