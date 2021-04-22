package com.tokopedia.hotel.common.util

object HotelGqlMutation {
    val DELETE_RECENT_SEARCH_UUID = """
        mutation (${'$'}id: Int!,${'$'}uuid: String!) {
          travelRecentSearchDelete(userID:${'$'}id,dataType:HOTEL,UUID:${'$'}uuid){
            Result
          }
        }
    """.trimIndent()

    val DELETE_RECENT_SEARCH = """
        mutation {
            travelRecentSearchDelete(
              dataType : HOTEL
            ){
            Result
          }
        }
    """.trimIndent()

    val SHARE_PDF_NOTIFICATION = """
        mutation sendNotification (${'$'}data: propertySendNotificationArgs!) {
          propertySendNotification(input:${'$'}data){
            Success
          }
        }
    """.trimIndent()

    val ADD_TO_CART = """
        mutation PropertyAddToCart(${'$'}data: PropertyAddtoCartArgs!) {
            propertyAddToCart(input:${'$'}data) {
                cartID
            }
        }
    """.trimIndent()

    val CHECKOUT = """
        mutation PropertyCheckout(${'$'}data: PropertyCheckoutArgs!) {
            propertyCheckout(input:${'$'}data) {
                queryString
                redirectURL
            }
        }
    """.trimIndent()
}