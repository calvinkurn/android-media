package com.tokopedia.wishlistcollection.util

    const val GQL_GET_WISHLIST_COLLECTION = """
          query GetWishlistCollections {
              get_wishlist_collections {
                error_message
                status
                data {
                  ticker {
                    title
                    description
                  }
                  is_empty_state
                  empty_wishlist_image_url
                  empty_state {
                    messages {
                      image_url
                      description
                    }
                    buttons {
                      text
                      url
                      action
                      color
                    }
                  }
                  collections {
                    id
                    name
                    total_item
                    item_text
                    images
                    actions {
                      text
                      action
                      url
                    }
                  }
                  placeholder {
                    text
                    image_url
                    action
                  }
                  onboarding_bottomsheet {
                    image_url
                    title
                    description
                    buttons {
                      text
                      url
                      action
                      color
                    }
                  }
                  onboarding_coachmark {
                    skip_button_text
                    details {
                      step
                      title
                      message
                      buttons {
                        text
                        action
                      }
                    }
                  }
                  total_collection
                  max_limit_collection
                  wording_max_limit_collection
                }
              }
            }
            """

    const val GQL_GET_WISHLIST_COLLECTIONS_BOTTOMSHEET = """
             query GetWishlistCollectionsBottomsheet(${'$'}params:GetWishlistCollectionBottomsheetParams){
              get_wishlist_collections_bottomsheet(params:${'$'}params){
                status
                error_message
                data{
                  title
                  title_button{
                text
                    image_url
                    url
                    action
                  }
                  notification
                  placeholder{
                text
                    image_url
                    url
                    action
                  }
                  main_section{
                    text
                    collections{
                      id
                      name
                      total_item
                      item_text
                      label
                      image_url
                      is_contain_product
                    }
                  }
                  additional_section{
                    text
                    collections{
                      id
                      name
                      total_item
                      item_text
                      label
                      image_url
                      is_contain_product
                    }
                  }
                  total_collection
                  max_limit_collection
                  wording_max_limit_collection
                }
              }
            }"""