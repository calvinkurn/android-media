package com.tokopedia.tokopedianow.search.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetTargetedTickerResponse
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel

data class SearchModel(
    @SerializedName("get_targeted_ticker")
    @Expose
    val targetedTicker: GetTargetedTickerResponse = GetTargetedTickerResponse(),

    @SerializedName("displayAdsV3")
    @Expose
    val productAds: ProductAdsResponse = ProductAdsResponse(),

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

    @SerializedName("feedbackFieldToggle")
    @Expose
    var feedbackFieldToggle: GetFeedbackFieldModel = GetFeedbackFieldModel()
) {
    fun getResponseCode() = searchProduct.header.responseCode

    fun getSuggestion() = searchProduct.data.suggestion

    fun getRelated() = searchProduct.data.related
}
