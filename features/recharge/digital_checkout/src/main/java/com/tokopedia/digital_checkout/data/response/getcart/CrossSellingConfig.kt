package com.tokopedia.digital_checkout.data.response.getcart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/01/21
 */

data class CrossSellingConfig (
        @SerializedName("can_be_skipped")
        @Expose
        val canBeSkipped: Boolean = false,

        @SerializedName("is_checked")
        @Expose
        val isChecked: Boolean = false,

        @SerializedName("wording")
        @Expose
        val wording: CrossSellingWording = CrossSellingWording(),

        @SerializedName("wording_is_subscribed")
        @Expose
        val wordingIsSubscribe: CrossSellingWording = CrossSellingWording()
) {
    data class CrossSellingWording (
            @SerializedName("header_title")
            @Expose
            var headerTitle: String = "",

            @SerializedName("body_title")
            @Expose
            var bodyTitle: String = "",

            @SerializedName("body_content_before")
            @Expose
            var bodyContentBefore: String = "",

            @SerializedName("body_content_after")
            @Expose
            var bodyContentAfter: String = "",

            @SerializedName("cta_button_text")
            @Expose
            var checkoutButtonText: String = ""
    )
}