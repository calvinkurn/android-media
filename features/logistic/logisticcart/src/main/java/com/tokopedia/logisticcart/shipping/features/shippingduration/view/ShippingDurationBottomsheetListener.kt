package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
interface ShippingDurationBottomsheetListener {

    fun onShippingDurationChoosen(shippingCourierUiModels: List<ShippingCourierUiModel>?,
                                  selectedCourier: ShippingCourierUiModel?,
                                  recipientAddressModel: RecipientAddressModel?,
                                  cartPosition: Int, selectedServiceId: Int,
                                  serviceData: ServiceData?, flagNeedToSetPinpoint: Boolean,
                                  isDurationClick: Boolean, isClearPromo: Boolean)

    fun onLogisticPromoChosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        courierData: ShippingCourierUiModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        serviceData: ServiceData?,
        flagNeedToSetPinpoint: Boolean,
        promoCode: String?,
        selectedServiceId: Int,
        logisticPromo: LogisticPromoUiModel
    )

    fun onNoCourierAvailable(message: String?) {}
    fun onShippingDurationButtonCloseClicked() {}
    fun onShowDurationListWithCourierPromo(isCourierPromo: Boolean, duration: String?) {}
    fun onShowLogisticPromo(listLogisticPromo: List<LogisticPromoUiModel>) {}
}
