package com.tokopedia.createpost.view.plist

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class GetShopPagePListSortModel(
    @field:SerializedName("result")
    val result: List<ShopPagePListSortItem>
)

data class ShopPagePListSortItem(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("value")
    val value: Int,

    var isSelected: Boolean = false
)