package com.tokopedia.epharmacy.network.gql

const val GQL_FETCH_ORDER_DETAILS_QUERY = """
    query GetEpharmacyOrderDetails(${'$'}order_id: Int64!) {
    getEpharmacyOrderDetails(order_id: ${'$'}order_id) {
        form {
          shop_id
          shop_name
          shop_type
          shop_location
          shop_logo_url
          products {
            product_id
            name
            quantity
            product_image
            item_weight
          }
          is_reupload_enabled
          prescription_images {
            prescription_id
            status
            prescription_data {
              format
              value
            }
          }
        }
    }
}
"""

const val GQL_POST_PRESCRIPTION_IDS_QUERY: String = """mutation confirmPrescriptionIDs(${'$'}input: EpharmacyConfirmPrescriptionParam!) {
      confirmPrescriptionIDs(input: ${"$"}input) {
        success
      }
    }
"""

const val GQL_FETCH_CHECKOUT_DETAILS_QUERY = """
    query GetEpharmacyCheckoutData(${'$'}checkout_id: String!, ${'$'}source: String!) {
      getEpharmacyCheckoutData(checkout_id: ${'$'}checkout_id, source: ${'$'}source) {
        data {
          checkout_id
          prescription_images {
            status
            prescription_data {
              format
              value
            }
          }
          products_info {
            shop_id
            shop_name
            shop_type
            shop_location
            shop_logo_url
            products {
              product_id
              name
              quantity
              is_ethical_drug
              product_image
              item_weight
            }
          }
        }
      }
    }
"""
val GQL_FETCH_MINI_CONSULTATION_MASTER_QUERY = """
    query getEpharmacyStaticData(${"$"}data_type: String!,${'$'}params: EpharmacyStaticInfoParams!) {
    getEpharmacyStaticData(data_type: ${"$"}data_type, params: ${"$"}params) {
        header {
            error_code
        }
        data {
          steps{
            subtitle
            title
            image_url
          }
          step_title
          info_text
          logo_url
          logo_title
          info_title
        }
    }
}
""".trimIndent()
