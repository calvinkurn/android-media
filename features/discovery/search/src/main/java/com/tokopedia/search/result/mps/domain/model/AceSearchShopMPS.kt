package com.tokopedia.search.result.mps.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS

data class AceSearchShopMPS(
    @SerializedName("ace_search_shop_mps")
    @Expose
    val searchShopMPS: SearchShopMPS = SearchShopMPS(),
)
