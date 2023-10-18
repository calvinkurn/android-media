package com.tokopedia.checkout.domain.usecase

const val CHECKOUT_QUERY = """
    mutation checkout(${"$"}carts: CheckoutParams) {
      checkout(carts: ${"$"}carts) {
        header {
          process_time
          messages
          reason
          error_code
        }
        status
        data {
          success
          error
          message
          data {
            redirect_url
            callback_url
            parameter {
              transaction_id
            }
            query_string
            price_validation {
              is_updated
              message {
                title
                desc
                action
              }
              tracker_data {
                product_changes_type
                campaign_type
                product_ids
              }
            }
          }
          prompt {
            type
            title
            description
            buttons {
              text
              link
              color
              action
            }
          }
        }
      }
    }
"""
