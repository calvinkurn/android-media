package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExDefaultBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("metadata")
    val metadata: String
) : ShareExBottomSheetRequest
