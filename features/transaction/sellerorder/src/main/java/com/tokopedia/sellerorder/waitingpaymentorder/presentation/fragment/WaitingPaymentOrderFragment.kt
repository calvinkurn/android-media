package com.tokopedia.sellerorder.waitingpaymentorder.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.waitingpaymentorder.di.DaggerWaitingPaymentOrderComponent
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet.BottomSheetWaitingPaymentOrderTips
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel.WaitingPaymentOrderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_waiting_payment_order.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WaitingPaymentOrderFragment : BaseListFragment<WaitingPaymentOrder, WaitingPaymentOrderAdapterTypeFactory>() {

    companion object {
        const val TAG_BOTTOM_SHEET = "bottom_sheet"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val waitingPaymentOrderViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WaitingPaymentOrderViewModel::class.java)
    }

    private val bottomSheetTips by lazy {
        BottomSheetWaitingPaymentOrderTips()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waiting_payment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeWaitingPaymentOrderResult()
        fakeRefresh()
    }

    override fun createAdapterInstance(): BaseListAdapter<WaitingPaymentOrder, WaitingPaymentOrderAdapterTypeFactory> {
        return WaitingPaymentOrderAdapter(adapterTypeFactory)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return rvWaitingPaymentOrder
    }

    override fun getAdapterTypeFactory(): WaitingPaymentOrderAdapterTypeFactory {
        return WaitingPaymentOrderAdapterTypeFactory()
    }

    override fun onItemClicked(t: WaitingPaymentOrder?) {
        // still noop
    }

    override fun getScreenName(): String {
        return WaitingPaymentOrderFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            DaggerWaitingPaymentOrderComponent.builder()
                    .somComponent(SomComponentInstance.getSomComponent(it.application))
                    .build()
                    .inject(this)
        }
    }

    override fun loadData(page: Int) {
        Handler().postDelayed({
            waitingPaymentOrderViewModel.loadWaitingPaymentOrder(
                    WaitingPaymentOrderRequestParam(
                            page = waitingPaymentOrderViewModel.paging.currentPage,
                            batchPage = waitingPaymentOrderViewModel.paging.batchPage,
                            nextPaymentDeadline = waitingPaymentOrderViewModel.paging.nextPaymentDeadline
                    )
            )
        }, 2000)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(rvWaitingPaymentOrder.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                loadData(page)
            }
        }
    }

    override fun showGetListError(throwable: Throwable?) {
        hideLoading()

        updateStateScrollListener()

        val errorType = if (throwable is UnknownHostException || throwable is SocketTimeoutException) 0 else 1
        (adapter as WaitingPaymentOrderAdapter).setErrorNetworkModel(errorType, this)

        if (adapter.itemCount > 0) {
            onGetListErrorWithExistingData(throwable)
        } else {
            onGetListErrorWithEmptyData(throwable)
            swipeRefreshLayoutWaitingPaymentOrder.isEnabled = false
        }
    }

    override fun onGetListErrorWithExistingData(throwable: Throwable?) {
        view?.run {
            Toaster.make(this, "Unable to load more orders!", Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, "Muat Ulang", View.OnClickListener {
                enableLoadMore()
                endlessRecyclerViewScrollListener.loadMoreNextPage()
                rvWaitingPaymentOrder.smoothScrollToPosition(adapter.dataSize - 1)
            })
        }
    }

    private fun setupViews() {
        setupTicker()
        context?.run {
            with(rvWaitingPaymentOrder) {
                addItemDecoration(WaitingPaymentOrderAdapter.ItemDivider(this@run))
                isNestedScrollingEnabled = false
                itemAnimator?.addDuration = 500
                itemAnimator?.removeDuration = 500
                itemAnimator?.changeDuration = 500
                itemAnimator?.moveDuration = 500
            }
            swipeRefreshLayoutWaitingPaymentOrder.setOnRefreshListener {
                loadInitialData()
            }
        }
    }

    private fun setupTicker() {
        with(tickerWaitingPaymentOrder) {
            setHtmlDescription(getString(R.string.som_waiting_payment_orders_ticker_description))
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    showTipsBottomSheet()
                }

                override fun onDismiss() {
                    // no op
                }
            })
        }
    }

    private fun showTipsBottomSheet() {
        bottomSheetTips.show(childFragmentManager, TAG_BOTTOM_SHEET)
    }

    private fun fakeRefresh() {
//        Handler().postDelayed({
//            if (adapter.dataSize != 0) {
//                (adapter as WaitingPaymentOrderAdapter).updateProducts(adapter.data.takeLast(adapter.dataSize - 1))
//                fakeRefresh()
//            }
//        }, 10000)
    }

    private fun observeWaitingPaymentOrderResult() {
        waitingPaymentOrderViewModel.waitingPaymentOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val newItems = result.data
                    if (currentPage == 0) {
                        (adapter as WaitingPaymentOrderAdapter).updateProducts(newItems)
                    } else {
                        hideLoading()
                        adapter.addMoreData(newItems)
                    }
                    updateScrollListenerState(hasNextPage(newItems.size))
                    swipeRefreshLayoutWaitingPaymentOrder.isEnabled = true
                }
                is Fail -> {
                    showGetListError(result.throwable)
                }
            }
            swipeRefreshLayoutWaitingPaymentOrder.isRefreshing = false
        })
    }

    private fun hasNextPage(newItemsSize: Int) = waitingPaymentOrderViewModel.paging.nextPaymentDeadline.isNotEmpty() &&
            waitingPaymentOrderViewModel.paging.nextPaymentDeadline != "0" && newItemsSize != 0
}