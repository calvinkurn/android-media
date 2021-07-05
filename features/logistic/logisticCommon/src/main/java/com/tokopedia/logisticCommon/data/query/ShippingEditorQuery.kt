package com.tokopedia.logisticCommon.data.query

object ShippingEditorQuery {

    val ongkirShippingEditor = """
        query ongkirShippingEditor (${'$'}input: ShippingEditorShopMultiLocInput!){
          ongkirShippingEditor(input: ${'$'}input) {
            status
            message
            data {
              shippers {
                ondemand {
                  shipper_id
                  shipper_name
                  is_active
                  text_promo
                  image
                  feature_info {
                    header
                    body
                  }
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    is_active
                  }
                }
                conventional {
                  shipper_id
                  shipper_name
                  is_active
                  text_promo
                  image
                  feature_info {
                    header
                    body
                  }
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    is_active
                  }
                }
              }
              ticker {
                header
                body
                text_link
                url_link
              }
            }
          }
        }
    """.trimIndent()

    val ongkirShippingEditorTicker = """
        query ongkirShippingEditorGetShipperTicker (${'$'}input: ShippingEditorShopMultiLocInput!){
          ongkirShippingEditorGetShipperTicker(input: ${'$'}input) {
            status
            message
            data {
            	header_ticker {
            	  header
            	  body
            	  text_link
            	  url_link
            	  is_active
                  warehouse_ids
            	}
              courier_ticker {
                shipper_id
                warehouse_ids
                ticker_state
                is_available
                shipper_product {
                  shipper_product_id
                  is_available
                }
              }
              warehouses {
                warehouse_id
                warehouse_name
                district_id
                district_name
                city_id
                city_name
                province_id
                province_name
                status
                postal_code
                is_default
                latlon
                latitude
                longitude
                address_detail
                country
                is_fulfillment
                warehouse_type
                email
                shop_id {
                  int64
                  valid
                }
                partner_id {
                  int64
                  valid
                }
              }
            }
          }
        }
    """.trimIndent()


    val getShipperDetails = """
        query ongkirShippingEditorGetShipperDetail {
          ongkirShippingEditorGetShipperDetail() {
            status
           	message
            data {
              shipper_details {
                name
                description
                image
                shipper_product {
                  name
                  description
                }
              }
              feature_details {
        		header
                description
              }
              service_details {
              	header
                description
              }
            }
          }
        }
    """.trimIndent()

    val ongkirShippingEditorPopup = """
        query ongkirShippingEditorPoup (${'$'}input: OngkirShippingEditorPopupInput!) {
          ongkirShippingEditorPopup (input:${'$'}input) {
            data {
              ui_content {
                warehouses {
                 	warehouse_id
                  shop_id {
                    int64
                    valid
                  }
                  warehouse_name
                  district_id
                  district_name
                  city_id
                  city_name
                  province_id
                  province_name
                  status
                  postal_code
                  is_default
                  latlon
                  latitude
                  longitude
                  address_detail
                  country
                  is_fulfillment
                  warehouse_type
                }
                warehouse_ids
                header
                header_location
                body
                ticker {
                  text_link
                  body
                  header
                  url_link
                }
              }
              state
              feature_id
            }
            status
            message
          }
        }
    """.trimIndent()

    val saveShippingEditor = """
        mutation OngkirShippingEditorSave(${'$'}input : OngkirShippingEditorSaveInput!)
        {
          ongkirShippingEditorSave(input:${'$'}input) {
            status
            message
            data {
              message
              is_success
            }
            errors {
              id
              status
              title
            }
          }
        }
    """.trimIndent()
}