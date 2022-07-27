package com.tokopedia.wishlistcollection.util

const val GQL_GET_WISHLIST_COLLECTION_ITEMS = """
             query GetWishlistCollectionItems(${'$'}params:GetWishlistCollectionItemsParams) {
                 get_wishlist_collection_items(params:${'$'}params){
                    page
                    limit
                    offset
                    query
                    has_next_page
                    total_data
                    count_removable_items
                    show_empty_state_on_bottomsheet
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