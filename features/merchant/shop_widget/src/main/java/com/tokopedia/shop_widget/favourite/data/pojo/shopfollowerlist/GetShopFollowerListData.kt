package com.tokopedia.shop_widget.favourite.data.pojo.shopfollowerlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopFollowerListData(
        @SerializedName("shopFollowerList")
        @Expose
        val shopFollowerList: ShopFollowerList
)