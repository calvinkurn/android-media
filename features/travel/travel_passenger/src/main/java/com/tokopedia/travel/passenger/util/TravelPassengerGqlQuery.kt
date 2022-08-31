package com.tokopedia.travel.passenger.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery.CONTACT_LIST

@GqlQuery("QueryGetContactList", CONTACT_LIST)
object TravelPassengerGqlQuery {
    const val CONTACT_LIST = """
        query travelGetContact (${'$'}product: String!,${'$'}filterType: String!) {
          travelGetContact(product:${'$'}product, filterType:${'$'}filterType) {
            contacts{
              uuid
              type
              titleID
              title
              shortTitle
              firstName
              lastName
              fullName
              gender
              birthDate
              nationality
              phoneCountryCode
              phoneNumber
              email
              idList{
                type
                title
                number
                country
                expiry
              }
            }
          }
        }
    """
}