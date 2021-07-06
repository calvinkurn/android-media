package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.BuyerOrderDetailDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.model.*

@Suppress("UNCHECKED_CAST")
class BuyerOrderDetailAdapter(private val typeFactory: BuyerOrderDetailTypeFactory) : BaseAdapter<BuyerOrderDetailTypeFactory>(typeFactory) {

    private fun setupNewItems(newData: BuyerOrderDetailUiModel): List<Visitable<BuyerOrderDetailTypeFactory>> {
        return mutableListOf<Visitable<BuyerOrderDetailTypeFactory>>().apply {
            setupOrderStatusSection(newData.orderStatusUiModel)
            setupProductListSection(newData.productListUiModel)
            setupShipmentInfoSection(newData.shipmentInfoUiModel)
            setupPaymentInfoSection(newData.paymentInfoUiModel)
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
        addCourierDriverInfoSection(shipmentInfoUiModel.courierDriverInfoUiModel)
        addAwbInfoSection(shipmentInfoUiModel.awbInfoUiModel)
        addReceiverAddressInfoSection(shipmentInfoUiModel.receiverAddressInfoUiModel)
        addDropShipperInfoSection(shipmentInfoUiModel.dropShipperInfoUiModel)
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
        if (headerUiModel.shouldShow()) add(headerUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addTickerSection(tickerUiModel: TickerUiModel) {
        if (tickerUiModel.shouldShow()) add(tickerUiModel)
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
        if (orderStatusHeaderUiModel.shouldShow()) add(orderStatusHeaderUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addOrderStatusInfoSection(orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel) {
        if (orderStatusInfoUiModel.shouldShow()) add(orderStatusInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListHeaderSection(productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel) {
        if (productListHeaderUiModel.shouldShow()) add(productListHeaderUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addProductListSection(productList: List<ProductListUiModel.ProductUiModel>) {
        productList.filter { it.shouldShow() }.also { addAll(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierInfoSection(courierInfoUiModel: ShipmentInfoUiModel.CourierInfoUiModel) {
        if (courierInfoUiModel.shouldShow()) add(courierInfoUiModel)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addCourierDriverInfoSection(courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel) {
        if (courierDriverInfoUiModel.shouldShow()) {
            addThinDashedDividerSection()
            add(courierDriverInfoUiModel)
            addThinDashedDividerSection()
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addAwbInfoSection(awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel) {
        if (awbInfoUiModel.shouldShow()) {
            add(awbInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addReceiverAddressInfoSection(receiverAddressInfoUiModel: CopyableKeyValueUiModel) {
        if (receiverAddressInfoUiModel.shouldShow()) {
            add(receiverAddressInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addDropShipperInfoSection(dropShipperInfoUiModel: CopyableKeyValueUiModel) {
        if (dropShipperInfoUiModel.shouldShow()) {
            add(dropShipperInfoUiModel)
        }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentMethodSection(paymentMethodInfoItem: PaymentInfoUiModel.PaymentInfoItemUiModel) {
        if (paymentMethodInfoItem.shouldShow()) add(paymentMethodInfoItem)
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentInfoSection(paymentInfoItems: List<PaymentInfoUiModel.PaymentInfoItemUiModel>) {
        paymentInfoItems.filter { it.shouldShow() }.also { addAll(it) }
    }

    private fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.addPaymentGrandTotalSection(paymentGrandTotal: PaymentInfoUiModel.PaymentGrandTotalUiModel) {
        if (paymentGrandTotal.shouldShow()) add(paymentGrandTotal)
    }

    fun updateItems(newData: BuyerOrderDetailUiModel) {
        val newItems = setupNewItems(newData)
        val diffCallback = BuyerOrderDetailDiffUtilCallback(visitables as List<Visitable<BuyerOrderDetailTypeFactory>>, newItems, typeFactory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newItems)
    }

    fun updateItem(oldItem: Visitable<BuyerOrderDetailTypeFactory>, newItem: Visitable<BuyerOrderDetailTypeFactory>) {
        val index = visitables.indexOf(oldItem)
        if (index != -1) {
            visitables[index] = newItem
            notifyItemChanged(index, oldItem to newItem)
        }
    }
}