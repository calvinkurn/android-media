package com.tokopedia.sellerorder.detail.data.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerorder.common.domain.model.TickerInfo

/**
 * Created by fwidjaja on 2019-10-04.
 */
data class SomDetailShipping(
    val shippingTitle: String = String.EMPTY,
    val shippingName: String = "",
    val receiverName: String = "",
    val receiverPhone: String = "",
    val receiverStreet: String = "",
    val receiverDistrict: String = "",
    val receiverProvince: String = "",
    val isFreeShipping: Boolean = false,
    val driverPhoto: String = "",
    val driverName: String = "",
    val driverPhone: String = "",
    val dropshipperName: String,
    val dropshipperPhone: String = "",
    val driverLicense: String = "",
    val onlineBookingCode: String = "",
    val onlineBookingState: Int = -1,
    val onlineBookingMsg: String = "",
    val onlineBookingMsgArray: List<String> = listOf(),
    val onlineBookingType: String = "",
    val isRemoveAwb: Boolean = false,
    val awb: String = "",
    val awbTextColor: String = "",
    val isShippingPrinted: Boolean = false,
    val shipmentLogo: String = "",
    val courierInfo: String = "",
    val logisticInfo: SomDetailOrder.GetSomDetail.LogisticInfo = SomDetailOrder.GetSomDetail.LogisticInfo(),
    val shipmentTickerInfo: TickerInfo? = TickerInfo()
)
