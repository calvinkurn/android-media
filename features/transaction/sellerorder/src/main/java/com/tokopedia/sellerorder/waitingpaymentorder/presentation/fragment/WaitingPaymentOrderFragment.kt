package com.tokopedia.sellerorder.waitingpaymentorder.presentation.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickCheckAndSetStockButton
import com.tokopedia.sellerorder.waitingpaymentorder.di.DaggerWaitingPaymentOrderComponent
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet.BottomSheetWaitingPaymentOrderTips
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentTickerUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel.WaitingPaymentOrderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_waiting_payment_order.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderFragment : BaseListFragment<Visitable<WaitingPaymentOrderAdapterTypeFactory>, WaitingPaymentOrderAdapterTypeFactory>() {

    companion object {
        private const val TAG_BOTTOM_SHEET = "bottom_sheet"
        private const val BUTTON_ENTER_LEAVE_ANIMATION_DURATION = 300L
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val waitingPaymentOrderViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WaitingPaymentOrderViewModel::class.java)
    }

    private val bottomSheetTips by lazy {
        BottomSheetWaitingPaymentOrderTips()
    }

    private val userSession by lazy { UserSession(context) }
    private var buttonEnterAnimation: ValueAnimator? = null
    private var buttonLeaveAnimation: ValueAnimator? = null
    private var shouldScrollToTop: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_waiting_payment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeWaitingPaymentOrderResult()
    }

    override fun onPause() {
        super.onPause()
        buttonEnterAnimation?.end()
        buttonLeaveAnimation?.end()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<WaitingPaymentOrderAdapterTypeFactory>, WaitingPaymentOrderAdapterTypeFactory> {
        return WaitingPaymentOrderAdapter(adapterTypeFactory)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return rvWaitingPaymentOrder
    }

    override fun getAdapterTypeFactory(): WaitingPaymentOrderAdapterTypeFactory {
        return WaitingPaymentOrderAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<WaitingPaymentOrderAdapterTypeFactory>?) {
        t?.let { item ->
            when (item) {
                is WaitingPaymentTickerUiModel -> showTipsBottomSheet()
            }
        }
    }

    override fun getScreenName(): String {
        return "WaitingPaymentOrderFragment"
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
        if (page == defaultInitialPage) {
            animateCheckAndSetStockButtonLeave()
        }
        waitingPaymentOrderViewModel.loadWaitingPaymentOrder(
                WaitingPaymentOrderRequestParam(
                        page = page,
                        batchPage = page,
                        nextPaymentDeadline = waitingPaymentOrderViewModel.paging.nextPaymentDeadline,
                        showPage = page
                )
        )
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(rvWaitingPaymentOrder.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!swipeRefreshLayoutWaitingPaymentOrder.isRefreshing) {
                    showLoading()
                    loadData(page)
                }
            }
        }
    }

    override fun showGetListError(throwable: Throwable?) {
        hideLoading()

        updateStateScrollListener()

        val errorType = if (throwable is UnknownHostException || throwable is SocketTimeoutException) 0 else 1
        (adapter as WaitingPaymentOrderAdapter).setErrorNetworkModel(errorType, this)

        if (adapter.itemCount > 0 && !isLoadingInitialData) {
            onGetListErrorWithExistingData(throwable)
            disableLoadMore()
        } else {
            onGetListErrorWithEmptyData(throwable)
            swipeRefreshLayoutWaitingPaymentOrder.isEnabled = false
        }
    }

    override fun onGetListErrorWithExistingData(throwable: Throwable?) {
        view?.run {
            Toaster.make(this, getString(R.string.global_error), Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR, getString(R.string.btn_reload), View.OnClickListener {
                enableLoadMore()
                scrollToBottomAfterLoadMoreViewInflated()
                endlessRecyclerViewScrollListener.loadMoreNextPage()
            })
        }
    }

    private fun setupViews() {
        setupRecyclerView()
        setupSwipeRefreshLayout()
        setupCheckAndSetStockButton()
    }

    private fun setupCheckAndSetStockButton() {
        btnCheckAndSetStock.setOnClickListener {
            goToProductManageSeller()
        }
    }

    private fun goToProductManageSeller() {
        context?.let { context ->
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
            } else {
                startActivity(intent)
            }
            if (intent != null) {
                eventClickCheckAndSetStockButton(
                        adapter.data.filterIsInstance<WaitingPaymentOrderUiModel>().size,
                        userSession.userId,
                        userSession.shopId
                )
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayoutWaitingPaymentOrder.setOnRefreshListener {
            shouldScrollToTop = true
            waitingPaymentOrderViewModel.resetPaging()
            isLoadingInitialData = true
            loadData(defaultInitialPage)
        }
    }

    private fun setupRecyclerView() {
        context?.run {
            with(rvWaitingPaymentOrder) {
                isNestedScrollingEnabled = true
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (isLoadingInitialData) shouldScrollToTop = false
                    }
                })
            }
        }
    }

    private fun showTipsBottomSheet() {
        bottomSheetTips.show(childFragmentManager, TAG_BOTTOM_SHEET)
    }

    private fun observeWaitingPaymentOrderResult() {
        waitingPaymentOrderViewModel.waitingPaymentOrderUiModelResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val newItems = ArrayList<Visitable<WaitingPaymentOrderAdapterTypeFactory>>(
                            result.data.map { newItem ->
                                val oldItem = adapter.data.find { oldItem ->
                                    oldItem is WaitingPaymentOrderUiModel && newItem.orderId == oldItem.orderId
                                }

                                newItem.isExpanded = (oldItem as? WaitingPaymentOrderUiModel)?.isExpanded
                                        ?: false
                                newItem
                            }
                    )
                    if (isLoadingInitialData) {
                        if (newItems.isNotEmpty()) {
                            newItems.add(0, createTicker())
                            (adapter as WaitingPaymentOrderAdapter).updateProducts(newItems)
                            animateCheckAndSetStockButtonEnter()
                        } else {
                            cardCheckAndSetStock.invisible()
                            (adapter as WaitingPaymentOrderAdapter).showEmpty()
                        }
                    } else {
                        hideLoading()
                        adapter.addMoreData(newItems)
                    }
                    updateScrollListenerState(hasNextPage(newItems.size))
                    swipeRefreshLayoutWaitingPaymentOrder.isEnabled = true
                }
                is Fail -> {
                    showGetListError(result.throwable)
                    if (isLoadingInitialData) {
                        animateCheckAndSetStockButtonLeave()
                    }
                }
            }
            swipeRefreshLayoutWaitingPaymentOrder.isRefreshing = false
        })
    }

    private fun createTicker(): WaitingPaymentTickerUiModel {
        return WaitingPaymentTickerUiModel(
                title = "",
                description = getString(R.string.som_waiting_payment_orders_ticker_description),
                type = Ticker.TYPE_INFORMATION,
                showCloseIcon = false
        )
    }

    private fun scrollToTopAfterRecyclerViewInflated() {
        rvWaitingPaymentOrder?.addOneTimeGlobalLayoutListener {
            rvWaitingPaymentOrder?.smoothScrollToPosition(0)
        }
    }

    private fun scrollToBottomAfterLoadMoreViewInflated() {
        rvWaitingPaymentOrder?.addOneTimeGlobalLayoutListener {
            rvWaitingPaymentOrder?.smoothScrollToPosition(adapter.dataSize - 1)
        }
    }

    private fun animateCheckAndSetStockButton(from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = BUTTON_ENTER_LEAVE_ANIMATION_DURATION
        animator.addUpdateListener { valueAnimator ->
            context?.let {
                cardCheckAndSetStock?.translationY = (valueAnimator.animatedValue as? Float).orZero()
            }
        }
        animator.start()
        return animator
    }

    private fun animateCheckAndSetStockButtonEnter() {
        if (buttonLeaveAnimation?.isRunning == true) buttonLeaveAnimation?.end()
        cardCheckAndSetStock?.visible()
        buttonEnterAnimation = animateCheckAndSetStockButton(cardCheckAndSetStock?.height?.toFloat() ?: 0f, 0f)
        buttonEnterAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                if (isLoadingInitialData) {
                    if (shouldScrollToTop) {
                        scrollToTopAfterRecyclerViewInflated()
                        shouldScrollToTop = false
                    }
                    isLoadingInitialData = false
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun animateCheckAndSetStockButtonLeave() {
        if (buttonEnterAnimation?.isRunning == true) buttonEnterAnimation?.end()
        buttonLeaveAnimation = animateCheckAndSetStockButton(0f, cardCheckAndSetStock?.height?.toFloat() ?: 0f)
        buttonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                cardCheckAndSetStock?.gone()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun hasNextPage(newItemsSize: Int) =
            waitingPaymentOrderViewModel.paging.nextPaymentDeadline != 0L && newItemsSize != 0
}