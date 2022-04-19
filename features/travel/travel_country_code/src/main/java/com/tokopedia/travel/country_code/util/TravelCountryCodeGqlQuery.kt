package com.tokopedia.travel.country_code.util

object TravelCountryCodeGqlQuery {
    val ALL_COUNTRY = """
        query {
          TravelGetAllCountries() {
            countries {
              id
              attributes {
                name
                currency
                phoneCode
              }
            }
          }
        }
    """.trimIndent()
}