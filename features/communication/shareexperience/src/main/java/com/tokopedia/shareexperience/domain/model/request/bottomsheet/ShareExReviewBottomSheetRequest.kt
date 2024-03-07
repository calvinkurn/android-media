package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExReviewBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("reviewId")
    val reviewId: String,
    @SerializedName("productId")
    val productId: String
): ShareExBottomSheetRequest
