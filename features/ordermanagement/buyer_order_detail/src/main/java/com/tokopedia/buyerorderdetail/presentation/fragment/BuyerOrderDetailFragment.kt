package com.tokopedia.buyerorderdetail.presentation.fragment

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.performance.BuyerOrderDetailLoadMonitoring
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.*
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.BuyerOrderDetailBottomSheetManager
import com.tokopedia.buyerorderdetail.presentation.partialview.BuyerOrderDetailStickyActionButton
import com.tokopedia.buyerorderdetail.presentation.helper.BuyerOrderDetailStickyActionButtonHandler
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductBundlingViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.TickerViewHolder
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailContentAnimator
import com.tokopedia.buyerorderdetail.presentation.dialog.RequestCancelResultDialog
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.StringUtils
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class BuyerOrderDetailFragment : BaseDaggerFragment(), ProductViewHolder.ProductViewListener,
        ProductBundlingViewHolder.Listener, TickerViewHolder.TickerViewHolderListener {

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragment {
            return BuyerOrderDetailFragment().apply {
                arguments = extras
            }
        }

        const val RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST = 100
        const val RESULT_CODE_CANCEL_ORDER_DISABLE = 102

        const val CHAT_ICON_TAG = "buyerOrderDetailToolbarChatIcon"

        private const val CONTENT_CHANGING_ANIMATION_DURATION = 300L
        private const val CONTENT_CHANGING_ANIMATION_DELAY = 45L

        private const val SAVED_INSTANCE_STATE_CACHE_MANAGER_KEY = "buyerOrderDetailSavedInstanceState"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var containerBuyerOrderDetail: ConstraintLayout? = null
    private var swipeRefreshBuyerOrderDetail: SwipeRefreshLayout? = null
    private var rvBuyerOrderDetail: RecyclerView? = null
    private var actionButtonWrapper: ConstraintLayout? = null
    private var toolbarBuyerOrderDetail: HeaderUnify? = null
    private var globalErrorBuyerOrderDetail: GlobalError? = null
    private var emptyStateBuyerOrderDetail: EmptyStateUnify? = null
    private var loaderBuyerOrderDetail: LoaderUnify? = null

    private val viewModel: BuyerOrderDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BuyerOrderDetailViewModel::class.java)
    }
    private val contentVisibilityAnimator by lazy {
        BuyerOrderDetailContentAnimator(swipeRefreshBuyerOrderDetail, rvBuyerOrderDetail)
    }
    private val cacheManager: SaveInstanceCacheManager by lazy {
        SaveInstanceCacheManager(requireContext(), true)
    }
    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this, this, navigator, this)
    }
    private val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
    }
    private val requestCancelResultDialog: RequestCancelResultDialog by lazy {
        RequestCancelResultDialog(navigator)
    }
    private val navigator: BuyerOrderDetailNavigator by lazy {
        BuyerOrderDetailNavigator(requireActivity(), this)
    }
    private val bottomSheetManager: BuyerOrderDetailBottomSheetManager by lazy {
        BuyerOrderDetailBottomSheetManager(requireContext(), childFragmentManager)
    }
    private val stickyActionButtonHandler: BuyerOrderDetailStickyActionButtonHandler by lazy {
        BuyerOrderDetailStickyActionButtonHandler(bottomSheetManager, cacheManager, getCartId(), navigator, viewModel)
    }
    private val stickyActionButton: BuyerOrderDetailStickyActionButton by lazy {
        BuyerOrderDetailStickyActionButton(bottomSheetManager, stickyActionButtonHandler, viewModel, view)
    }

    // show this chat icon only if there's no `Tanya Penjual` button on the sticky button
    private val chatIcon: IconUnify by lazy {
        createChatIcon(requireContext())
    }

    private fun createChatIcon(context: Context): IconUnify {
        return IconUnify(requireContext(), IconUnify.CHAT).apply {
            setOnClickListener {
                viewModel.buyerOrderDetailResult.value?.let {
                    if (it is Success) {
                        navigator.goToAskSeller(it.data)
                        BuyerOrderDetailTracker.eventClickChatIcon(
                                it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId,
                                it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId)
                    }
                }
            }
            layoutParams = LinearLayout.LayoutParams(
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt(),
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3).toInt()).apply {
                setMargins(0, 0, context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toInt(), 0)
            }
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
            tag = CHAT_ICON_TAG
        }
    }

    private val buyerOrderDetailLoadMonitoring: BuyerOrderDetailLoadMonitoring?
        get() {
            return activity?.let {
                if (it is BuyerOrderDetailActivity) it.buyerOrderDetailLoadMonitoring else null
            }
        }

    override fun getScreenName() = BuyerOrderDetailFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(BuyerOrderDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_buyer_order_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeBuyerOrderDetail()
        observeReceiveConfirmation()
        observeAddSingleToCart()
        observeAddMultipleToCart()
        buyerOrderDetailLoadMonitoring?.startNetworkPerformanceMonitoring()
        if (savedInstanceState == null) {
            loadInitialData()
        } else {
            restoreFragmentState(savedInstanceState)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_left_in_medium, com.tokopedia.resources.common.R.anim.slide_right_out_medium)
        when (requestCode) {
            BuyerOrderDetailIntentCode.REQUEST_CODE_REQUEST_CANCEL_ORDER -> handleRequestCancelResult(resultCode, data)
            BuyerOrderDetailIntentCode.REQUEST_CODE_CREATE_RESOLUTION -> handleComplaintResult()
            BuyerOrderDetailIntentCode.REQUEST_CODE_REFRESH_ONLY -> handleResultRefreshOnly()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                cacheManager.put(SAVED_INSTANCE_STATE_CACHE_MANAGER_KEY, it.data)
                outState.putString(BuyerOrderDetailCommonIntentParamKey.CACHE_ID, cacheManager.id)
            }
        }
    }

    override fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel) {
        val productCopy = product.copy(isProcessing = true)
        adapter.updateItem(product, productCopy)
        viewModel.addSingleToCart(productCopy)
        trackBuyAgainProduct(listOf(product))
    }

    override fun onPurchaseAgainButtonClicked(productBundlingUiModel: ProductListUiModel.ProductBundlingUiModel) {
//        TODO("Check for action from PM")
    }

    override fun onClickShipmentInfoTnC() {
        BuyerOrderDetailTracker.eventClickSeeShipmentInfoTNC(viewModel.getOrderStatusId(), viewModel.getOrderId())
    }

    private fun getCartId(): String {
        return activity?.intent?.extras?.getString(BuyerOrderDetailIntentParamKey.PARAM_CART_STRING, "") ?: ""
    }

    private fun restoreFragmentState(savedInstanceState: Bundle) {
        val cacheManagerId = savedInstanceState.getString(BuyerOrderDetailCommonIntentParamKey.CACHE_ID).orEmpty()
        cacheManager.id = cacheManagerId
        val savedBuyerOrderDetailData = cacheManager.get<BuyerOrderDetailUiModel>(SAVED_INSTANCE_STATE_CACHE_MANAGER_KEY, BuyerOrderDetailUiModel::class.java)
        if (savedBuyerOrderDetailData != null) {
            viewModel.restoreBuyerOrderDetailData(savedBuyerOrderDetailData)
        } else {
            loadInitialData()
        }
    }

    private fun loadInitialData() {
        showLoadIndicator()
        loadBuyerOrderDetail()
    }

    private fun setupViews() {
        bindViews()
        containerBuyerOrderDetail?.layoutTransition?.apply {
            setInterpolator(LayoutTransition.CHANGING, AccelerateInterpolator())
            enableTransitionType(LayoutTransition.CHANGING)
            setDuration(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DURATION)
            setStartDelay(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DELAY)
        }
        actionButtonWrapper?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        setupToolbar()
        setupGlobalError()
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    private fun bindViews() {
        containerBuyerOrderDetail = view?.findViewById(R.id.containerBuyerOrderDetail)
        swipeRefreshBuyerOrderDetail = view?.findViewById(R.id.swipeRefreshBuyerOrderDetail)
        rvBuyerOrderDetail = view?.findViewById(R.id.rvBuyerOrderDetail)
        actionButtonWrapper = view?.findViewById(R.id.actionButtonWrapper)
        toolbarBuyerOrderDetail = view?.findViewById(R.id.toolbarBuyerOrderDetail)
        globalErrorBuyerOrderDetail = view?.findViewById(R.id.globalErrorBuyerOrderDetail)
        emptyStateBuyerOrderDetail = view?.findViewById(R.id.emptyStateBuyerOrderDetail)
        loaderBuyerOrderDetail = view?.findViewById(R.id.loaderBuyerOrderDetail)
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
    }

    private fun setupGlobalError() {
        globalErrorBuyerOrderDetail?.setActionClickListener {
            globalErrorBuyerOrderDetail?.gone()
            showLoadIndicator()
            contentVisibilityAnimator.hideContent()
            loadBuyerOrderDetail()
        }
        emptyStateBuyerOrderDetail?.apply {
            setImageDrawable(resources.getDrawable(com.tokopedia.globalerror.R.drawable.unify_globalerrors_500, null))
            setPrimaryCTAText(context?.getString(com.tokopedia.globalerror.R.string.error500Action).orEmpty())
            setPrimaryCTAClickListener {
                emptyStateBuyerOrderDetail?.gone()
                showLoadIndicator()
                contentVisibilityAnimator.hideContent()
                loadBuyerOrderDetail()
            }
        }
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshBuyerOrderDetail?.isEnabled = false
        swipeRefreshBuyerOrderDetail?.setOnRefreshListener {
            loadBuyerOrderDetail()
        }
        swipeRefreshBuyerOrderDetail?.translationY = getScreenHeight().toFloat()
    }

    private fun setupRecyclerView() {
        rvBuyerOrderDetail?.adapter = adapter
    }

    private fun loadBuyerOrderDetail() {
        val orderId = arguments?.getString(BuyerOrderDetailCommonIntentParamKey.ORDER_ID, "").orEmpty()
        val paymentId = arguments?.getString(BuyerOrderDetailIntentParamKey.PARAM_PAYMENT_ID, "").orEmpty()
        val cart = arguments?.getString(BuyerOrderDetailIntentParamKey.PARAM_CART_STRING, "").orEmpty()
        viewModel.getBuyerOrderDetail(orderId, paymentId, cart)
    }

    private fun observeBuyerOrderDetail() {
        viewModel.buyerOrderDetailResult.observe(viewLifecycleOwner, { result ->
            buyerOrderDetailLoadMonitoring?.startRenderPerformanceMonitoring()
            hideLoadIndicator()
            when (result) {
                is Success -> onSuccessGetBuyerOrderDetail(result.data)
                is Fail -> onFailedGetBuyerOrderDetail(result.throwable)
            }
            swipeRefreshBuyerOrderDetail?.isRefreshing = false
            swipeRefreshBuyerOrderDetail?.isEnabled = true
        })
    }

    private fun observeReceiveConfirmation() {
        viewModel.finishOrderResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> onSuccessReceiveConfirmation(result.data)
                is Fail -> onFailedReceiveConfirmation(result.throwable)
            }
        })
    }

    private fun observeAddSingleToCart() {
        viewModel.singleAtcResult.observe(viewLifecycleOwner, { result ->
            when (val requestResult = result.second) {
                is Success -> onSuccessAddToCart(requestResult.data)
                is Fail -> onFailedAddToCart(requestResult.throwable)
            }
            adapter.updateItem(result.first, result.first.copy(isProcessing = false))
        })
    }

    private fun observeAddMultipleToCart() {
        viewModel.multiAtcResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> onSuccessAddToCart(result.data)
                is Fail -> onFailedAddToCart(result.throwable)
            }
            stickyActionButton.finishPrimaryActionButtonLoading()
        })
    }

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        stickyActionButton.setupActionButtons(data.actionButtonsUiModel)
        setupToolbarChatIcon(containsAskSellerButton(data.actionButtonsUiModel))
        adapter.updateItems(data)
        contentVisibilityAnimator.showContent()
        stopLoadTimeMonitoring()
    }

    private fun containsAskSellerButton(actionButtonsUiModel: ActionButtonsUiModel): Boolean {
        return actionButtonsUiModel.primaryActionButton.key == BuyerOrderDetailActionButtonKey.ASK_SELLER ||
                actionButtonsUiModel.secondaryActionButtons.any { it.key == BuyerOrderDetailActionButtonKey.ASK_SELLER }
    }

    private fun onSuccessReceiveConfirmation(data: FinishOrderResponse.Data.FinishOrderBuyer) {
        bottomSheetManager.finishReceiveConfirmationBottomSheetLoading()
        bottomSheetManager.dismissBottomSheets()
        showCommonToaster(data.message.firstOrNull().orEmpty())
        swipeRefreshBuyerOrderDetail?.isRefreshing = true
        loadBuyerOrderDetail()
    }

    private fun onFailedReceiveConfirmation(throwable: Throwable) {
        bottomSheetManager.finishReceiveConfirmationBottomSheetLoading()
        throwable.showErrorToaster()
    }

    private fun onSuccessAddToCart(data: AtcMultiData) {
        val msg = StringUtils.convertListToStringDelimiter(data.atcMulti.buyAgainData.message, ",")
        if (data.atcMulti.buyAgainData.success == 1) {
            showCommonToaster(msg, getString(R.string.label_see)) {
                navigator.openAppLink(ApplinkConst.CART, false)
            }
        } else {
            showErrorToaster(msg)
        }
    }

    private fun onFailedAddToCart(throwable: Throwable) {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information).orEmpty()
        showErrorToaster(errorMessage)
    }

    private fun onFailedGetBuyerOrderDetail(throwable: Throwable) {
        val errorType = when (throwable) {
            is MessageErrorException -> null
            is SocketTimeoutException, is UnknownHostException -> GlobalError.NO_CONNECTION
            else -> GlobalError.SERVER_ERROR
        }

        if (errorType == null) {
            globalErrorBuyerOrderDetail?.gone()
            emptyStateBuyerOrderDetail?.showMessageExceptionError(throwable)
        } else {
            globalErrorBuyerOrderDetail?.apply {
                setType(errorType)
                visible()
            }
            emptyStateBuyerOrderDetail?.gone()
        }
        contentVisibilityAnimator.hideContent()
        stopLoadTimeMonitoring()
    }

    private fun EmptyStateUnify.showMessageExceptionError(throwable: Throwable) {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, throwable)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information).orEmpty()
        setDescription(errorMessage)
        visible()
    }

    private fun setupToolbarChatIcon(containAskSellerButton: Boolean) {
        val orderId = viewModel.getOrderId()
        if (containAskSellerButton || orderId.isBlank() || orderId == "0") {
            toolbarBuyerOrderDetail?.apply {
                rightContentView.removeAllViews()
                rightIcons?.clear()
            }
        } else {
            toolbarBuyerOrderDetail?.apply {
                addCustomRightContent(chatIcon)
            }
        }
    }

    private fun showLoadIndicator() {
        loaderBuyerOrderDetail?.show()
    }

    private fun hideLoadIndicator() {
        loaderBuyerOrderDetail?.gone()
    }

    private fun showCommonToaster(message: String, actionText: String = "", onActionClicked: () -> Unit = {}) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionText) {
                    onActionClicked()
                }.show()
            }
        }
    }

    private fun showErrorToaster(message: String, actionText: String = "", onActionClicked: () -> Unit = {}) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, actionText) {
                    onActionClicked()
                }.show()
            }
        }
    }

    private fun Throwable.showErrorToaster() {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, this)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information).orEmpty()
        showErrorToaster(errorMessage)
    }

    private fun handleRequestCancelResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST) {
            val resultMessage = data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_MSG_INSTANT_CANCEL).orEmpty()
            val result = data?.getIntExtra(BuyerOrderDetailMiscConstant.RESULT_CODE_INSTANT_CANCEL, 1) ?: 1
            if (result == 1) {
                if (resultMessage.isNotBlank()) {
                    showCommonToaster(resultMessage)
                }
            } else if (result == 3) {
                val popupTitle = data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_POPUP_TITLE_INSTANT_CANCEL).orEmpty()
                val popupBody = data?.getStringExtra(BuyerOrderDetailMiscConstant.RESULT_POPUP_BODY_INSTANT_CANCEL).orEmpty()
                if (popupTitle.isNotBlank() && popupBody.isNotBlank()) {
                    context?.let { context ->
                        requestCancelResultDialog.apply {
                            setTitle(popupTitle)
                            setBody(popupBody)
                            show(context)
                        }
                    }
                }
            }
            if (result != 0) {
                loadBuyerOrderDetail()
            }
        } else if (resultCode == RESULT_CODE_CANCEL_ORDER_DISABLE) {
            loadBuyerOrderDetail()
        }
        bottomSheetManager.dismissBottomSheets()
    }

    private fun handleComplaintResult() {
        swipeRefreshBuyerOrderDetail?.isRefreshing = true
        loadBuyerOrderDetail()
        bottomSheetManager.dismissBottomSheets()
    }

    private fun handleResultRefreshOnly() {
        swipeRefreshBuyerOrderDetail?.isRefreshing = true
        loadBuyerOrderDetail()
        bottomSheetManager.dismissBottomSheets()
    }

    private fun stopLoadTimeMonitoring() {
        rvBuyerOrderDetail?.post {
            buyerOrderDetailLoadMonitoring?.stopRenderPerformanceMonitoring()
        }
    }

    private fun trackBuyAgainProduct(products: List<ProductListUiModel.ProductUiModel>) {
        BuyerOrderDetailTracker.eventClickBuyAgain(
                products,
                viewModel.getOrderId(),
                getCartId(),
                viewModel.getShopId(),
                viewModel.getShopName(),
                viewModel.getShopType(),
                viewModel.getUserId())
    }
}