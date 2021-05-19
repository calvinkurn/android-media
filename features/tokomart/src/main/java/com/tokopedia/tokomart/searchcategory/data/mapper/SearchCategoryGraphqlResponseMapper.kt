package com.tokopedia.tokomart.searchcategory.data.mapper

import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokomart.searchcategory.domain.model.FilterModel
import com.tokopedia.tokomart.searchcategory.domain.model.QuickFilterModel

internal fun getSearchProduct(graphqlResponse: GraphqlResponse): AceSearchProductModel.SearchProduct {
    return graphqlResponse
            .getData<AceSearchProductModel?>(AceSearchProductModel::class.java)?.searchProduct
            ?: AceSearchProductModel.SearchProduct()
}

internal fun getCategoryFilter(graphqlResponse: GraphqlResponse): DataValue {
    return graphqlResponse.getData<CategoryFilterModel?>(FilterModel::class.java)
            ?.filterSortProduct
            ?.data ?: DataValue()
}

internal fun getQuickFilter(graphqlResponse: GraphqlResponse): DataValue {
    return graphqlResponse.getData<QuickFilterModel?>(FilterModel::class.java)
            ?.filterSortProduct
            ?.data ?: DataValue()
}