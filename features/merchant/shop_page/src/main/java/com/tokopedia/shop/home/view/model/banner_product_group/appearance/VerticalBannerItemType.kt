package com.tokopedia.shop.home.view.model.banner_product_group.appearance

import android.os.Parcelable
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerticalBannerItemType(
    val imageUrl: String,
    val appLink: String,
    override val id : String = imageUrl,
    override val overrideTheme: Boolean = false,
    override val colorSchema: ShopPageColorSchema = ShopPageColorSchema()
) : ShopHomeBannerProductGroupItemType, Parcelable
