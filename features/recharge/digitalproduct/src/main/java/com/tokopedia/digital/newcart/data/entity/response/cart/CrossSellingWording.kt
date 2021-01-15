package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CrossSellingWording {
    @SerializedName("header_title")
    @Expose
    var headerTitle: String? = null
    @SerializedName("body_title")
    @Expose
    var bodyTitle: String? = null
    @SerializedName("body_content_before")
    @Expose
    var bodyContentBefore: String? = null
    @SerializedName("body_content_after")
    @Expose
    var bodyContentAfter: String? = null
    @SerializedName("cta_button_text")
    @Expose
    var checkoutButtonText: String? = null
}
