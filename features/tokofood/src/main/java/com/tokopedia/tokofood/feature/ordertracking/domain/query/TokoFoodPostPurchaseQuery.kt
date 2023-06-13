package com.tokopedia.tokofood.feature.ordertracking.domain.query


const val TOKO_FOOD_ORDER_STATUS_QUERY = """
            query GetTokofoodOrderStatus(${'$'}orderID : String!) {
              tokofoodOrderDetail(orderID: ${'$'}orderID) {
                orderStatus {
                  status
                  title
                  subtitle
                  iconName
                  enableChatButton
                }
                additionalTickerInfo {
                  level
                  appText
                }
                eta {
                  label
                  time
                }
                driverDetails {
                  name
                  photoUrl
                  licensePlateNumber
                  karma {
                    icon
                    message
                  }
                }
                invoice {
                  invoiceNumber
                  gofoodOrderNumber
                }
                payment {
                  paymentDate
                }
              }
            }
    """

const val TOKO_FOOD_ORDER_DETAIL_QUERY = """
        query GetTokofoodOrderDetail(${'$'}orderID : String!) {
          tokofoodOrderDetail(orderID: ${'$'}orderID) {
             orderStatus {
              status
              title
              subtitle
              iconName
              enableChatButton
            }
            eta {
              label
              time
            }
            merchant {
              merchantId
              displayName
              distanceInKm
            }
            destination {
              label
              phone
              info
            }
            items {
              cartId
              categoryId
              categoryName
              itemId
              displayName
              price
              quantity
              variants {
                displayName
                optionName
              }
              notes
            }
            additionalTickerInfo {
              level
              appText
            }
            actionButtons {
              label
              actionType
              appUrl
            }
            dotMenus {
              label
              actionType
              appUrl
            }
            driverDetails {
              name
              photoUrl
              licensePlateNumber
              karma {
                icon
                message
              }
            }
            invoice {
              invoiceNumber
              invoiceURL
              gofoodOrderNumber
            }
            payment {
              paymentMethod {
                label
                value
              }
              paymentDetails {
                label
                value
              }
              paymentAmount {
                label
                value
              }
              paymentDate
            }
          }
        }
    """

const val DRIVER_PHONE_NUMBER_QUERY = """
        query GetDriverPhoneNumber(${'$'}orderID: String!) {
          tokofoodDriverPhoneNumber(orderID: ${'$'}orderID) {
            isCallable
            phoneNumber
          }
        }
    """
