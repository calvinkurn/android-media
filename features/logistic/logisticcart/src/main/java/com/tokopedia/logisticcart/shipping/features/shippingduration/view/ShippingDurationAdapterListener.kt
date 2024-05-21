package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
interface ShippingDurationAdapterListener {
    fun onShippingDurationChoosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        cartPosition: Int,
        serviceData: ServiceData,
        shippingDuration: ShippingDurationUiModel
    )

    fun onLogisticPromoClicked(data: LogisticPromoUiModel)

    fun onCollapseClicked(isCollapsed: Boolean)
}
