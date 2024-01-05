package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.model.ProductRecommendationModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopadsGetProductRecommendationV2Usecase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ProductRecommendationModel>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(ProductRecommendationModel::class.java)
    }

    suspend operator fun invoke(shopId: String):
        ProductRecommendationModel {
        setRequestParams(createRequestParam(shopId).parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(shopId: String): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(ParamObject.SHOP_id, shopId)
        return requestParam
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topadsGetProductRecommendationV2"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}shop_id : String!){
              $OPERATION_NAME(shop_id:${'$'}shop_id){
                data {
                  info
                  nominal_id
                  products {
                      product_id
                      product_name
                      image_url
                      search_count
                      search_percent
                      recommended_bid
                      min_bid
                  }
                }
                errors{
                  code
                  detail
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}
