package com.tokopedia.logisticaddaddress.data.query

object GetDistrictQuery {
    val keroPlacesGetDistrict = """
        query KeroPlacesGetDistrict(${'$'}param: String!) {
          kero_places_get_district(placeid: ${'$'}param) {
            data {
              title
              formatted_address
              district_id
              city_id
              province_id
              district_name
              postal_code
              latitude
              longitude
              full_data {
                long_name
                short_name
                types
              }
            }
          }
        }
    """.trimIndent()
}