package com.tokopedia.buyerorderdetail.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.*
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

        const val REQUEST_CANCEL_ORDER = 101

        private const val CONTENT_CHANGING_ANIMATION_DURATION = 300L
        private const val CONTENT_CHANGING_ANIMATION_DELAY = 45L
        private const val SHOW_HIDE_CONTENT_ANIMATION_DURATION = 300L
        private const val FADE_ANIMATION_DELAY = 60L
        private const val TRANSLATION_ANIMATION_DELAY = 45L
    }

    @Inject
    lateinit var viewModel: BuyerOrderDetailViewModel

    private var animatorShowContent: AnimatorSet? = null
    private var animatorHideContent: AnimatorSet? = null

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
    }

    override fun onBuyAgainButtonClicked() {
        //TODO: Implement ATC after backend provide contract
    }

    override fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        when (button.key) {
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_ASK_SELLER -> onAskSellerActionButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_REQUEST_CANCEL -> onRequestCancelActionButtonClicked(button)
        }
    }

    private fun onSecondaryActionButtonClicked() {
        val secondaryActionButtons = viewModel.getSecondaryActionButtons()
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }

    private fun onAskSellerActionButtonClicked() {

    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        BuyerOrderDetailNavigator.goToRequestCancellationPage(this, viewModel.buyerOrderDetailResult.value, button, cacheManager)
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
            hideContent()
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

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupOrderStatusSection(orderStatusUiModel: OrderStatusUiModel) {
        addOrderStatusHeaderSection(orderStatusUiModel.orderStatusHeaderUiModel)
        addTickerSection(orderStatusUiModel.ticker)
        addThinDividerSection()
        addOrderStatusInfoSection(orderStatusUiModel.orderStatusInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupProductListSection(productListUiModel: ProductListUiModel) {
        addThickDividerSection()
        addProductListHeaderSection(productListUiModel.productListHeaderUiModel)
        addProductListSection(productListUiModel.productList)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupShipmentInfoSection(shipmentInfoUiModel: ShipmentInfoUiModel) {
        addThickDividerSection()
        addPlainHeaderSection(shipmentInfoUiModel.headerUiModel)
        addTickerSection(shipmentInfoUiModel.ticker)
        addCourierInfoSection(shipmentInfoUiModel.courierInfoUiModel)
        if (shipmentInfoUiModel.courierDriverInfoUiModel.name.isNotBlank()) {
            addThinDashedDividerSection()
            addCourierDriverInfoSection(shipmentInfoUiModel.courierDriverInfoUiModel)
            addThinDashedDividerSection()
        }
        addAwbInfoSection(shipmentInfoUiModel.awbInfoUiModel)
        addReceiverAddressInfoSection(shipmentInfoUiModel.receiverAddressInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupPaymentInfoSection(paymentInfoUiModel: PaymentInfoUiModel) {
        addThickDividerSection()
        addPlainHeaderSection(paymentInfoUiModel.headerUiModel)
        addPaymentMethodSection(paymentInfoUiModel.paymentMethodInfoItem)
        addThinDividerSection()
        addPaymentInfoSection(paymentInfoUiModel.paymentInfoItems)
        addThinDividerSection()
        addPaymentGrandTotalSection(paymentInfoUiModel.paymentGrandTotal)
        addTickerSection(paymentInfoUiModel.ticker)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPlainHeaderSection(headerUiModel: PlainHeaderUiModel) {
        add(headerUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addTickerSection(tickerUiModel: TickerUiModel) {
        if (tickerUiModel.description.isNotBlank()) {
            add(tickerUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThinDashedDividerSection() {
        add(ThinDashedDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThickDividerSection() {
        add(ThickDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addThinDividerSection() {
        add(ThinDividerUiModel())
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderStatusHeaderSection(orderStatusHeaderUiModel: OrderStatusUiModel.OrderStatusHeaderUiModel) {
        add(orderStatusHeaderUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderStatusInfoSection(orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel) {
        add(orderStatusInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListHeaderSection(productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel) {
        add(productListHeaderUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListSection(productList: List<ProductListUiModel.ProductUiModel>) {
        addAll(productList)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierInfoSection(courierInfoUiModel: ShipmentInfoUiModel.CourierInfoUiModel) {
        add(courierInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierDriverInfoSection(courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel) {
        add(courierDriverInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addAwbInfoSection(awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel) {
        add(awbInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addReceiverAddressInfoSection(receiverAddressInfoUiModel: ShipmentInfoUiModel.ReceiverAddressInfoUiModel) {
        add(receiverAddressInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentMethodSection(paymentMethodInfoItem: PaymentInfoUiModel.PaymentInfoItemUiModel) {
        add(paymentMethodInfoItem)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentInfoSection(paymentInfoItems: List<PaymentInfoUiModel.PaymentInfoItemUiModel>) {
        addAll(paymentInfoItems)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentGrandTotalSection(paymentGrandTotal: PaymentInfoUiModel.PaymentGrandTotalUiModel) {
        add(paymentGrandTotal)
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

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        val newItems = mutableListOf<Visitable<BuyerOrderDetailTypeFactory>>().apply {
            setupOrderStatusSection(data.orderStatusUiModel)
            setupProductListSection(data.productListUiModel)
            setupShipmentInfoSection(data.shipmentInfoUiModel)
            setupPaymentInfoSection(data.paymentInfoUiModel)
        }
        setupActionButtons(data.actionButtonsUiModel)
        adapter.updateItems(newItems)
        showContent()
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
        hideContent()
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
            showActionButtons()
        } else {
            hideActionButtons()
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

    private fun showContent() {
        animatorShowContent = createShowContentAnimatorSet()
        animatorHideContent?.pause()
        animatorShowContent?.start()
    }

    private fun hideContent() {
        animatorHideContent = createHideContentAnimatorSet()
        animatorShowContent?.pause()
        animatorHideContent?.start()
    }

    private fun showActionButtons() {
        containerActionButtons.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerBuyerOrderDetail)
            constraintSet.connect(R.id.containerActionButtons, ConstraintSet.BOTTOM, R.id.containerBuyerOrderDetail, ConstraintSet.BOTTOM)
            constraintSet.clear(R.id.containerActionButtons, ConstraintSet.TOP)
            constraintSet.applyTo(containerBuyerOrderDetail)
        }
    }

    private fun hideActionButtons() {
        containerActionButtons.apply {
            val constraintSet = ConstraintSet()
            constraintSet.clone(containerBuyerOrderDetail)
            constraintSet.connect(R.id.containerActionButtons, ConstraintSet.TOP, R.id.containerBuyerOrderDetail, ConstraintSet.BOTTOM)
            constraintSet.clear(R.id.containerActionButtons, ConstraintSet.BOTTOM)
            constraintSet.applyTo(containerBuyerOrderDetail)
        }
    }

    private fun createShowContentAnimatorSet(): AnimatorSet {
        val showContentAnimator = createTranslationYAnimator(swipeRefreshBuyerOrderDetail, swipeRefreshBuyerOrderDetail.translationY, 0f)
        val fadeInAnimator = createFadeAnimator(rvBuyerOrderDetail, rvBuyerOrderDetail.alpha, 1f)
        return AnimatorSet().apply {
            playTogether(showContentAnimator, fadeInAnimator)
        }
    }

    private fun createHideContentAnimatorSet(): AnimatorSet {
        val hideContentAnimator = createTranslationYAnimator(swipeRefreshBuyerOrderDetail, swipeRefreshBuyerOrderDetail.translationY, swipeRefreshBuyerOrderDetail.measuredHeight.toFloat())
        val fadeOutAnimator = createFadeAnimator(rvBuyerOrderDetail, rvBuyerOrderDetail.alpha, 0f)
        return AnimatorSet().apply {
            playTogether(hideContentAnimator, fadeOutAnimator)
        }
    }

    private fun createFadeAnimator(target: View, from: Float, to: Float): Animator {
        return ObjectAnimator.ofFloat(target, View.ALPHA, from, to).apply {
            interpolator = DecelerateInterpolator(2f)
            duration = SHOW_HIDE_CONTENT_ANIMATION_DURATION
            startDelay = FADE_ANIMATION_DELAY
        }
    }

    private fun createTranslationYAnimator(target: View, from: Float, to: Float): Animator {
        return ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, from, to).apply {
            interpolator = DecelerateInterpolator()
            duration = SHOW_HIDE_CONTENT_ANIMATION_DURATION
            startDelay = TRANSLATION_ANIMATION_DELAY
        }
    }
}