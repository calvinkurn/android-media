package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.utils.CommonTopupBillsGqlQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery(GetRechargeCatalogPrefixSelectUseCase.QUERY_NAME, GetRechargeCatalogPrefixSelectUseCase.QUERY)
class GetRechargeCatalogPrefixSelectUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<TelcoCatalogPrefixSelect>(graphqlRepository) {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): TelcoCatalogPrefixSelect {
        val gqlRequest = GraphqlRequest(QUERY, TelcoCatalogPrefixSelect::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest),
            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())
        val error = gqlResponse.getError(TelcoCatalogPrefixSelect::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(TelcoCatalogPrefixSelect::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createPrefixSelectParam(menuId: Int): RequestParams {
        return RequestParams.create().apply {
            putInt(KEY_MENU_ID, menuId)
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        private const val RECHARGE_PARAM_ANDROID_DEVICE_ID = 5
        private const val EXP_TIME = 10

        const val QUERY_NAME = "GetRechargeCatalogPrefixSelectUseCaseQuery"
        const val QUERY = """
        query telcoPrefixSelect(${'$'}menuID: Int!) {
          rechargeCatalogPrefixSelect(menuID:${'$'}menuID, platformID: ${RECHARGE_PARAM_ANDROID_DEVICE_ID}) {
            componentID
            name
            paramName
            text
            help
            placeholder
            validations {
              id
              title
              message
              rule
            }
            prefixes {
              key
              value
              operator {
                id
                attributes {
                  name
                  default_product_id
                  image_url
                }
              }
            }
          }
        }
        """
    }
}