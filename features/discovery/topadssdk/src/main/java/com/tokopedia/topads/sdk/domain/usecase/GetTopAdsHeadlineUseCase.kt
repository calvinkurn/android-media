package com.tokopedia.topads.sdk.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.sdk.domain.TopAdsParams.Companion.DEFAULT_KEY_SRC
import com.tokopedia.topads.sdk.domain.TopAdsParams.Companion.KEY_SRC
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.utils.*
import javax.inject.Inject

const val GET_TOPADS_HEADLINE_QUERY: String = """query TopadsCPMHeadlineQuery(${'$'}displayParams: String!) {
  displayAdsV3(displayParams: ${'$'}displayParams) {
    status {
      error_code
      message
    }
    header {
      process_time
      total_data
    }
    data {
      ad_click_url
      ad_ref_key
      id
      redirect
      applinks
      headline {
        promoted_text
        name
        button_text
        layout
        position
        widget_image_url 
        description,
        widget_title
        image {
          full_url
          full_ecs
        }
        flash_sale_campaign_detail{
          start_time
          end_time
          campaign_type
        }
        shop {
          id
          name
          tagline
          slogan
          location
          city
          domain
          is_followed
          gold_shop
          shop_is_official
          pm_pro_shop
          merchant_vouchers
          product {
            id
            name
            applinks
            image {
              m_url
            }
            image_product {
              image_url
              image_click_url
            }
            price_format
            product_rating
            uri
            count_review_format
            label_group {
              title
              position
              type
              url
            }
            free_ongkir {
              is_active
              img_url
            }
            campaign {
               original_price
               discount_percentage
            }
          }
          image_shop {
            xs_url
          }
        }
        badges {
          image_url
          title
          show
        }
        template_id
      }
    }
  }
}
"""
private const val PARAMS_QUERY = "displayParams"
private const val KEY_SEEN_ADS = "seen_ads"

@GqlQuery("GetTopadsHeadlineQuery", GET_TOPADS_HEADLINE_QUERY)
class GetTopAdsHeadlineUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TopAdsHeadlineResponse>(graphqlRepository) {

    init {
        setTypeClass(TopAdsHeadlineResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsHeadlineQuery.GQL_QUERY)
    }

    fun setParams(params: String, addressData: Map<String,String>) {
        val reqParam = appendAddressParams(params, addressData)
        val queryParams = mutableMapOf(
            PARAMS_QUERY to reqParam
        )
        setRequestParams(queryParams)
    }


    private fun appendAddressParams(
        params : String,
        addressData: Map<String,String>
    ): String {
        return if (addressData.isEmpty()) params
        else{
            val map = params.split("&").associate {
                val (left, right) = it.split("=")
                left to right
            }

            val newReqParam = map.toMutableMap()

            if (map[KEY_SRC] != DEFAULT_KEY_SRC ){
                newReqParam.putAll(addressData)
            }
            newReqParam.entries.joinToString("&")
        }
    }

    fun createParams(
        userId: String,
        page: String,
        ep: String = "headline",
        src: String,
        templateId: String,
        headlineProductCount: String,
        item: String,
        device: String = "android",
        seenAds: String?
    ): String {
        val map = mutableMapOf(
            PARAM_USER_ID to userId,
            PARAM_PAGE to page,
            PARAM_EP to ep,
            PARAM_SRC to src,
            PARAM_TEMPLATE_ID to templateId,
            PARAM_HEADLINE_PRODUCT_COUNT to headlineProductCount,
            PARAM_ITEM to item,
            PARAM_DEVICE to device,
            KEY_SEEN_ADS to (seenAds ?: "")
        )

        seenAds?.let { map.remove(KEY_SEEN_ADS) }

        return map.entries.joinToString("&")

    }
}
