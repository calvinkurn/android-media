package com.tokopedia.createpost.usecase

import com.tokopedia.createpost.data.non_seller_model.SearchShopModel
import com.tokopedia.createpost.common.util.CreatePostSearchConstant
import com.tokopedia.createpost.view.util.CreatePostUrlParamUtils
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import java.util.*
import javax.inject.Inject

private const val GQL_QUERY = """
query SearchShop(${'$'}params : String!) {
    aceSearchShop(params: ${'$'}params) {
        source
        total_shop
        search_url
        header {
            response_code
            keyword_process
        }
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
            is_pm_pro
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

@GqlQuery("FeedSearchShopLoadMoreQuery", GQL_QUERY)
 class FeedSearchShopLoadMoreUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<SearchShopModel>() {

    init {
        setTypeClass(SearchShopModel::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GQL_QUERY)
    }

    fun setParams() {
        val map = mutableMapOf("req" to createParametersForQuery())
        setRequestParams(map)
    }

    suspend fun execute(): SearchShopModel {
        this.setParams()
        return executeOnBackground()
    }

    private fun createParametersForQuery(): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[CreatePostSearchConstant.GQL.KEY_PARAMS] = CreatePostUrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)
        return variables
    }

}