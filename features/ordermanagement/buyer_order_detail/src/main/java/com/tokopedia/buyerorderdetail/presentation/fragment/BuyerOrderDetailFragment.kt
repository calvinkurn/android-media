package com.tokopedia.buyerorderdetail.presentation.fragment

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
import javax.inject.Inject

class BuyerOrderDetailFragment : BaseDaggerFragment(), ActionButtonClickListener, ProductViewHolder.ProductViewListener {

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragment {
            return BuyerOrderDetailFragment().apply {
                arguments = extras
            }
        }

        const val REQUEST_CANCEL_ORDER = 101
    }

    @Inject
    lateinit var viewModel: BuyerOrderDetailViewModel

    private val cacheManager: SaveInstanceCacheManager by lazy {
        SaveInstanceCacheManager(requireContext())
    }
    private val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactory(this, this)
    }
    private val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapter(typeFactory)
    }
    private val secondaryActionButtonBottomSheet: SecondaryActionButtonBottomSheet by lazy {
        SecondaryActionButtonBottomSheet(requireContext(), this)
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

    override fun onActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        when (button.key) {
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_ASK_SELLER -> onAskSellerActionButtonClicked()
            BuyerOrderDetailConst.ACTION_BUTTON_KEY_REQUEST_CANCEL -> onRequestCancelActionButtonClicked(button)
        }
    }

    override fun onSecondaryActionButtonClicked() {
        val secondaryActionButtons = viewModel.getSecondaryActionButtons()
        secondaryActionButtonBottomSheet.setSecondaryActionButtons(secondaryActionButtons)
        secondaryActionButtonBottomSheet.show(childFragmentManager)
    }

    private fun onAskSellerActionButtonClicked() {

    }

    private fun onRequestCancelActionButtonClicked(button: ActionButtonsUiModel.ActionButton) {
        BuyerOrderDetailNavigator.goToRequestCancellationPage(this, viewModel.buyerOrderDetailResult.value, button, cacheManager)
    }

    override fun onBuyAgainButtonClicked() {
        //TODO: Implement ATC after backend provide contract
    }

    private fun setupViews() {
        containerBuyerOrderDetail.layoutTransition.apply {
            enableTransitionType(LayoutTransition.CHANGING)
            setDuration(LayoutTransition.CHANGING, 300L)
            setStartDelay(LayoutTransition.CHANGING, 45L)
        }
        setupToolbar()
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshBuyerOrderDetail?.isEnabled = false
        swipeRefreshBuyerOrderDetail?.setOnRefreshListener {
            loadBuyerOrderDetail()
        }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolbarBuyerOrderDetail)
        }
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
            rvBuyerOrderDetail.show()
            when (result) {
                is Success -> onSuccessGetBuyerOrderDetail(result.data)
                is Fail -> onFailedGetBuyerOrderDetail(result.throwable)
            }
            swipeRefreshBuyerOrderDetail?.isRefreshing = false
            swipeRefreshBuyerOrderDetail?.isEnabled = true
        })
    }

    private fun showLoadIndicator() {
        loaderBuyerOrderDetail.apply {
            scaleX = 1f
            scaleY = 1f
        }
    }

    private fun hideLoadIndicator() {
        loaderBuyerOrderDetail.apply {
            scaleX = 0f
            scaleY = 0f
        }
    }

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        val newItems = mutableListOf<Visitable<BuyerOrderDetailTypeFactory>>().apply {
            setupOrderStatusSection(data.orderStatusUiModel)
            setupProductListSection(data.productListUiModel)
            setupShipmentInfoSection(data.shipmentInfoUiModel)
            setupPaymentInfoSection(data.paymentInfoUiModel)
            setupActionButtonsSection(data.actionButtonsUiModel)
        }
        adapter.updateItems(newItems)
    }

    private fun onFailedGetBuyerOrderDetail(throwable: Throwable) {
        view?.let { view ->
            Toaster.build(view, throwable.message.orEmpty(), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
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

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupActionButtonsSection(actionButtonsUiModel: ActionButtonsUiModel) {
        addActionButtonsSection(actionButtonsUiModel)
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

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addActionButtonsSection(actionButtonsUiModel: ActionButtonsUiModel) {
        add(actionButtonsUiModel)
    }
}