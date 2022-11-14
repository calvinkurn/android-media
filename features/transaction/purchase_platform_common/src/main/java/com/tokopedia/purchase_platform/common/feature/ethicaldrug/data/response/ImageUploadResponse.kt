package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response

import com.google.gson.annotations.SerializedName

data class ImageUploadResponse (
    @SerializedName("show_image_upload")
    var showImageUpload: Boolean = false,
    @SerializedName("text")
    var text: String = "",
    @SerializedName("left_icon_url")
    var leftIconUrl: String = "",
    @SerializedName("checkout_id")
    var checkoutId: String = "",
    @SerializedName("front_end_validation")
    var frontEndValidation: Boolean = false,
    @SerializedName("consultation_flow")
    var consultationFlow: Boolean = false,
    @SerializedName("rejected_wording")
    var rejectedWording: String = ""
)
