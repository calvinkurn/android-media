package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.CompletedStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingLoadingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentAmountlUiModel
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

interface OrderTrackingTypeFactory {
    fun type(orderTrackingLoadingUiModel: OrderTrackingLoadingUiModel): Int

    fun type(orderTrackingErrorUiModel: OrderTrackingErrorUiModel): Int

    fun type(tickerInfoUiModel: TickerInfoData): Int

    fun type(restaurantUserAddressUiModel: RestaurantUserAddressUiModel): Int

    fun type(shippingHeaderUiModel: ShippingHeaderUiModel): Int

    fun type(shippingDetailUiModel: ShippingDetailUiModel): Int

    fun type(orderDetailHeaderUiModel: OrderDetailHeaderUiModel): Int

    fun type(orderDetailToggleCtaUiModel: OrderDetailToggleCtaUiModel): Int

    fun type(driverSectionUiModel: DriverSectionUiModel): Int

    fun type(orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel): Int

    fun type(completedStatusInfoUiModel: CompletedStatusInfoUiModel): Int

    fun type(orderTrackingEstimationUiModel: OrderTrackingEstimationUiModel): Int

    fun type(foodItemUiModel: FoodItemUiModel): Int

    fun type(invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel): Int

    fun type(paymentInfoUiModel: PaymentDetailUiModel): Int

    fun type(paymentMethodUiModel: PaymentMethodUiModel): Int

    fun type(paymentAmountlUiModel: PaymentAmountlUiModel): Int

    fun type(paymentHeaderUiModel: PaymentHeaderUiModel): Int

    fun type(temporaryFinishOrderUiModel: TemporaryFinishOrderUiModel): Int

    fun type(thickDividerUiModel: ThickDividerUiModel): Int

    fun type(thinDividerUiModel: ThinDividerUiModel): Int

    fun type(thinDividerMarginUiModel: ThinDividerMarginUiModel): Int
}