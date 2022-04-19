package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoritShopResponseData(
    @SerializedName("favorite_shop") @Expose var data: FavShopItemData? = null
) {
    override fun toString(): String {
        return """FavoritShopResponseData{data=$data}"""
    }
}
