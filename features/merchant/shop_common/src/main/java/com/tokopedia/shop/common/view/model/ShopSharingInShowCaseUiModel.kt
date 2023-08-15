package com.tokopedia.shop.common.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopSharingInShowCaseUiModel(
    var shopId: String = "",
    var shopName: String = "",
    var avatar: String = "",
    var location: String = "",
    var isOfficial: Boolean = false,
    var isGoldMerchant: Boolean = false,
    var shopStatus: Int = -1,
    var shopCoreUrl: String = "",
    var shopBadge: String = "",
    var tagline: String = "",
    var shopSnippetUrl: String = "",

    ) : Parcelable
