package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetRechargeCatalogUseCase.QUERY_NAME, GetRechargeCatalogUseCase.QUERY)
class GetRechargeCatalogUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<CatalogData.Response>(graphqlRepository)  {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): CatalogData.Response {
        val gqlRequest = GraphqlRequest(QUERY, CatalogData.Response::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(CatalogData.Response::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(CatalogData.Response::class.java) as CatalogData.Response)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    companion object{
        private const val KEY_MENU_ID = "menuID"
        private const val KEY_OPERATOR = "operator"
        private const val RECHARGE_PARAM_ANDROID_DEVICE_ID = 5

        const val QUERY_NAME = "GetRechargeCatalogUseCaseQuery"
        const val QUERY = """
        query rechargeCatalogProductInput(${'$'}menuID: Int!,${'$'}operator: String!){
          rechargeCatalogProductInput(menuID:${'$'}menuID, platformID: ${RECHARGE_PARAM_ANDROID_DEVICE_ID}, operator:${'$'}operator) {
            needEnquiry
            isShowingProduct
            enquiryFields {
              id
              param_name
              name
              style
              text
              placeholder
              help
              data_collections {
                value
              }
              validations {
                rule
                title
              }
            }
            product {
              name
              text
              dataCollections {
                name
                products {
                  id
                  attributes {
                    desc
                    price
                    price_plain
                    promo {
                      id
                      new_price
                    }
                    product_labels
                    detail
                    detail_compact
                    detail_url
                    detail_url_text
                  }
                }
              }
            }
          }
        }
    """

        fun createProductListParams(menuID: Int, operator: String): RequestParams {
            return RequestParams.create().apply {
                putInt(KEY_MENU_ID, menuID)
                putString(KEY_OPERATOR, operator)
            }
        }
    }
}