package com.tokopedia.tokopedianow.searchcategory.data.mapper

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_component.data.DynamicHomeChannelCommon.Channels
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRecentPurchaseResponse.RecentPurchaseData
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.DynamicChannelModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.QuickFilterModel

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

internal fun getRepurchaseWidget(graphqlResponse: GraphqlResponse): RecentPurchaseData {
    return graphqlResponse
            .getData<GetRecentPurchaseResponse?>(GetRecentPurchaseResponse::class.java)
            ?.response
            ?.data
            ?: RecentPurchaseData()
}