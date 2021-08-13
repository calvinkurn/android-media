package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CrossSellingConfig(
        @SerializedName("is_skipable")
        @Expose
        var isSkipAble: Boolean = false,

        @SerializedName("is_checked")
        @Expose
        var isChecked: Boolean = false,

        @SerializedName("wording")
        @Expose
        var wording: CrossSellingWording? = null,

        @SerializedName("wording_is_subscribed")
        @Expose
        var wordingIsSubscribed: CrossSellingWording? = null
) {
    data class CrossSellingWording(
            @SerializedName("header_title")
            @Expose
            var headerTitle: String? = null,

            @SerializedName("body_title")
            @Expose
            var bodyTitle: String? = null,

            @SerializedName("body_content_before")
            @Expose
            var bodyContentBefore: String? = null,

            @SerializedName("body_content_after")
            @Expose
            var bodyContentAfter: String? = null,

            @SerializedName("cta_button_text")
            @Expose
            var checkoutButtonText: String? = null
    )
}
