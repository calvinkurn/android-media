package com.tokopedia.hotel.common.util

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.hotel.common.util.HotelAddToCartMutation.ADD_TO_CART
import com.tokopedia.hotel.common.util.HotelCheckoutMutation.CHECKOUT
import com.tokopedia.hotel.common.util.HotelDeleteRecentSearchMutation.DELETE_RECENT_SEARCH
import com.tokopedia.hotel.common.util.HotelDeleteRecentSearchUUIDMutation.DELETE_RECENT_SEARCH_UUID
import com.tokopedia.hotel.common.util.HotelSharePdfNotificationMutation.SHARE_PDF_NOTIFICATION
import com.tokopedia.hotel.common.util.HotelSubmitCancellationMutation.SUBMIT_CANCELLATION

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

    fun getSubmitCancellationQuery() = """
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

    fun getCancellationQuery() = """
        query getCancellation(${'$'}data: PropertyGetCancellationRequest!){
          propertyGetCancellation(input: ${'$'}data        ){
            data {
                cancelCartID
                cancelCartExpiry
                property {
                    propertyID
                    name
                    type
                    address
                    propertyImage {
                        urlSquare60
                        urlOriginal
                        urlMax300
                    }
                    star
                    paymentNote
                    checkInOut{
                        title
                        checkInOut {
                          day
                          date
                          time
                        }
                    }
                    stayLength
                    isDirectPayment
                    room {
                        isBreakFastIncluded
                        maxOccupancy
                        roomName
                        roomContent
                        isRefundAble
                        isCCRequired
                    }
                }
                cancelPolicy {
                    title
                    policy {
                        title
                        desc
                        active
                        feeInLocalCurrency {
                            amountStr
                            amount
                            currency
                        }
                        fee {
                            amountStr
                            amount
                            currency
                        }
                        styling
                    }
                }
                cancelInfo {
                    desc
                    isClickable
                    longDesc {
                        title
                        desc
                    }
                }
                payment {
                    title
                    detail {
                        title
                        amount
                    }
                    summary {
                        title
                        amount
                    }
                    footer {
                        desc
                        links
                    }
                }
                reasons {
                    id
                    title
                    freeText
                }
                footer{
                    desc
                    links
                }
                confirmationButton {
                    title
                    desc
                }
          }
            meta{
              invoiceID
            }
            content {
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
          }
        }
    """
}

@GqlQuery("MutationDeleteRecentSearchUUID", DELETE_RECENT_SEARCH_UUID)
internal object HotelDeleteRecentSearchUUIDMutation{
    const val DELETE_RECENT_SEARCH_UUID = """
        mutation (${'$'}id: Int!,${'$'}uuid: String!) {
          travelRecentSearchDelete(userID:${'$'}id,dataType:HOTEL,UUID:${'$'}uuid){
            Result
          }
        }
    """
}

@GqlQuery("MutationDeleteRecentSearch", DELETE_RECENT_SEARCH)
internal object HotelDeleteRecentSearchMutation{
    const val DELETE_RECENT_SEARCH = """
        mutation {
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