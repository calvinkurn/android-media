package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionItemsParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionItemsResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionItemsQuery", GetWishlistCollectionItemsUseCase.query)
class GetWishlistCollectionItemsUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<GetWishlistCollectionItemsParams, GetWishlistCollectionItemsResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: GetWishlistCollectionItemsParams): GetWishlistCollectionItemsResponse {
        return repository.request(GetWishlistCollectionItemsQuery(), createVariables(params))
    }

    private fun createVariables(params: GetWishlistCollectionItemsParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        const val query = """
            query GetWishlistCollectionItems(${'$'}params:GetWishlistCollectionItemsParams) {
                 get_wishlist_collection_items(params:${'$'}params){
                    add_wishlist_bulk_config {
                      max_bulk
                      toaster {
                        message
                      }
                    }
                    page
                    limit
                    offset
                    query
                    has_next_page
                    total_data
                    count_removable_items
                    description
                    show_empty_state_on_bottomsheet
                    collection_type
                    empty_state {
                      type
                      messages{
                        title
                        description
                        image_url
                      }
                      buttons {
                        text
                        action
                        url
                      }
                    }
                    ticker {
                        message
                        type
                        button {
                            text
                            action
                        }
                    }
                    storage_cleaner_bottomsheet {
                        title
                        description
                        options {
                            name
                            description
                            action
                        }
                        button {
                            text
                        }
                    }
                    show_delete_progress
                    has_collection
                    setting {
                        buttons {
                            text
                            action
                            url
                        }
                    }
                    header_title
                    sort_filters {
                      id
                      name
                      title
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
                        fulfillment {
                          is_fulfillment
                          text
                        }
                        is_tokonow
                      }
                      badges {
                        title
                        image_url
                      }
                      labels
                      wholesale_price {
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
                        primary_button {
                          text
                          action
                          url
                        }
                        additional_buttons {
                          text
                          action
                          url
                        }
                      }
                      wishlist_id
                      variant_name
                      category {
                        category_id
                        category_name
                      }
                    }
                    error_message
                  }
                }"""
    }
}
