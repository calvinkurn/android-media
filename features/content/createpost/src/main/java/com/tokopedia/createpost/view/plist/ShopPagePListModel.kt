package com.tokopedia.createpost.view.plist

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class GetShopProduct(
    @field:SerializedName("GetShopProduct")
    val productList: ShopPagePListModel
)

data class ShopPagePListModel(
    @field:SerializedName("data")
    val data: MutableList<ShopPageProduct>,

    @field:SerializedName("links")
    val paging: Paging
)

data class Paging(
    @field:SerializedName("next")
    val next: String
)


data class ShopPageProduct(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("primary_image")
    val pImage: PrimaryImage,

    @field:SerializedName("price")
    val price: SPrice,

    @field:SerializedName("product_id")
    val pId: String? = null,

    @field:SerializedName("campaign")
    val campaign: Campaign
) : BaseItem()

data class PrimaryImage(
    @field:SerializedName("resize300")
    val img: String
)

data class SPrice(
    @field:SerializedName("text_idr")
    val priceIdr: String
)

data class Campaign(
    @field:SerializedName("discounted_percentage")
    val dPrice: String,

    @field:SerializedName("discounted_price_fmt")
    val dPriceFormatted: String,

    @field:SerializedName("original_price_fmt")
    val oPriceFormatted: String,
)