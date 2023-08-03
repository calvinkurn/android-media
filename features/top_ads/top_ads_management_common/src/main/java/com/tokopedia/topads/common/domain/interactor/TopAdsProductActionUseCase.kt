package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION
import com.tokopedia.topads.common.data.internal.ParamObject.ADS
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.common.data.response.ProductActionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 5/6/20.
 */

private const val UPDATE_SINGLE_ADS = """
    mutation topadsUpdateSingleAds(${'$'}action: String!, ${'$'}ads: [topadsUpdateSingleAdsReqData]!, ${'$'}shopID: String!) {
  topadsUpdateSingleAds(action: ${'$'}action, ads: ${'$'}ads, shopID: ${'$'}shopID) {
        data {
            action
            shopID
        }
        errors {
            code
            detail
            title
        }
    }
}
"""

@GqlQuery("GetTopadsUpdateSingleAds", UPDATE_SINGLE_ADS)
class TopAdsProductActionUseCase @Inject constructor(val userSession: UserSessionInterface, graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<ProductActionResponse>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): ProductActionResponse {
        graphql.apply {
            setGraphqlQuery(GetTopadsUpdateSingleAds.GQL_QUERY)
            setTypeClass(ProductActionResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParams(action: String, adIds: List<String>, selectedFilter: String?): RequestParams {
        val params = RequestParams.create()
        val product: ArrayList<Map<String, String?>> = arrayListOf()

        adIds.forEach {
            val map = mapOf(
                ParamObject.AD_ID to it,
                ParamObject.GROUPID to (
                    selectedFilter
                        ?: ""
                    ),
                PRICE_BID to null
            )
            product.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[ADS] = product
        params.putAll(queryMap)
        return params
    }
}
