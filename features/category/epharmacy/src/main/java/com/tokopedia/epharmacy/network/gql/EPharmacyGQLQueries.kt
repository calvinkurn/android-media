package com.tokopedia.epharmacy.network.gql

const val GQL_FETCH_ORDER_DETAILS_QUERY = """
    query GetEpharmacyOrderDetails(${'$'}order_id: Int64!) {
    getEpharmacyOrderDetails(order_id: ${'$'}order_id) {
        form {
          shop_id
          shop_name
          shop_type
          shop_location
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
        header {
            error_code
            error_message
        }
      }
    }
"""

const val GQL_FETCH_CHECKOUT_DETAILS_QUERY = """
    query GetPrescriptionsByCheckoutId(${'$'}checkout_id: String!) {
      getPrescriptionsByCheckoutId(checkout_id: ${'$'}checkout_id) {
        checkoutId: checkout_id
        prescriptions {
          prescriptionId: prescription_id
          prescriptionData: prescription_data {
            format
            value
          }
          status
          createdAt: created_at
        }
        productsInfo: products_info {
          shopId: shop_id
          shopName: shop_name
          shopType: shop_type
          shopLocation: shop_location
          products {
              productId: product_id
              name
              quantity
              productImage: product_image
              itemWeight: item_weight
          }
        }
      }
    }
"""
