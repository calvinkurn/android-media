package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.*
import com.tokopedia.buyerorderdetail.presentation.model.*

class BuyerOrderDetailTypeFactory(
        private val productViewListener: ProductViewHolder.ProductViewListener,
        private val navigator: BuyerOrderDetailNavigator
) : BaseAdapterTypeFactory() {

    fun type(awbInfoUiModel: ShipmentInfoUiModel.AwbInfoUiModel): Int {
        return AwbInfoViewHolder.LAYOUT
    }

    fun type(courierDriverInfoUiModel: ShipmentInfoUiModel.CourierDriverInfoUiModel): Int {
        return CourierDriverInfoViewHolder.LAYOUT
    }

    fun type(courierUiModel: ShipmentInfoUiModel.CourierInfoUiModel): Int {
        return CourierInfoViewHolder.LAYOUT
    }

    fun type(orderStatusHeaderUiModel: OrderStatusUiModel.OrderStatusHeaderUiModel): Int {
        return OrderStatusHeaderViewHolder.LAYOUT
    }

    fun type(orderStatusInfoUiModel: OrderStatusUiModel.OrderStatusInfoUiModel): Int {
        return OrderStatusInfoViewHolder.LAYOUT
    }

    fun type(paymentGrandTotalUiModel: PaymentInfoUiModel.PaymentGrandTotalUiModel): Int {
        return PaymentGrandTotalViewHolder.LAYOUT
    }

    fun type(paymentInfoUiModel: PaymentInfoUiModel.PaymentInfoItemUiModel): Int {
        return PaymentInfoItemViewHolder.LAYOUT
    }

    fun type(plainHeaderUiModel: PlainHeaderUiModel): Int {
        return PlainHeaderViewHolder.LAYOUT
    }

    fun type(productListHeaderUiModel: ProductListUiModel.ProductListHeaderUiModel): Int {
        return ProductListHeaderViewHolder.LAYOUT
    }

    fun type(productUiModel: ProductListUiModel.ProductUiModel): Int {
        return ProductViewHolder.LAYOUT
    }

    fun type(receiverAddressInfoUiModel: ShipmentInfoUiModel.ReceiverAddressInfoUiModel): Int {
        return ReceiverAddressInfoViewHolder.LAYOUT
    }

    fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        return ThickDividerViewHolder.LAYOUT
    }

    fun type(thinDashedDividerUiModel: ThinDashedDividerUiModel): Int {
        return ThinDashedDividerViewHolder.LAYOUT
    }

    fun type(thinDividerUiModel: ThinDividerUiModel): Int {
        return ThinDividerViewHolder.LAYOUT
    }

    fun type(tickerUiModel: TickerUiModel): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            AwbInfoViewHolder.LAYOUT -> AwbInfoViewHolder(parent)
            CourierDriverInfoViewHolder.LAYOUT -> CourierDriverInfoViewHolder(parent, navigator)
            CourierInfoViewHolder.LAYOUT -> CourierInfoViewHolder(parent)
            OrderStatusHeaderViewHolder.LAYOUT -> OrderStatusHeaderViewHolder(parent, navigator)
            OrderStatusInfoViewHolder.LAYOUT -> OrderStatusInfoViewHolder(parent, navigator)
            PaymentGrandTotalViewHolder.LAYOUT -> PaymentGrandTotalViewHolder(parent)
            PaymentInfoItemViewHolder.LAYOUT -> PaymentInfoItemViewHolder(parent)
            PlainHeaderViewHolder.LAYOUT -> PlainHeaderViewHolder(parent)
            ProductListHeaderViewHolder.LAYOUT -> ProductListHeaderViewHolder(parent, navigator)
            ProductViewHolder.LAYOUT -> ProductViewHolder(parent, productViewListener, navigator)
            ReceiverAddressInfoViewHolder.LAYOUT -> ReceiverAddressInfoViewHolder(parent)
            ThickDividerViewHolder.LAYOUT -> ThickDividerViewHolder(parent)
            ThinDashedDividerViewHolder.LAYOUT -> ThinDashedDividerViewHolder(parent)
            ThinDividerViewHolder.LAYOUT -> ThinDividerViewHolder(parent)
            TickerViewHolder.LAYOUT -> TickerViewHolder(parent, navigator)
            else -> super.createViewHolder(parent, type)
        }
    }
}