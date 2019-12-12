package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-04.
 */
data class SomDetailShipping (
        val shippingName: String = "",
        val shippingPrice: String = "",
        val receiverName: String = "",
        val receiverPhone: String = "",
        val receiverStreet: String = "",
        val receiverDistrict: String = "",
        val shippingNotes: String = "",
        val isFreeShipping: Boolean = false,
        val driverPhoto: String = "",
        val driverName: String = "",
        val driverPhone: String = "",
        val driverLicense: String = "",
        val onlineBookingCode: String = "",
        val onlineBookingState: Int = -1,
        val onlineBookingMsg: String = "",
        val onlineBookingMsgArray: List<String> = listOf(),
        val onlineBookingType: String = "",
        val isRemoveAwb: Boolean = false)