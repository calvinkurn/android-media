package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.utils.paging.PagingHandler.PagingHandlerModel

data class ShopItemData(
        @SerializedName("list") @Expose var list: List<ShopItem>? = null,
        @SerializedName("paging") @Expose var pagingHandlerModel: PagingHandlerModel? = null
) {

    override fun toString(): String {
        return "ShopItemData{list=$list, pagingHandlerModel=$pagingHandlerModel}"
    }

}
