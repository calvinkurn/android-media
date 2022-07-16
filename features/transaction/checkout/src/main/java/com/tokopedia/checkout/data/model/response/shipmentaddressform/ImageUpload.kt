package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ImageUpload (
    @SerializedName("show_image_upload")
    var showImageUpload: Boolean = false,
    @SerializedName("text")
    var text: String = "",
    @SerializedName("left_icon_url")
    var leftIconUrl: String = "",
    @SerializedName("checkout_id")
    var checkoutId: String = "",
)
