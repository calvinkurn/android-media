package com.tokopedia.shop.home.view.model.banner_product_group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerticalBannerItemType(
    val imageUrl: String,
    val appLink: String,
    override val id : String = imageUrl
) : ShopHomeBannerProductGroupItemType, Parcelable
