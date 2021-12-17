package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore

data class ShopShareOtherResponse(
    @Expose
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByID = ShopInfoByID(),
) {
    data class ShopInfoByID(
        @Expose
        @SerializedName("result")
        val result: List<Result> = listOf()
    ) {
        data class Result(
            @Expose
            @SerializedName("shopSnippetURL")
            val shopSnippetUrl: String = "",
            @Expose
            @SerializedName("location")
            val location: String = "",
            @Expose
            @SerializedName("branchLinkDomain")
            val branchLinkDomain: String = "",
            @Expose
            @SerializedName("shopCore")
            val shopCore: ShopCore = ShopCore()
        )
    }
}