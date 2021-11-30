package com.tokopedia.createpost.common.view.plist

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class GetShopPagePListSortModel(
    @field:SerializedName("result")
    val result: List<ShopPagePListSortItem>
)

data class ShopPlIstSortingListBase(
    @field:SerializedName("shopSortingOptions")
    val sortData: GetShopPagePListSortModel
)

data class ShopPagePListSortItem(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("value")
    val value: Int,

    var isSelected: Boolean = false
)