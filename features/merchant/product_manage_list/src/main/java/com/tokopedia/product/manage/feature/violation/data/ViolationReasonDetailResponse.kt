package com.tokopedia.product.manage.feature.violation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ViolationReasonDetailResponse(
        @SerializedName("visionGetPendingReasonDetail")
        @Expose
        val detail: VisionGetPendingReasonDetail = VisionGetPendingReasonDetail()
)

data class VisionGetPendingReasonDetail(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("content")
        @Expose
        val content: PendingReasonContent = PendingReasonContent()
)

data class PendingReasonContent(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: PendingReasonDescription = PendingReasonDescription(),
        @SerializedName("resolution")
        @Expose
        val resolution: PendingReasonResolution = PendingReasonResolution(),
        @SerializedName("cta")
        @Expose
        val ctaList: List<ViolationCTA> = listOf()
)

data class PendingReasonDescription(
        @SerializedName("info")
        @Expose
        val descInfo: String = "",
        @SerializedName("detail")
        @Expose
        val descDetail: String = ""
)

data class PendingReasonResolution(
        @SerializedName("info")
        @Expose
        val resolutionInfo: String = "",
        @SerializedName("steps")
        @Expose
        val resolutionSteps: List<String> = listOf()
)

data class ViolationCTA(
        @SerializedName("text")
        @Expose
        val buttonText: String = "",
        @SerializedName("link")
        @Expose
        val buttonLink: String = ""
)