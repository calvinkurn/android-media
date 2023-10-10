package com.tokopedia.shop.home.view.model.banner_product_group.appearance

import android.os.Parcelable
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerticalBannerItemType(
    val componentId: Long?,
    val componentName: BannerProductGroupUiModel.Tab.ComponentList.ComponentName?,
    val imageUrl: String,
    val appLink: String,
    override val id : String = imageUrl,
    override val overrideTheme: Boolean = false,
    override val colorSchema: ShopPageColorSchema = ShopPageColorSchema()
) : ShopHomeBannerProductGroupItemType, Parcelable
