package com.tokopedia.sellerorder.waitingpaymentorder.presentation.fragment

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics.eventClickCheckAndSetStockButton
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.databinding.FragmentWaitingPaymentOrderBinding
import com.tokopedia.sellerorder.waitingpaymentorder.di.DaggerWaitingPaymentOrderComponent
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderRequestParam
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.WaitingPaymentOrderAdapter
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.bottomsheet.BottomSheetWaitingPaymentOrderTips
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.ErrorType
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentTickerUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel.WaitingPaymentOrderViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
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

    private var binding: FragmentWaitingPaymentOrderBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWaitingPaymentOrderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeWaitingPaymentOrderResult()
    }

    override fun onPause() {
        super.onPause()
        if (buttonEnterAnimation?.isRunning == true) {
            buttonEnterAnimation?.end()
        }
        if (buttonLeaveAnimation?.isRunning == true) {
            buttonLeaveAnimation?.end()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<WaitingPaymentOrderAdapterTypeFactory>, WaitingPaymentOrderAdapterTypeFactory> {
        return WaitingPaymentOrderAdapter(adapterTypeFactory)
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.rvWaitingPaymentOrder
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
        return object : EndlessRecyclerViewScrollListener(binding?.rvWaitingPaymentOrder?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (!binding?.swipeRefreshLayoutWaitingPaymentOrder?.isRefreshing.orFalse()) {
                    showLoading()
                    loadData(page)
                }
            }
        }
    }

    override fun showGetListError(throwable: Throwable?) {
        hideLoading()

        updateStateScrollListener()

        val errorType = if (throwable is UnknownHostException || throwable is SocketTimeoutException) ErrorType.NO_CONNECTION else ErrorType.SERVER_ERROR
        (adapter as WaitingPaymentOrderAdapter).setErrorNetworkModel(errorType, this)

        if (adapter.itemCount > 0 && !isLoadingInitialData) {
            onGetListErrorWithExistingData(throwable)
            disableLoadMore()
        } else {
            onGetListErrorWithEmptyData(throwable)
            binding?.swipeRefreshLayoutWaitingPaymentOrder?.isEnabled = false
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
        setupHeader()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        setupCheckAndSetStockButton()
    }

    private fun setupCheckAndSetStockButton() {
        binding?.btnCheckAndSetStock?.setOnClickListener {
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
        binding?.swipeRefreshLayoutWaitingPaymentOrder?.setOnRefreshListener {
            shouldScrollToTop = true
            waitingPaymentOrderViewModel.resetPaging()
            isLoadingInitialData = true
            loadData(defaultInitialPage)
        }
    }

    private fun setupHeader() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.headerWaitingPaymentOrder)
        }
    }

    private fun setupRecyclerView() {
        context?.run {
            binding?.rvWaitingPaymentOrder?.run {
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

                                newItem.isExpanded = (oldItem as? WaitingPaymentOrderUiModel)?.isExpanded ?: false
                                newItem.collapsedHeight = (oldItem as? WaitingPaymentOrderUiModel)?.collapsedHeight.orZero()
                                newItem.expandedHeight = (oldItem as? WaitingPaymentOrderUiModel)?.expandedHeight.orZero()
                                newItem
                            }
                    )
                    if (isLoadingInitialData) {
                        if (newItems.isNotEmpty()) {
                            newItems.add(0, createTicker())
                            (adapter as WaitingPaymentOrderAdapter).updateProducts(newItems)
                            animateCheckAndSetStockButtonEnter()
                        } else {
                            binding?.cardCheckAndSetStock?.invisible()
                            (adapter as WaitingPaymentOrderAdapter).showEmpty()
                        }
                    } else {
                        hideLoading()
                        adapter.addMoreData(newItems)
                    }
                    updateScrollListenerState(hasNextPage(newItems.size))
                    binding?.swipeRefreshLayoutWaitingPaymentOrder?.isEnabled = true
                }
                is Fail -> {
                    showGetListError(result.throwable)
                    if (isLoadingInitialData) {
                        animateCheckAndSetStockButtonLeave()
                    }
                    SomErrorHandler.logExceptionToServer(
                        errorTag = SomErrorHandler.SOM_TAG,
                        throwable = result.throwable,
                        errorType =
                        SomErrorHandler.SomMessage.GET_WAITING_PAYMENT_ORDER_LIST_ERROR,
                        deviceId = userSession.deviceId.orEmpty()
                    )
                }
            }
            binding?.swipeRefreshLayoutWaitingPaymentOrder?.isRefreshing = false
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
        binding?.rvWaitingPaymentOrder?.addOneTimeGlobalLayoutListener {
            binding?.rvWaitingPaymentOrder?.smoothScrollToPosition(Int.ZERO)
        }
    }

    private fun scrollToBottomAfterLoadMoreViewInflated() {
        binding?.rvWaitingPaymentOrder?.addOneTimeGlobalLayoutListener {
            binding?.rvWaitingPaymentOrder?.smoothScrollToPosition(adapter.dataSize - 1)
        }
    }

    private fun animateCheckAndSetStockButton(from: Float, to: Float): ValueAnimator {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = BUTTON_ENTER_LEAVE_ANIMATION_DURATION
        animator.addUpdateListener { valueAnimator ->
            context?.let {
                binding?.cardCheckAndSetStock?.translationY = (valueAnimator.animatedValue as? Float).orZero()
            }
        }
        animator.start()
        return animator
    }

    private fun animateCheckAndSetStockButtonEnter() {
        if (buttonLeaveAnimation?.isRunning == true) buttonLeaveAnimation?.end()
        binding?.cardCheckAndSetStock?.visible()
        buttonEnterAnimation = animateCheckAndSetStockButton(binding?.cardCheckAndSetStock?.height?.toFloat().orZero(), Float.ZERO)
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
        buttonLeaveAnimation = animateCheckAndSetStockButton(Float.ZERO, binding?.cardCheckAndSetStock?.height?.toFloat().orZero())
        buttonLeaveAnimation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                binding?.cardCheckAndSetStock?.gone()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun hasNextPage(newItemsSize: Int) =
            waitingPaymentOrderViewModel.paging.nextPaymentDeadline != 0L && newItemsSize != 0
}