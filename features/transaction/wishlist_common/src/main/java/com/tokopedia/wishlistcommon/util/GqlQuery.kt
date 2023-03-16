package com.tokopedia.wishlistcommon.util

const val GQL_WISHLIST_V2 = """
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
                }"""

const val GQL_WISHLIST_ADD_V2 = """
         mutation WishlistAddV2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger, ${'$'}collection_sharing: CollectionSharingParams) {
              wishlist_add_v2(productID:${'$'}productID, userID:${'$'}userID, collection_sharing:${'$'}collection_sharing) {
                id
                success
                message
                toaster_color
                button{
                    text
                    action
                    url
                }
                error_type
              }
            }"""

const val GQL_WISHLIST_REMOVE_V2 = """
         mutation WishlistRemoveV2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
              wishlist_remove_v2(productID:${'$'}productID, userID:${'$'}userID) {
                id
                success
                message
                toaster_color
                button {
                    text
                    action
                    url
                }
              }
            }"""
