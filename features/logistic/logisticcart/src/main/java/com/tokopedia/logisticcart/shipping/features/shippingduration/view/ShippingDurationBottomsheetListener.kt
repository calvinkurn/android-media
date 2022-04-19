package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
interface ShippingDurationBottomsheetListener {

    fun onShippingDurationChoosen(shippingCourierUiModels: List<ShippingCourierUiModel>?,
                                  courierItemData: CourierItemData?,
                                  recipientAddressModel: RecipientAddressModel?,
                                  cartPosition: Int, selectedServiceId: Int,
                                  serviceData: ServiceData?, flagNeedToSetPinpoint: Boolean,
                                  isDurationClick: Boolean, isClearPromo: Boolean)

    fun onLogisticPromoChosen(shippingCourierUiModels: List<ShippingCourierUiModel>?,
                              courierData: CourierItemData?,
                              recipientAddressModel: RecipientAddressModel?, cartPosition: Int,
                              serviceData: ServiceData?, flagNeedToSetPinpoint: Boolean, promoCode: String?, selectedServiceId: Int)

    fun onNoCourierAvailable(message: String?)
    fun onShippingDurationButtonCloseClicked()
    fun onShowDurationListWithCourierPromo(isCourierPromo: Boolean, duration: String?)
}