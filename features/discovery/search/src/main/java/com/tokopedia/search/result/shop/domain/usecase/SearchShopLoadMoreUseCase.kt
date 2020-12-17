package com.tokopedia.search.result.shop.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap

internal class SearchShopLoadMoreUseCase(
        private val graphqlCacheStrategy: GraphqlCacheStrategy,
        private val graphqlRepository: GraphqlRepository
): UseCase<SearchShopModel>() {

    @GqlQuery("SearchShopLoadMoreQuery", GQL_QUERY)
    override suspend fun executeOnBackground(): SearchShopModel {
        val graphqlRequest = GraphqlRequest(
                SearchShopLoadMoreQuery.GQL_QUERY,
                SearchShopModel::class.java,
                createParametersForQuery()
        )

        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)

        val error = graphqlResponse.getError(SearchShopModel::class.java)

        if (error == null || error.isEmpty()){
            return graphqlResponse.getData(SearchShopModel::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private fun createParametersForQuery(): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)

        return variables
    }

    companion object {
        private const val GQL_QUERY = """
query SearchShop(${'$'}params : String!) {
    aceSearchShop(params: ${'$'}params) {
        source
        total_shop
        search_url
        paging {
            uri_next
            uri_previous
        }
        tab_name
        shops {
            shop_id
            shop_name
            shop_domain
            shop_url
            shop_applink
            shop_image
            shop_image_300
            shop_description
            shop_tag_line
            shop_location
            shop_total_transaction
            shop_total_favorite
            shop_gold_shop
            shop_is_owner
            shop_rate_speed
            shop_rate_accuracy
            shop_rate_service
            shop_status
            products {
                id
                name
                url
                applink
                price
                price_format
                image_url
            }
            voucher {
                free_shipping
                cashback {
                    cashback_value
                    is_percentage
                }
            }
            shop_lucky
            reputation_image_uri
            reputation_score
            is_official
            ga_key
        }
        top_shop {
            shop_id
            shop_name
            shop_domain
            shop_url
            shop_applink
            shop_image
            shop_image_300
            shop_description
            shop_tag_line
            shop_location
            shop_total_transaction
            shop_total_favorite
            shop_gold_shop
            shop_is_owner
            shop_rate_speed
            shop_rate_accuracy
            shop_rate_service
            shop_status
            products {
                id
                name
                url
                applink
                price
                price_format
                image_url
            }
            voucher {
                free_shipping
                cashback {
                    cashback_value
                    is_percentage
                }
            }
            shop_lucky
            reputation_image_uri
            reputation_score
            is_official
            ga_key
        }
    }
}
        """
    }
}