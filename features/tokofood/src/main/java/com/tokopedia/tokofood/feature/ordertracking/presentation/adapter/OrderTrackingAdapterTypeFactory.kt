package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DetailOrderHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverSectionUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.InvoiceOrderNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailShippingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingLoadingUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingTickerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ShippingHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StatusInfoHeaderUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThickDividerUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerMarginUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.ThinDividerUiModel

class OrderTrackingAdapterTypeFactory: BaseAdapterTypeFactory(), OrderTrackingTypeFactory {

    override fun type(orderTrackingLoadingUiModel: OrderTrackingLoadingUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(orderTrackingErrorUiModel: OrderTrackingErrorUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(orderTrackingTickerUiModel: OrderTrackingTickerUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(shippingHeaderUiModel: ShippingHeaderUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(orderDetailShippingUiModel: OrderDetailShippingUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(detailOrderHeaderUiModel: DetailOrderHeaderUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(driverSectionUiModel: DriverSectionUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(orderTrackingStatusInfoUiModel: OrderTrackingStatusInfoUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(statusInfoHeaderUiModel: StatusInfoHeaderUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(orderTrackingEstimationUiModel: OrderTrackingEstimationUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(foodItemUiModel: FoodItemUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(invoiceOrderNumberUiModel: InvoiceOrderNumberUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(thickDividerUiModel: ThickDividerUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(thinDividerUiModel: ThinDividerUiModel): Int {
        TODO("Not yet implemented")
    }

    override fun type(thinDividerMarginUiModel: ThinDividerMarginUiModel): Int {
        TODO("Not yet implemented")
    }

}