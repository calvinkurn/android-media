package com.tokopedia.shop.home.view.model.banner_product_group.appearance

import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.product.view.datamodel.ShopBadgeUiModel

data class ProductItemType(
    val productId: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    val slashedPrice: String,
    val slashedPricePercent: Int,
    val rating: String,
    val appLink: String,
    val showProductInfo: Boolean,
    val labelGroups: List<LabelGroup>,
    val badges: List<ShopBadgeUiModel>,
    val freeOngkir: FreeOngkir,
    override val id: String = productId,
    override val overrideTheme: Boolean,
    override val colorSchema: ShopPageColorSchema
) : ShopHomeBannerProductGroupItemType
