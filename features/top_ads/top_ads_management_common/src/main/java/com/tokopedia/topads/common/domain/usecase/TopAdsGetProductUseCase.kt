package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.raw.TOP_ADS_PRODUCT_GQL
import com.tokopedia.topads.common.data.response.OptionV3
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import javax.inject.Inject

@GqlQuery("TopAdsProductGql", TOP_ADS_PRODUCT_GQL)
class TopAdsGetProductUseCase @Inject constructor() : GraphqlUseCase<TopAdsProductResponse>() {

    companion object {
        const val PRODUCT_ID_KEY = "productID"
        const val OPTIONS_KEY = "options"
    }

    fun getProduct(
        productId: String,
        success: (TopAdsProductResponse) -> Unit
    ) {
        setRequestParams(mapOf(PRODUCT_ID_KEY to productId, OPTIONS_KEY to OptionV3(true)))
        setTypeClass(TopAdsProductResponse::class.java)
        setGraphqlQuery(TopAdsProductGql.GQL_QUERY)
        execute({
            success.invoke(it)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }
}
