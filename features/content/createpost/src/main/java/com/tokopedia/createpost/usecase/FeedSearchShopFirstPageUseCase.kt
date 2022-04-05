package com.tokopedia.createpost.usecase

import com.tokopedia.createpost.data.non_seller_model.SearchShopModel
import com.tokopedia.createpost.common.util.CreatePostSearchConstant
import com.tokopedia.createpost.view.util.CreatePostUrlParamUtils
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.sdk.domain.TopAdsParams
import java.util.*
import javax.inject.Inject

private const val GQL_QUERY = """
query SearchShop(${'$'}params: String!, ${'$'}headline_params: String, ${'$'}quick_filter_params: String!) {
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
              pm_pro_shop
              merchant_vouchers
              product {
                id
                name
                price_format
                applinks
                rating_average
                free_ongkir{
                    is_active
                    img_url
                }
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

@GqlQuery("FeedSearchShopFirstPageUseCaseQuery",GQL_QUERY)
 class FeedSearchShopFirstPageUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SearchShopModel>(graphqlRepository)  {

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

        variables[KEY_PARAMS] = CreatePostUrlParamUtils.generateUrlParamString(useCaseRequestParams.parameters)
        variables[KEY_HEADLINE_PARAMS] = createHeadlineParams(useCaseRequestParams.parameters)
        variables[KEY_QUICK_FILTER_PARAMS] = createQuickFilterParams(useCaseRequestParams.parameters)

        return variables
    }

    private fun createHeadlineParams(requestParams: Map<String, Any>): String {
        val headlineParams = HashMap(requestParams)

        headlineParams[TopAdsParams.KEY_EP] = CreatePostSearchConstant.SearchShop.HEADLINE
        headlineParams[TopAdsParams.KEY_TEMPLATE_ID] = CreatePostSearchConstant.SearchShop.HEADLINE_TEMPLATE_VALUE
        headlineParams[TopAdsParams.KEY_ITEM] = CreatePostSearchConstant.SearchShop.HEADLINE_ITEM_VALUE
        headlineParams[TopAdsParams.KEY_SRC] = CreatePostSearchConstant.SearchShop.ADS_SOURCE
        headlineParams[TopAdsParams.KEY_HEADLINE_PRODUCT_COUNT] = CreatePostSearchConstant.SearchShop.HEADLINE_PRODUCT_COUNT

        return CreatePostUrlParamUtils.generateUrlParamString(headlineParams)
    }

    private fun createQuickFilterParams(requestParams: Map<String, Any>): String {
        val quickFilterParams = HashMap(requestParams)

        quickFilterParams[KEY_PAGE_SOURCE] = PAGE_SOURCE_SEARCH_SHOP
        quickFilterParams[KEY_SOURCE] = SOURCE_QUICK_FILTER

        return CreatePostUrlParamUtils.generateUrlParamString(quickFilterParams)
    }

    companion object {
        private const val KEY_QUERY = "query"
        private const val KEY_PARAMS = "params"
        private const val KEY_SOURCE = "source"
        private const val KEY_HEADLINE_PARAMS = "headline_params"
        private const val KEY_QUICK_FILTER_PARAMS = "quick_filter_params"
        private const val KEY_PAGE_SOURCE = "page_source"
        private const val PAGE_SOURCE_SEARCH_SHOP = "search_shop"
        private const val SOURCE_QUICK_FILTER = "quick_filter"

        private const val GQL_QUERY = """
query SearchShop(${'$'}params: String!, ${'$'}headline_params: String, ${'$'}quick_filter_params: String!) {
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
              pm_pro_shop
              merchant_vouchers
              product {
                id
                name
                price_format
                applinks
                rating_average
                free_ongkir{
                    is_active
                    img_url
                }
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