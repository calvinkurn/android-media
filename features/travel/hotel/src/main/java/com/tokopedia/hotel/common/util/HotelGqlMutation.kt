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
          }
        }
    """
}