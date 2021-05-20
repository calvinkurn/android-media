package com.tokopedia.topads.sdk.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse

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
        description
        image {
          full_url
          full_ecs
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
            count_review_format
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
const val PARAMS_QUERY = "displayParams"

@GqlQuery("GetTopadsHeadlineQuery", GET_TOPADS_HEADLINE_QUERY)
class GetTopAdsHeadlineUseCase constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<TopAdsHeadlineResponse>(graphqlRepository) {

    init {
        setTypeClass(TopAdsHeadlineResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsHeadlineQuery.GQL_QUERY)
    }

    fun setParams(params: String) {
        val queryParams = mutableMapOf(
                 PARAMS_QUERY to params
        )
        setRequestParams(queryParams)
    }
}