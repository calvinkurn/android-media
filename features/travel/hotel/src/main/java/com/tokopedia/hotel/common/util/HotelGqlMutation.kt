package com.tokopedia.hotel.common.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.hotel.common.util.HotelAddToCartMutation.ADD_TO_CART
import com.tokopedia.hotel.common.util.HotelCheckoutMutation.CHECKOUT
import com.tokopedia.hotel.common.util.HotelDeleteRecentSearchMutation.DELETE_RECENT_SEARCH
import com.tokopedia.hotel.common.util.HotelDeleteRecentSearchUUIDMutation.DELETE_RECENT_SEARCH_UUID
import com.tokopedia.hotel.common.util.HotelSharePdfNotificationMutation.SHARE_PDF_NOTIFICATION
import com.tokopedia.hotel.common.util.HotelSubmitCancellationMutation.SUBMIT_CANCELLATION

@GqlQuery("MutationDeleteRecentSearchUUID", DELETE_RECENT_SEARCH_UUID)
internal object HotelDeleteRecentSearchUUIDMutation{
    const val DELETE_RECENT_SEARCH_UUID = """
        mutation travelRecentSearchDelete (${'$'}id: Int!,${'$'}uuid: String!) {
          travelRecentSearchDelete(userID:${'$'}id,dataType:HOTEL,UUID:${'$'}uuid){
            Result
          }
        }
    """
}

@GqlQuery("MutationDeleteRecentSearch", DELETE_RECENT_SEARCH)
internal object HotelDeleteRecentSearchMutation{
    const val DELETE_RECENT_SEARCH = """
        mutation travelRecentSearchDelete() {
            travelRecentSearchDelete(
              dataType : HOTEL
            ){
            Result
          }
        }
    """
}

@GqlQuery("MutationSharePdfNotification", SHARE_PDF_NOTIFICATION)
internal object HotelSharePdfNotificationMutation{
    const val SHARE_PDF_NOTIFICATION = """
        mutation sendNotification (${'$'}data: propertySendNotificationArgs!) {
          propertySendNotification(input:${'$'}data){
            Success
          }
        }
    """
}

@GqlQuery("MutationAddToCart", ADD_TO_CART)
internal object HotelAddToCartMutation{
    const val ADD_TO_CART = """
        mutation PropertyAddToCart(${'$'}data: PropertyAddtoCartArgs!) {
            propertyAddToCart(input:${'$'}data) {
                cartID
            }
        }
    """
}

@GqlQuery("MutationHotelCheckout", CHECKOUT)
internal object HotelCheckoutMutation{
    const val CHECKOUT = """
        mutation PropertyCheckout(${'$'}data: PropertyCheckoutArgs!) {
            propertyCheckout(input:${'$'}data) {
                queryString
                redirectURL
            }
        }
    """
}

@GqlQuery("MutationHotelSubmitCancellation", SUBMIT_CANCELLATION)
internal object HotelSubmitCancellationMutation{
    const val SUBMIT_CANCELLATION = """
        mutation hotelSubmitCancelRequest (${'$'}data: cancelRequestArgs!) {
            hotelSubmitCancelRequest(input:${'$'}data){
                data {
                    success
                    title
                    desc
                    actionButton {
                        label
                        buttonType
                        URI
                        URIWeb

                    }
                }
                meta {
                    cancelCartID
                    selectedReason {
                        id
                        title
                        freeText
                    }
                }
            }
        }
    """
}
