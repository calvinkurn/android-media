package com.tokopedia.travel.country_code.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.travel.country_code.util.TravelCountryCodeGqlQuery.ALL_COUNTRY

@GqlQuery("QueryTravelCountryCode", ALL_COUNTRY)
object TravelCountryCodeGqlQuery {
    const val ALL_COUNTRY = """
        query {
          TravelGetAllCountries {
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
    """
}