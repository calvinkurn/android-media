package com.tokopedia.search.result.mps.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.mps.domain.model.AceSearchShopMPS
import com.tokopedia.search.utils.UrlParamUtils

@GqlQuery("MPSFirstPageQuery", MPS_GQL_QUERY)
internal fun mpsRequest(requestParams: Map<String?, String>): GraphqlRequest {
    return GraphqlRequest(
        MPSFirstPageQuery(),
        AceSearchShopMPS::class.java,
        mapOf(
            SearchConstant.GQL.KEY_PARAMS to
                UrlParamUtils.generateUrlParamString(requestParams)
        )
    )
}

private const val MPS_GQL_QUERY = """
    query MPSQuery(${'$'}params: String!) {
      ace_search_shop_mps(params: ${'$'}params) {
        header {
          total_data
          treatment_code
        }
        data {
          id
          name
          city
          location
          subtitle
          applink
          image_url
          component_id
          tracking_option
          ticker {
            type
            image_url
            message
          }
          badges {
            title
            image_url
            show
          }
          free_ongkir {
            image_url
            is_active
          }
          products {
            id
            name
            applink
            image_url
            price
            price_format
            original_price
            discount_percentage
            rating_average
            parent_id
            stock
            min_order
            component_id
            tracking_option
            label_groups {
              url
              type
              title
              position
            }
            buttons {
              name
              applink
              text
              is_cta
              component_id
              tracking_option
            }
          }
          buttons {
            name
            applink
            text
            is_cta
            component_id
            tracking_option
          }
        }
      }
    }
"""
