package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SmartReplyQuestionParam (
    @SerializedName(PARAM_MSG_ID)
    var msgId: String,

    @SerializedName(PARAM_PRODUCT_IDS)
    var productIds: String,

    @SerializedName(PARAM_ADDRESS_ID)
    var addressId: Long,

    @SerializedName(PARAM_DISTRICT_ID)
    var districtId: Long,

    @SerializedName(PARAM_POSTAL_CODE)
    var postalCode: String,

    @SerializedName(PARAM_LAT_LONG)
    var latLon: String
): GqlParam {
    companion object {
        const val PARAM_MSG_ID = "msgID"
        const val PARAM_PRODUCT_IDS = "productIDs"
        const val PARAM_ADDRESS_ID = "addressID"
        const val PARAM_DISTRICT_ID = "districtID"
        const val PARAM_POSTAL_CODE = "postalCode"
        const val PARAM_LAT_LONG = "latlon"
    }
}