package com.tokopedia.buyerorderdetail.presentation.fragment

import android.animation.LayoutTransition
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
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailActionButtonAnimator
import com.tokopedia.buyerorderdetail.presentation.animator.BuyerOrderDetailContentAnimator
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.ReceiveConfirmationBottomSheet
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
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

        const val REQUEST_CODE_REQUEST_CANCEL_ORDER = 101
        const val REQUEST_CODE_CREATE_RESOLUTION = 102

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
        BuyerOrderDetailTypeFactory(this)
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
    }

    override fun onBuyAgainButtonClicked() {
        //TODO: Implement ATC after backend provide contract
    }

    override fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        when (button.key) {
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_ASK_SELLER -> onAskSellerActionButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_REQUEST_CANCEL -> onRequestCancelActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_TRACK_SHIPMENT -> onTrackShipmentActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_COMPLAINT -> onComplaintActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_RECEIVE_CONFIRMATION -> onReceiveConfirmationActionButtonClicked(button)
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_DO_RECEIVE_CONFIRMATION -> onDoReceiveConfirmationActionButtonClicked(button)
        }
    }

    private fun onSecondaryActionButtonClicked() {
        val secondaryActionButtons = viewModel.getSecondaryActionButtons()
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }

    private fun onAskSellerActionButtonClicked() {
        context?.let { context ->
            viewModel.buyerOrderDetailResult.value?.let {
                if (it is Success) {
                    BuyerOrderDetailNavigator.goToAskSeller(context, it.data)
                }
            }
        }
    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        BuyerOrderDetailNavigator.goToRequestCancellationPage(this, viewModel.buyerOrderDetailResult.value, button, cacheManager)
    }

    private fun onTrackShipmentActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        context?.let { context ->
            viewModel.buyerOrderDetailResult.value?.let {
                if (it is Success) {
                    BuyerOrderDetailNavigator.goToTrackShipmentPage(
                            context,
                            it.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId,
                            button.url)
                }
            }
        }
    }

    private fun onComplaintActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        viewModel.buyerOrderDetailResult.value?.let {
            if (it is Success) {
                BuyerOrderDetailNavigator.goToCreateResolution(this, button.url)
            }
        }
    }

    private fun onReceiveConfirmationActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        showReceiveConfirmationBottomSheet(button)
    }

    private fun onDoReceiveConfirmationActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        viewModel.finishOrder()
    }

    private fun setupViews() {
        containerBuyerOrderDetail.layoutTransition.apply {
            setInterpolator(LayoutTransition.CHANGING, AccelerateInterpolator())
            enableTransitionType(LayoutTransition.CHANGING)
            setDuration(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DURATION)
            setStartDelay(LayoutTransition.CHANGING, CONTENT_CHANGING_ANIMATION_DELAY)
        }
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
            globalErrorBuyerOrderDetail.gone()
            showLoadIndicator()
            contentVisibilityAnimator.hideContent()
            loadBuyerOrderDetail()
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
        showLoadIndicator()
        loadBuyerOrderDetail()
        viewModel.buyerOrderDetailResult.observe(viewLifecycleOwner, Observer { result ->
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

    private fun onSuccessReceiveConfirmation(data: FinishOrderResponse.Data.FinishOrderBuyer) {
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

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        setupActionButtons(data.actionButtonsUiModel)
        adapter.updateItems(data)
        contentVisibilityAnimator.showContent()
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
            setupPrimaryButton(actionButtonsUiModel.primaryActionButton)
            setupSecondaryButton()
            actionButtonAnimator.showActionButtons()
        } else {
            actionButtonAnimator.hideActionButtons()
        }
    }

    private fun setupPrimaryButton(primaryActionButton: ActionButtonsUiModel.ActionButton) {
        btnBuyerOrderDetailPrimaryActions.text = primaryActionButton.label
        btnBuyerOrderDetailPrimaryActions.setOnClickListener(primaryActionButtonClickListener)
    }

    private fun setupSecondaryButton() {
        btnBuyerOrderDetailSecondaryActions?.apply {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(ContextCompat.getColor(context, android.R.color.transparent))
                cornerRadius = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.button_corner_radius)
                setStroke(resources.getDimensionPixelSize(com.tokopedia.unifycomponents.R.dimen.button_stroke_width), ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.buttonunify_alternate_stroke_color))
            }
            setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N200))
            setOnClickListener(secondaryActionButtonClickListener)
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
}