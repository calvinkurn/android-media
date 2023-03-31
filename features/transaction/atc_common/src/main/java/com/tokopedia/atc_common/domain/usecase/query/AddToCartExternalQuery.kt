package com.tokopedia.atc_common.domain.usecase.query

const val ADD_TO_CART_EXTERNAL_QUERY = """
    mutation add_to_cart_external_v2(${'$'}productID: SuperInteger, ${'$'}chosen_address: ChosenAddressParam) {
      add_to_cart_external_v2(productID: ${'$'}productID, chosen_address: ${'$'}chosen_address) {
        error_message
        status
        data {
            message
            success
            data {
                product_id
                product_name
                quantity
                price
                category
                shop_id
                shop_type
                shop_name
                picture
                url
                cart_id
                brand
                category_id
                variant
                tracker_attribution
                is_multi_origin
                is_free_ongkir
                is_free_ongkir_extra
            }
        }
    }
}
"""
