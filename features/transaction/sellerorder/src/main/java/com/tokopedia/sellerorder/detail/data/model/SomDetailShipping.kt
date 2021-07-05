package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-04.
 */
data class SomDetailShipping (
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
        val logisticInfo: SomDetailOrder.Data.GetSomDetail.LogisticInfo = SomDetailOrder.Data.GetSomDetail.LogisticInfo()
)