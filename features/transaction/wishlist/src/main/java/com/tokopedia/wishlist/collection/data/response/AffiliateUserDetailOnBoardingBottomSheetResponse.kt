package com.tokopedia.wishlist.collection.data.response

import com.google.gson.annotations.SerializedName

data class AffiliateUserDetailOnBoardingBottomSheetResponse(
    @SerializedName("affiliateUserDetail")
    val affiliateUserDetail: AffiliateUserDetail
) {
    data class AffiliateUserDetail(
        @SerializedName("IsRegistered")
        val isRegistered: Boolean = false
    )
}
