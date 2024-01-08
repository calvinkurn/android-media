package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExProductBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("productId")
    val id: Long
) : ShareExBottomSheetRequest
