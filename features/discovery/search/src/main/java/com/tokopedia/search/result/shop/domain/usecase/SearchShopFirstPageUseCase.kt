package com.tokopedia.search.result.shop.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.*
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap

internal class SearchShopFirstPageUseCase(
        private val graphqlCacheStrategy: GraphqlCacheStrategy,
        private val graphqlRepository: GraphqlRepository
): UseCase<SearchShopModel>() {

    @GqlQuery("SearchShopFirstPageQuery", GQL_QUERY)
    override suspend fun executeOnBackground(): SearchShopModel {
        val graphqlRequest = GraphqlRequest(
                SearchShopFirstPageQuery.GQL_QUERY,
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

        variables[KEY_PARAMS] = UrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)
        variables[KEY_HEADLINE_PARAMS] = createHeadlineParams(useCaseRequestParams.parameters)
        variables[KEY_QUICK_FILTER_PARAMS] = createQuickFilterParams(useCaseRequestParams.parameters)

        return variables
    }

    private fun createHeadlineParams(requestParams: Map<String, Any>): String {
        val headlineParams = HashMap(requestParams)

        headlineParams[TopAdsParams.KEY_EP] = SearchConstant.SearchShop.HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = SearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = SearchConstant.SearchShop.HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_SRC] = SearchConstant.SearchShop.ADS_SOURCE
        headlineParams[TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT] = SearchConstant.SearchShop.HEADLINE_PRODUCT_COUNT

        return UrlParamUtils.generateUrlParamString(headlineParams)
    }

    private fun createQuickFilterParams(requestParams: Map<String, Any>): String {
        val quickFilterParams = HashMap(requestParams)

        quickFilterParams[KEY_PAGE_SOURCE] = PAGE_SOURCE_SEARCH_SHOP
        quickFilterParams[KEY_SOURCE] = SOURCE_QUICK_FILTER

        return UrlParamUtils.generateUrlParamString(quickFilterParams)
    }

    companion object {
        private const val GQL_QUERY = """
query SearchShop(${'$'}params: String!, ${'$'}headline_params: String, ${'$'}quick_filter_params: String!) {
    aceSearchShop(params: ${'$'}params) {
        source
        total_shop
        search_url
        paging {
            uri_next
            uri_previous
        }
        tab_name
        suggestion {
            currentKeyword
            query
            text
        }
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
    headlineAds: displayAdsV3(displayParams: ${'$'}headline_params) {
        status {
          error_code
          message
        }
        header {
          process_time
          total_data
        }
        data {
          id
          ad_ref_key
          redirect
          ad_click_url
          headline {
            template_id
            name
            image {
              full_url
              full_ecs
            }
            shop {
              id
              name
              domain
              tagline
              slogan
              location
              city
              gold_shop
              gold_shop_badge
              shop_is_official
              merchant_vouchers
              product {
                id
                name
                price_format
                applinks
                rating_average
                label_group {
                    title
                    type
                    position
                }
                image_product {
                  product_id
                  product_name
                  image_url
                  image_click_url
                }
              }
              image_shop {
                cover
                s_url
                xs_url
                cover_ecs
                s_ecs
                xs_ecs
              }
            }
            badges {
              image_url
              show
              title
            }
            button_text
            promoted_text
            description
            uri
          }
          applinks
        }
    }
    quickFilter: filter_sort_product(params:${'$'}quick_filter_params) {
        data {
            filter {
                title
                template_name
                search {
                    placeholder
                }
                options {
                    key
                    value
                    name
                    icon
                    totalData
                    valMax
                    valMin
                    hexColor
                    isPopular
                    isNew
                    Description
                }
            }
        }
    }
}
        """
    }
}