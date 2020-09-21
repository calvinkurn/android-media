package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel

data class FavShopItemData(
        @SerializedName("shops") @Expose var list: List<FavShopsItem>? = null,
        @SerializedName("paging") @Expose var pagingHandlerModel: PagingHandlerModel? = null
) {

    override fun toString(): String {
        return """FavShopItemData{list=$list,pagingHandlerModel=$pagingHandlerModel}"""
    }

}
