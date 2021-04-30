package com.tokopedia.buyerorderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.BuyProtectionViewHolder
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.ProductViewHolder
import com.tokopedia.buyerorderdetail.presentation.bottomsheet.SecondaryActionButtonBottomSheet
import com.tokopedia.buyerorderdetail.presentation.model.*
import com.tokopedia.buyerorderdetail.presentation.viewmodel.BuyerOrderDetailViewModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_buyer_order_detail.*
import javax.inject.Inject

class BuyerOrderDetailFragment : BaseDaggerFragment(), ActionButtonClickListener, BuyProtectionViewHolder.BuyProtectionListener, ProductViewHolder.ProductViewListener {

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
        BuyerOrderDetailTypeFactory(this, this, this)
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

    override fun onClickBuyProtection() {
        view?.let {
            Toaster.build(it, "Beli proteksi dong...", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onReachBuyProtectionDeadline() {
        view?.let {
            Toaster.build(it, "Beli proteksi dong...", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    override fun onBuyAgainButtonClicked() {
        //TODO: Implement ATC after backend provide contract
    }

    private fun setupViews() {
        setupToolbar()
        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    private fun setupSwipeRefreshLayout() {
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
        loadBuyerOrderDetail()
        viewModel.buyerOrderDetailResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> onSuccessGetBuyerOrderDetail(result.data)
                is Fail -> onFailedGetBuyerOrderDetail(result.throwable)
            }
            swipeRefreshBuyerOrderDetail?.isRefreshing = false
        })
    }

    private fun onSuccessGetBuyerOrderDetail(data: BuyerOrderDetailUiModel) {
        adapter.clearAllElements()
        setupOrderStatusSection(data.orderStatusUiModel)
        setupProductListSection(data.productListUiModel)
        setupBuyProtectionSection(data.buyProtectionUiModel)
        setupShipmentInfoSection(data.shipmentInfoUiModel)
        setupPaymentInfoSection(data.paymentInfoUiModel)
        setupActionButtonsSection(data.actionButtonsUiModel)
    }

    private fun onFailedGetBuyerOrderDetail(throwable: Throwable) {
        view?.let { view ->
            Toaster.build(view, throwable.message.orEmpty(), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun setupOrderStatusSection(orderStatusUiModel: OrderStatusUiModel) {
        addOrderStatusHeaderSection(orderStatusUiModel.orderStatusHeaderUiModel)
        addTickerSection(orderStatusUiModel.ticker)
        addThinDividerSection()
        addOrderStatusInfoSection(orderStatusUiModel.orderStatusInfoUiModel)
    }

    private fun setupProductListSection(productListUiModel: ProductListUiModel) {
        addThickDividerSection()
        addProductListHeaderSection(productListUiModel.productListHeaderUiModel)
        addProductListSection(productListUiModel.productList)
    }

    private fun setupBuyProtectionSection(buyProtectionUiModel: BuyProtectionUiModel) {
        if (buyProtectionUiModel.showBuyProtection) {
            addThickDividerSection()
            addBuyProtectionSection(buyProtectionUiModel)
        }
    }

    private fun setupShipmentInfoSection(shipmentInfoUiModel: ShipmentInfoUiModel) {
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

    private fun setupPaymentInfoSection(paymentInfoUiModel: PaymentInfoUiModel) {
        addThickDividerSection()
        addPlainHeaderSection(paymentInfoUiModel.headerUiModel)
        addPaymentMethodSection(paymentInfoUiModel.paymentMethodInfoItem)
        addThinDividerSection()
        addPaymentInfoSection(paymentInfoUiModel.paymentInfoItems)
        addThinDividerSection()
        addPaymentGrandTotalSection(paymentInfoUiModel.paymentGrandTotal)
        addTickerSection(paymentInfoUiModel.ticker)
    }

    private fun setupActionButtonsSection(actionButtonsUiModel: ActionButtonsUiModel) {
        addActionButtonsSection(actionButtonsUiModel)
    }

    private fun addPlainHeaderSection(headerUiModel: PlainHeaderUiModel) {
        adapter.addItem(headerUiModel)
    }

    private fun addTickerSection(tickerUiModel: TickerUiModel) {
        if (tickerUiModel.description.isNotBlank()) {
            adapter.addItem(tickerUiModel)
        }
    }

    private fun addThinDashedDividerSection() {
        adapter.addItem(ThinDashedDividerUiModel())
    }

    private fun addThickDividerSection() {
        adapter.addItem(ThickDividerUiModel())
    }

    private fun addThinDividerSection() {
        adapter.addItem(ThinDividerUiModel())
    }

    private fun addOrderStatusHeaderSection(orderStatusHeaderUiModel: OrderStatusUiModel.OrderStatusHeaderUiModel) {
        adapter.addItem(orderStatusHeaderUiModel)
    }

    private fun addOrderStatusInfoSection(orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel) {
        adapter.addItem(orderStatusInfoUiModel)
    }

    private fun addProductListHeaderSection(productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel) {
        adapter.addItem(productListHeaderUiModel)
    }

    private fun addProductListSection(productList: List<ProductListUiModel.ProductUiModel>) {
        adapter.addItems(productList)
    }

    private fun addCourierInfoSection(courierInfoUiModel: ShipmentInfoUiModel.CourierInfoUiModel) {
        adapter.addItem(courierInfoUiModel)
    }

    private fun addCourierDriverInfoSection(courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel) {
        adapter.addItem(courierDriverInfoUiModel)
    }

    private fun addAwbInfoSection(awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel) {
        adapter.addItem(awbInfoUiModel)
    }

    private fun addReceiverAddressInfoSection(receiverAddressInfoUiModel: ShipmentInfoUiModel.ReceiverAddressInfoUiModel) {
        adapter.addItem(receiverAddressInfoUiModel)
    }

    private fun addBuyProtectionSection(buyProtectionUiModel: BuyProtectionUiModel) {
        adapter.addItem(buyProtectionUiModel)
    }

    private fun addPaymentMethodSection(paymentMethodInfoItem: PaymentInfoUiModel.PaymentInfoItemUiModel) {
        adapter.addItem(paymentMethodInfoItem)
    }

    private fun addPaymentInfoSection(paymentInfoItems: List<PaymentInfoUiModel.PaymentInfoItemUiModel>) {
        adapter.addItems(paymentInfoItems)
    }

    private fun addPaymentGrandTotalSection(paymentGrandTotal: PaymentInfoUiModel.PaymentGrandTotalUiModel) {
        adapter.addItem(paymentGrandTotal)
    }

    private fun addActionButtonsSection(actionButtonsUiModel: ActionButtonsUiModel) {
        adapter.addItem(actionButtonsUiModel)
    }
}