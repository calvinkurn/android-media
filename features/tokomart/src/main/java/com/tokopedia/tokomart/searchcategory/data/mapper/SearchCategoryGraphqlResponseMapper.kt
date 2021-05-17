package com.tokopedia.tokomart.searchcategory.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel

internal fun getSearchProduct(graphqlResponse: GraphqlResponse): AceSearchProductModel.SearchProduct {
    return graphqlResponse
            .getData<AceSearchProductModel?>(AceSearchProductModel::class.java)?.searchProduct
            ?: AceSearchProductModel.SearchProduct()
}