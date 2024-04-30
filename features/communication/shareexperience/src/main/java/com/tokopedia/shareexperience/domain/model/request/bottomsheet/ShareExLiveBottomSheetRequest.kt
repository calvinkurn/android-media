package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExLiveBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("contentId")
    val contentId: String,
    @SerializedName("origin")
    val origin: String
) : ShareExBottomSheetRequest
