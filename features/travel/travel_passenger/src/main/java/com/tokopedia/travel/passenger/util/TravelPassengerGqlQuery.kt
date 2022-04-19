package com.tokopedia.travel.passenger.util

object TravelPassengerGqlQuery {
    val CONTACT_LIST = """
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
    """.trimIndent()
}