package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailShippingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailToggleCtaUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingLoadingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingTickerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentGrandTotalUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.PaymentMethodUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.RestaurantUserAddressUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StatusInfoHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerMarginUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel

interface OrderTrackingTypeFactory {
    fun type(orderTrackingLoadingUiModel: OrderTrackingLoadingUiModel): Int

    fun type(orderTrackingErrorUiModel: OrderTrackingErrorUiModel): Int

    fun type(orderTrackingTickerUiModel: OrderTrackingTickerUiModel): Int

    fun type(restaurantUserAddressUiModel: RestaurantUserAddressUiModel): Int

    fun type(shippingHeaderUiModel: ShippingHeaderUiModel): Int

    fun type(orderDetailShippingUiModel: OrderDetailShippingUiModel): Int

    fun type(orderDetailHeaderUiModel: OrderDetailHeaderUiModel): Int

    fun type(orderDetailToggleCtaUiModel: OrderDetailToggleCtaUiModel): Int

    fun type(driverSectionUiModel: DriverSectionUiModel): Int

    fun type(orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel): Int

    fun type(statusInfoHeaderUiModel: StatusInfoHeaderUiModel): Int

    fun type(orderTrackingEstimationUiModel: OrderTrackingEstimationUiModel): Int

    fun type(foodItemUiModel: FoodItemUiModel): Int

    fun type(invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel): Int

    fun type(paymentInfoUiModel: PaymentInfoUiModel): Int

    fun type(paymentMethodUiModel: PaymentMethodUiModel): Int

    fun type(paymentGrandTotalUiModel: PaymentGrandTotalUiModel): Int

    fun type(thickDividerUiModel: ThickDividerUiModel): Int

    fun type(thinDividerUiModel: ThinDividerUiModel): Int

    fun type(thinDividerMarginUiModel: ThinDividerMarginUiModel): Int
}