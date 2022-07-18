package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddShopShowcaseParam(
        @Expose
        @SerializedName("name")
        var name: String = "",
        @SuppressLint("Invalid Data Type")
        @Expose
        @SerializedName("productIDs")
        var productIdList: MutableList<String> = mutableListOf()
)