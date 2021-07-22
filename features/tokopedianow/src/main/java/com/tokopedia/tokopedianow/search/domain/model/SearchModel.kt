package com.tokopedia.tokopedianow.search.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct

data class SearchModel(
        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct(),

        @SerializedName("category_filter")
        @Expose
        val categoryFilter: DataValue = DataValue(),

        @SerializedName("quick_filter")
        @Expose
        val quickFilter: DataValue = DataValue(),

        @SerializedName("channel")
        @Expose
        val bannerChannel: Channels = Channels(),
)