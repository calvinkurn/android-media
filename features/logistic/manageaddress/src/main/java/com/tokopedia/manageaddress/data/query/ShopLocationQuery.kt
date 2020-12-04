package com.tokopedia.manageaddress.data.query

object ShopLocationQuery {

    val getShopLocation = """
        query ShopGetAllLocations(${'$'}shop_id: Int!) {
          ShopLocGetAllLocations(
            input: {
                shop_id: ${'$'}shop_id
            }
          ) {
            status
            message
            error {
              id
              description
            }
            data {
              warehouses {
                warehouse_id
                warehouse_name
                warehouse_type
                shop_id {
                  int64
                  valid
                }
                partner_id {
                  int64
                  valid
                }
                address_detail
                postal_code
                latlon
                district_id
                district_name
                city_id
                city_name
                province_id
                province_name
                country
                status
                is_covered_by_couriers
                ticker {
                  text_inactive
                  text_courier_setting
                  link_courier_setting
                }
              }
            }
          }
        }
    """.trimIndent()

    /*ToDo: cari param yg mirip pake array ya*/
    val setShopLocationStatus = """
        mutation shopLocSetStatus(${'$'}inputShopLocSetStatus : [ShopLocParamSetStatus]!) 
        {
          ShopLocSetStatus(input: ${'$'}inputShopLocSetStatus ) {
            status_message
            is_success
          }
        }
    """.trimIndent()
}