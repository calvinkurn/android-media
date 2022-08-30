package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.CompletedStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentAmountUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentDetailUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentMethodUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.RestaurantUserAddressUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingDetailUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TemporaryFinishOrderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerMarginUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.TickerInfoData
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.CompletedStatusViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.DriverSectionViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.FoodItemViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.InvoiceOrderNumberViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderDetailHeaderViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderDetailToggleCtaViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingErrorViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingEstimationViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingLoadingViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingStatusInfoViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.OrderTrackingTickerViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.PaymentAmountViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.PaymentDetailViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.PaymentHeaderViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.PaymentMethodViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.RestaurantUserAddressViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.ShippingDetailHeaderViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.ShippingDetailViewHolderPayloadViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.TemporaryFinishOrderViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.ThickDividerViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.ThinDividerMarginViewHolder
import com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder.ThinDividerViewHolder

class OrderTrackingAdapterTypeFactoryImpl(
    private val recyclerViewPollerListener: RecyclerViewPollerListener,
    private val orderTrackingListener: OrderTrackingListener
) : BaseAdapterTypeFactory(), OrderTrackingTypeFactory {

    override fun type(viewModel: LoadingModel): Int {
        return OrderTrackingLoadingViewHolder.LAYOUT
    }

    override fun type(orderTrackingErrorUiModel: OrderTrackingErrorUiModel): Int {
        return OrderTrackingErrorViewHolder.LAYOUT
    }

    override fun type(tickerInfoUiModel: TickerInfoData): Int {
        return OrderTrackingTickerViewHolder.LAYOUT
    }

    override fun type(restaurantUserAddressUiModel: RestaurantUserAddressUiModel): Int {
        return RestaurantUserAddressViewHolder.LAYOUT
    }

    override fun type(shippingHeaderUiModel: ShippingHeaderUiModel): Int {
        return ShippingDetailHeaderViewHolder.LAYOUT
    }

    override fun type(orderDetailHeaderUiModel: OrderDetailHeaderUiModel): Int {
        return OrderDetailHeaderViewHolder.LAYOUT
    }

    override fun type(completedStatusInfoUiModel: CompletedStatusInfoUiModel): Int {
        return CompletedStatusViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(shippingDetailUiModel: ShippingDetailUiModel): Int {
        return ShippingDetailViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(orderDetailToggleCtaUiModel: OrderDetailToggleCtaUiModel): Int {
        return OrderDetailToggleCtaViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(driverSectionUiModel: DriverSectionUiModel): Int {
        return DriverSectionViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel): Int {
        return OrderTrackingStatusInfoViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(orderTrackingEstimationUiModel: OrderTrackingEstimationUiModel): Int {
        return OrderTrackingEstimationViewHolder.LAYOUT
    }

    override fun type(foodItemUiModel: FoodItemUiModel): Int {
        return FoodItemViewHolder.LAYOUT
    }

    override fun type(invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel): Int {
        return InvoiceOrderNumberViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(paymentInfoUiModel: PaymentDetailUiModel): Int {
        return PaymentDetailViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(paymentMethodUiModel: PaymentMethodUiModel): Int {
        return PaymentMethodViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(paymentAmountUiModel: PaymentAmountUiModel): Int {
        return PaymentAmountViewHolderPayloadViewHolder.LAYOUT
    }

    override fun type(paymentHeaderUiModel: PaymentHeaderUiModel): Int {
        return PaymentHeaderViewHolder.LAYOUT
    }

    override fun type(temporaryFinishOrderUiModel: TemporaryFinishOrderUiModel): Int {
        return TemporaryFinishOrderViewHolder.LAYOUT
    }

    override fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        return ThickDividerViewHolder.LAYOUT
    }

    override fun type(thinDividerUiModel: ThinDividerUiModel): Int {
        return ThinDividerViewHolder.LAYOUT
    }

    override fun type(thinDividerMarginUiModel: ThinDividerMarginUiModel): Int {
        return ThinDividerMarginViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            OrderTrackingLoadingViewHolder.LAYOUT -> OrderTrackingLoadingViewHolder(parent)
            OrderTrackingErrorViewHolder.LAYOUT -> OrderTrackingErrorViewHolder(parent, orderTrackingListener)
            OrderTrackingTickerViewHolder.LAYOUT -> OrderTrackingTickerViewHolder(
                parent,
                orderTrackingListener
            )
            RestaurantUserAddressViewHolder.LAYOUT -> RestaurantUserAddressViewHolder(parent)
            ShippingDetailHeaderViewHolder.LAYOUT -> ShippingDetailHeaderViewHolder(parent)
            OrderDetailHeaderViewHolder.LAYOUT -> OrderDetailHeaderViewHolder(parent)
            CompletedStatusViewHolderPayloadViewHolder.LAYOUT -> CompletedStatusViewHolderPayloadViewHolder(parent)
            ShippingDetailViewHolderPayloadViewHolder.LAYOUT -> ShippingDetailViewHolderPayloadViewHolder(parent)
            OrderDetailToggleCtaViewHolderPayloadViewHolder.LAYOUT -> OrderDetailToggleCtaViewHolderPayloadViewHolder(
                parent,
                orderTrackingListener
            )
            DriverSectionViewHolderPayloadViewHolder.LAYOUT -> DriverSectionViewHolderPayloadViewHolder(parent, orderTrackingListener)
            OrderTrackingStatusInfoViewHolderPayloadViewHolder.LAYOUT -> OrderTrackingStatusInfoViewHolderPayloadViewHolder(parent)
            OrderTrackingEstimationViewHolder.LAYOUT -> OrderTrackingEstimationViewHolder(parent)
            FoodItemViewHolder.LAYOUT -> FoodItemViewHolder(parent, recyclerViewPollerListener)
            InvoiceOrderNumberViewHolderPayloadViewHolder.LAYOUT -> InvoiceOrderNumberViewHolderPayloadViewHolder(parent, orderTrackingListener)
            PaymentDetailViewHolderPayloadViewHolder.LAYOUT -> PaymentDetailViewHolderPayloadViewHolder(parent)
            PaymentMethodViewHolderPayloadViewHolder.LAYOUT -> PaymentMethodViewHolderPayloadViewHolder(parent)
            PaymentAmountViewHolderPayloadViewHolder.LAYOUT -> PaymentAmountViewHolderPayloadViewHolder(parent)
            PaymentHeaderViewHolder.LAYOUT -> PaymentHeaderViewHolder(parent)
            TemporaryFinishOrderViewHolder.LAYOUT -> TemporaryFinishOrderViewHolder(
                parent,
                orderTrackingListener
            )
            ThickDividerViewHolder.LAYOUT -> ThickDividerViewHolder(parent)
            ThinDividerViewHolder.LAYOUT -> ThinDividerViewHolder(parent)
            ThinDividerMarginViewHolder.LAYOUT -> ThinDividerMarginViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}