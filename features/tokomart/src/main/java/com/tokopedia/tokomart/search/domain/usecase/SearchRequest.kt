package com.tokopedia.tokomart.search.domain.usecase

import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel

internal fun createAceSearchProductGraphqlRequest(params: String) = GraphqlRequest(
        ACE_SEARCH_PRODUCT_QUERY,
        AceSearchProductModel::class.java,
        mapOf(KEY_PARAMS to params)
)

private const val ACE_SEARCH_PRODUCT_QUERY = """
    query aceSearchProductV4(${'$'}params: String!) {
      ace_search_product_v4(params: ${'$'}params){
        header {
          totalData
          totalDataText
        }
        data {
          products {
            id
            imageUrl300
            name
            price
            priceInt
            discountPercentage
            originalPrice
            labelGroups {
              url
              title
              type
              position
            }
            labelGroupVariant {
              title
              type
              type_variant
              hex_color
            }
          }
        }
      }
    }
"""