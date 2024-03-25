package com.tokopedia.shareexperience.domain.model.request.bottomsheet

import com.google.gson.annotations.SerializedName

data class ShareExShopBottomSheetRequest(
    @SerializedName("pageType")
    val pageType: Int,
    @SerializedName("shopId")
    val id: String
) : ShareExBottomSheetRequest
