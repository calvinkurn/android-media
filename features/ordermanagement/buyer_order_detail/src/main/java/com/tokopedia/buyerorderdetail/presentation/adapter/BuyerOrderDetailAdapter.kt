package com.tokopedia.buyerorderdetail.presentation.adapter

import android.os.Bundle
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
        if (awbInfoUiModel.awbNumber.isNotBlank()) {
            add(awbInfoUiModel)
        }
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