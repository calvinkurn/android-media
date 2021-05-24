package com.tokopedia.buyerorderdetail.presentation.fragment

import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.performance.BuyerOrderDetailLoadMonitoring
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailActionButtonAnimator
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailContentAnimator
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.ReceiveConfirmationBottomSheet
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.dialog.RequestCancelResultDialog
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.StringUtils
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class BuyerOrderDetailFragment : BaseDaggerFragment(), ProductViewHolder.ProductViewListener, ActionButtonClickListener {

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragment {
            return BuyerOrderDetailFragment().apply {
                arguments = extras
            }
        }

        const val RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST = 100
        const val RESULT_CODE_CANCEL_ORDER_DISABLE = 102

        private const val CONTENT_CHANGING_ANIMATION_DURATION = 300L
        private const val CONTENT_CHANGING_ANIMATION_DELAY = 45L
    }

    @Inject
    lateinit var viewModel: BuyerOrderDetailViewModel

    private var bottomSheetReceiveConfirmation: ReceiveConfirmationBottomSheet? = null

    private val actionButtonAnimator by lazy {
        BuyerOrderDetailActionButtonAnimator(containerActionButtons, containerBuyerOrderDetail)
    }
    private val contentVisibilityAnimator by lazy {
        BuyerOrderDetailContentAnimator(swipeRefreshBuyerOrderDetail, rvBuyerOrderDetail)
    }
    private val cacheManager: SaveInstanceCacheManager by lazy {
        SaveInstanceCacheManager(requireContext())
    }
    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this, navigator)
    }
    private val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
    }
    private val secondaryActionButtonBottomSheet: SecondaryActionButtonBottomSheet by lazy {
        SecondaryActionButtonBottomSheet(requireContext(), this)
    }
    private val primaryActionButtonClickListener: View.OnClickListener by lazy {
        createPrimaryActionButtonClickListener()
    }
    private val secondaryActionButtonClickListener: View.OnClickListener by lazy {
        createSecondaryActionButtonClickListener()
    }
    private val requestCancelResultDialog: RequestCancelResultDialog by lazy {
        RequestCancelResultDialog(navigator)
    }
    private val navigator: BuyerOrderDetailNavigator by lazy {
        BuyerOrderDetailNavigator(requireActivity())
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
        loadInitialData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        activity?.overridePendingTransition(com.tokopedia.resources.common.R.anim.slide_right_in_medium, com.tokopedia.resources.common.R.anim.slide_left_out_medium)
        when (requestCode) {
            BuyerOrderDetailConst.REQUEST_CODE_REQUEST_CANCEL_ORDER -> handleRequestCancelResult(resultCode, data)
            BuyerOrderDetailConst.REQUEST_CODE_CREATE_RESOLUTION -> handleComplaintResult()
        }
    }

    override fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        when (button.key) {
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_ASK_SELLER -> onAskSellerActionButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_REQUEST_CANCEL -> onRequestCancelActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_TRACK_SHIPMENT -> onTrackShipmentActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_COMPLAINT -> onComplaintActionButtonClicked(button.url)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_VIEW_COMPLAINT -> onViewComplaintActionButtonClicked(button.url)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_FINISH_ORDER, BuyerOrderDetailConst.ACTION_BUTTON_KEY_RECEIVE_CONFIRMATION -> onReceiveConfirmationActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_HELP -> onHelpActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_BUY_AGAIN -> onBuyAgainAllProductButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_GIVE_REVIEW -> onGiveReviewActionButtonClicked(button.url)
        }
    }

    override fun onPopUpActionButtonClicked(button: ActionButtonsUiModel.ActionButton.PopUp.PopUpButton) {
        when (button.key) {
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_FINISH_ORDER -> onDoReceiveConfirmationActionButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_COMPLAINT -> onComplaintActionButtonClicked(button.uri)
        }
    }

    private fun onSecondaryActionButtonClicked() {
        val secondaryActionButtons = viewModel.getSecondaryActionButtons()
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }

    override fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel) {
        val productCopy = product.copy(isProcessing = true)
        adapter.updateItem(product, productCopy)
        viewModel.addSingleToCart(productCopy)
    }

    private fun loadInitialData() {
        showLoadIndicator()
        loadBuyerOrderDetail()
    }

    private fun onAskSellerActionButtonClicked() {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                navigator.goToAskSeller(it.data)
            }
        }
    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator.goToRequestCancellationPage(this, viewModel.buyerOrderDetailResult.value, button, cacheManager)
    }

    private fun onTrackShipmentActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                navigator.goToTrackShipmentPage(
                        it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                        button.url)
            }
        }
    }

    private fun onComplaintActionButtonClicked(complaintUrl: String) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                navigator.goToCreateResolution(this, complaintUrl)
            }
        }
        bottomSheetReceiveConfirmation?.finishLoading()
    }

    private fun onViewComplaintActionButtonClicked(url: String) {
        navigator.openAppLink(url)
    }

    private fun onReceiveConfirmationActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        showReceiveConfirmationBottomSheet(button)
    }

    private fun onDoReceiveConfirmationActionButtonClicked() {
        viewModel.finishOrder()
    }

    private fun onHelpActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        navigator.openWebView(button.url)
    }

    private fun onBuyAgainAllProductButtonClicked() {
        btnBuyerOrderDetailPrimaryActions?.isLoading = true
        viewModel.addMultipleToCart()
    }

    private fun onGiveReviewActionButtonClicked(url: String) {
        navigator.openAppLink(url)
    }

    private fun setupViews() {
        containerBuyerOrderDetail.layoutTransition.apply {
            setInterpolator(LayoutTransition.CHANGING, AccelerateInterpolator())
            enableTransitionType(LayoutTransition.CHANGING)
            setDuration(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DURATION)
            setStartDelay(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DELAY)
        }
        containerActionButtons?.actionButtonWrapper?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        setupToolbar()
        setupGlobalError()
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
    }

    private fun setupGlobalError() {
        globalErrorBuyerOrderDetail.setActionClickListener {
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
        rvBuyerOrderDetail.adapter = adapter
    }

    private fun loadBuyerOrderDetail() {
        val orderId = arguments?.getString(BuyerOrderDetailConst.PARAM_ORDER_ID, "").orEmpty()
        val paymentId = arguments?.getString(BuyerOrderDetailConst.PARAM_PAYMENT_ID, "").orEmpty()
        val cart = arguments?.getString(BuyerOrderDetailConst.PARAM_CART_STRING, "").orEmpty()
        viewModel.getBuyerOrderDetail(orderId, paymentId, cart)
    }

    private fun observeBuyerOrderDetail() {
        viewModel.buyerOrderDetailResult.observe(viewLifecycleOwner, Observer { result ->
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
        viewModel.finishOrderResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onSuccessReceiveConfirmation(result.data)
                is Fail -> onFailedReceiveConfirmation(result.throwable)
            }
        })
    }

    private fun observeAddSingleToCart() {
        viewModel.singleAtcResult.observe(viewLifecycleOwner, Observer { result ->
            when (val requestResult = result.second) {
                is Success -> onSuccessAddToCart(requestResult.data)
                is Fail -> onFailedAddToCart(requestResult.throwable)
            }
            adapter.updateItem(result.first, result.first.copy(isProcessing = false))
        })
    }

    private fun observeAddMultipleToCart() {
        viewModel.multiAtcResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onSuccessAddToCart(result.data)
                is Fail -> onFailedAddToCart(result.throwable)
            }
            btnBuyerOrderDetailPrimaryActions?.isLoading = false
        })
    }

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        setupActionButtons(data.actionButtonsUiModel)
        adapter.updateItems(data)
        contentVisibilityAnimator.showContent()
        stopLoadTimeMonitoring()
    }

    private fun onSuccessReceiveConfirmation(data: FinishOrderResponse.Data.FinishOrderBuyer) {
        bottomSheetReceiveConfirmation?.finishLoading()
        bottomSheetReceiveConfirmation?.dismiss()
        secondaryActionButtonBottomSheet.dismiss()
        showCommonToaster(data.message.firstOrNull().orEmpty())
        swipeRefreshBuyerOrderDetail.isRefreshing = true
        loadBuyerOrderDetail()
    }

    private fun onFailedReceiveConfirmation(throwable: Throwable) {
        bottomSheetReceiveConfirmation?.finishLoading()
        throwable.showErrorToaster()
    }

    private fun onSuccessAddToCart(data: AtcMultiData) {
        val msg = StringUtils.convertListToStringDelimiter(data.atcMulti.buyAgainData.message, ",")
        if (data.atcMulti.buyAgainData.success == 1) {
            showCommonToaster(msg, getString(R.string.label_see)) {
                navigator.openAppLink(ApplinkConst.CART)
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

    private fun setupActionButtons(actionButtonsUiModel: ActionButtonsUiModel) {
        if (actionButtonsUiModel.primaryActionButton.key.isNotBlank()) {
            setupPrimaryButton(actionButtonsUiModel.primaryActionButton, actionButtonsUiModel.secondaryActionButtons.size)
            setupSecondaryButton(actionButtonsUiModel.secondaryActionButtons)
            actionButtonAnimator.showActionButtons()
        } else {
            actionButtonAnimator.hideActionButtons()
        }
    }

    private fun setupPrimaryButton(primaryActionButton: ActionButtonsUiModel.ActionButton, secondaryActionButtonCount: Int) {
        btnBuyerOrderDetailPrimaryActions?.apply {
            val layoutParamsCopy = layoutParams as ViewGroup.MarginLayoutParams
            layoutParamsCopy.marginStart = if (secondaryActionButtonCount == 0) 0 else getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            layoutParams = layoutParamsCopy
            text = primaryActionButton.label
            buttonVariant = Utils.mapButtonVariant(primaryActionButton.variant)
            buttonType = Utils.mapButtonType(primaryActionButton.type)
            setOnClickListener(primaryActionButtonClickListener)
            show()
        }
    }

    private fun setupSecondaryButton(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>) {
        if (secondaryActionButtons.isNotEmpty()) {
            btnBuyerOrderDetailSecondaryActions?.apply {
                background = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                    setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
                }
                setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
                setOnClickListener(secondaryActionButtonClickListener)
                show()
            }
        } else {
            btnBuyerOrderDetailSecondaryActions?.gone()
        }
    }

    private fun createPrimaryActionButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            viewModel.buyerOrderDetailResult.value?.let {
                if (it is Success) {
                    onActionButtonClicked(it.data.actionButtonsUiModel.primaryActionButton)
                }
            }
        }
    }

    private fun createSecondaryActionButtonClickListener(): View.OnClickListener {
        return View.OnClickListener {
            onSecondaryActionButtonClicked()
        }
    }

    private fun showLoadIndicator() {
        loaderBuyerOrderDetail.show()
    }

    private fun hideLoadIndicator() {
        loaderBuyerOrderDetail.gone()
    }

    private fun showReceiveConfirmationBottomSheet(button: ActionButtonsUiModel.ActionButton) {
        val bottomSheetReceiveConfirmation = bottomSheetReceiveConfirmation?.apply {
            reInit(button)
        } ?: createReceiveConfirmationBottomSheet(button)
        this.bottomSheetReceiveConfirmation = bottomSheetReceiveConfirmation
        bottomSheetReceiveConfirmation.show(childFragmentManager)
    }

    private fun createReceiveConfirmationBottomSheet(button: ActionButtonsUiModel.ActionButton): ReceiveConfirmationBottomSheet {
        return ReceiveConfirmationBottomSheet(requireContext(), button, this)
    }

    private fun showCommonToaster(message: String, actionText: String = "", onActionClicked: () -> Unit = {}) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, actionText, View.OnClickListener {
                    onActionClicked()
                }).show()
            }
        }
    }

    private fun showErrorToaster(message: String, actionText: String = "", onActionClicked: () -> Unit = {}) {
        if (message.isNotBlank()) {
            view?.let { view ->
                Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, actionText, View.OnClickListener {
                    onActionClicked()
                }).show()
            }
        }
    }

    private fun Throwable.showErrorToaster() {
        val errorMessage = context?.let {
            ErrorHandler.getErrorMessage(it, this)
        } ?: this@BuyerOrderDetailFragment.context?.getString(R.string.failed_to_get_information).orEmpty()
        showErrorToaster(errorMessage)
    }

    private fun dismissBottomSheets() {
        secondaryActionButtonBottomSheet.dismiss()
        bottomSheetReceiveConfirmation?.dismiss()
    }

    private fun handleRequestCancelResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_CODE_INSTANT_CANCEL_BUYER_REQUEST) {
            val resultMessage = data?.getStringExtra(BuyerOrderDetailConst.RESULT_MSG_INSTANT_CANCEL).orEmpty()
            val result = data?.getIntExtra(BuyerOrderDetailConst.RESULT_CODE_INSTANT_CANCEL, 1) ?: 1
            if (result == 1) {
                if (resultMessage.isNotBlank()) {
                    showCommonToaster(resultMessage)
                }
            } else if (result == 3) {
                val popupTitle = data?.getStringExtra(BuyerOrderDetailConst.RESULT_POPUP_TITLE_INSTANT_CANCEL).orEmpty()
                val popupBody = data?.getStringExtra(BuyerOrderDetailConst.RESULT_POPUP_BODY_INSTANT_CANCEL).orEmpty()
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
        dismissBottomSheets()
    }

    private fun handleComplaintResult() {
        loadBuyerOrderDetail()
    }

    private fun stopLoadTimeMonitoring() {
        rvBuyerOrderDetail?.post {
            buyerOrderDetailLoadMonitoring?.stopRenderPerformanceMonitoring()
        }
    }
}