package com.tokopedia.tokopedianow.searchcategory.data.mapper

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse
import com.tokopedia.tokopedianow.common.domain.model.GetProductAdsResponse.ProductAdsResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse.RepurchaseData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.DynamicChannelModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.QuickFilterModel

internal fun getProductAds(graphqlResponse: GraphqlResponse): ProductAdsResponse {
    return graphqlResponse
        .getData<GetProductAdsResponse?>(GetProductAdsResponse::class.java)?.productAds
        ?: ProductAdsResponse()
}

internal fun getSearchProduct(graphqlResponse: GraphqlResponse): AceSearchProductModel.SearchProduct {
    return graphqlResponse
            .getData<AceSearchProductModel?>(AceSearchProductModel::class.java)?.searchProduct
            ?: AceSearchProductModel.SearchProduct()
}

internal fun getCategoryFilter(graphqlResponse: GraphqlResponse): DataValue {
    return graphqlResponse.getData<CategoryFilterModel?>(CategoryFilterModel::class.java)
            ?.filterSortProduct
            ?.data ?: DataValue()
}

internal fun getQuickFilter(graphqlResponse: GraphqlResponse): DataValue {
    return graphqlResponse.getData<QuickFilterModel?>(QuickFilterModel::class.java)
            ?.filterSortProduct
            ?.data ?: DataValue()
}

internal fun getBanner(graphqlResponse: GraphqlResponse): Channels {
    return graphqlResponse.getData<DynamicChannelModel?>(DynamicChannelModel::class.java)
            ?.dynamicHomeChannelCommon
            ?.channels
            ?.getOrElse(0) { Channels() }
            ?: Channels()
}

internal fun getRepurchaseWidget(graphqlResponse: GraphqlResponse): RepurchaseData {
    return graphqlResponse
            .getData<GetRepurchaseResponse?>(GetRepurchaseResponse::class.java)
            ?.response
            ?.data
            ?: RepurchaseData()
}
