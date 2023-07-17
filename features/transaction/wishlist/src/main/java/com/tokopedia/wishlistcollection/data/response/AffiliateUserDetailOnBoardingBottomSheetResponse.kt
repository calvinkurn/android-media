package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class AffiliateUserDetailOnBoardingBottomSheetResponse(
    @SerializedName("data")
    val affiliateUserDetail: AffiliateUserDetail
) {
    data class AffiliateUserDetail(
        @SerializedName("IsRegistered")
        val isRegistered: Boolean = false,
    )
}
