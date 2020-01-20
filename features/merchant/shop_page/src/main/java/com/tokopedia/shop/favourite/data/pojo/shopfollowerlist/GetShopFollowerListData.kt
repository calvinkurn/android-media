package com.tokopedia.shop.favourite.data.pojo.shopfollowerlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopFollowerListData(
        @SerializedName("shopFollowerList")
        @Expose
        val shopFollowerList: ShopFollowerList
)