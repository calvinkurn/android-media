package com.tokopedia.wishlist.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import com.tokopedia.wishlist.util.WishlistV2Consts.PARAMS
import javax.inject.Inject

class WishlistV2UseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(param: WishlistV2Params): WishlistV2Response.Data {
        val request = GraphqlRequest(QUERY, WishlistV2Response.Data::class.java, generateParam(param))
        val response = gqlRepository.response(listOf(request))
        return response.getData(WishlistV2Response.Data::class.java)
    }

    private fun generateParam(param: WishlistV2Params): Map<String, Any?> {
        return mapOf(PARAMS to param)
    }

    companion object {
        val QUERY = """
            query WishlistV2(${'$'}params:WishlistV2Params) {
                wishlist_v2(params:${'$'}params) {
                    page
                    limit
                    offset
                    query
                    next_page_url
                    has_next_page
                    total_data
                    empty_state{
                      type
                      messages{
                        title
                        description
                        image_url
                      }
                      button{
                        text
                        action
                        url
                      }
                    }
                    sort_filters {
                      id
                      name
                      text
                      is_active
                      selection_type
                      options{
                        option_id
                        text
                        description
                        is_selected
                      }
                    }
                    items {
                      id
                      name
                      url
                      image_url
                      price
                      price_fmt
                      available
                      label_status
                      preorder
                      rating
                      sold_count
                      min_order
                      shop {
                        id
                        name
                        url
                        location
                        fulfillment{
                          is_fulfillment
                          text
                        }
                        is_tokonow
                      }
                      badges{
                        title
                        image_url
                      }
                      labels
                      wholesale_price{
                        minimum
                        maximum
                        price
                      }
                      default_child_id
                      original_price
                      original_price_fmt
                      discount_percentage
                      discount_percentage_fmt
                      label_stock
                      bebas_ongkir {
                        type
                        title
                        image_url
                      }
                      label_group{
                        position
                        title
                        type
                        url
                      }
                      buttons{
                        primary_button{
                          text
                          action
                          url
                        }
                        additional_buttons{
                          text
                          action
                          url
                        }
                      }
                      wishlist_id
                      variant_name
                      category{
                        category_id
                        category_name
                      }
                    }
                    error_message
                  }
                }
        """.trimIndent()
    }
}