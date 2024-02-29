package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExOthersBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("id")
    val id: String
) : ShareExBottomSheetRequest
