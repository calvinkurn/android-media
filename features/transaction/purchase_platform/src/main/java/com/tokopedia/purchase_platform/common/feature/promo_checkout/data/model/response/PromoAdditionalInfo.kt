package com.tokopedia.purchase_platform.common.feature.promo_checkout.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class PromoAdditionalInfo (
        @SerializedName("message_info")
        var messageInfo: PromoMessageInfo = PromoMessageInfo(),

        @SerializedName("error_detail")
        var errorDetail: PromoErrorDetail = PromoErrorDetail(),

        @SerializedName("empty_cart_info")
        var emptyCartInfo: PromoEmptyCartInfo = PromoEmptyCartInfo())