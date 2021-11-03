package com.tokopedia.product.manage.feature.violation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ViolationReasonDetailResponse (
    @SerializedName("visionGetPendingReasonDetail")
    @Expose
    val detail: VisionGetPendingReasonDetail = VisionGetPendingReasonDetail()
)

data class VisionGetPendingReasonDetail(
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("main_title")
    @Expose
    val mainTitle: String = "",
    @SerializedName("description")
    @Expose
    val description: PendingReasonDescription = PendingReasonDescription(),
    @SerializedName("resolution_steps")
    @Expose
    val resolutionSteps: List<PendingReasonResolutionSteps> = listOf(),
    @SerializedName("call_to_action_info")
    @Expose
    val callToActionInfo: CallToActionInfo = CallToActionInfo()
)

data class PendingReasonDescription(
    @SerializedName("pre_desc_text")
    @Expose
    val preDescText: String = "",
    @SerializedName("desc_text")
    @Expose
    val descText: String = ""
)

data class PendingReasonResolutionSteps(
    @SerializedName("html_text")
    @Expose
    val htmlText: String = ""
)

data class CallToActionInfo(
    @SerializedName("button_text")
    @Expose
    val buttonText: String = "",
    @SerializedName("button_link")
    @Expose
    val buttonLink: String = ""
)