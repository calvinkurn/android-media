package com.tokopedia.buyerorder.detail.domain.queries

import com.tokopedia.buyerorder.detail.domain.queries.OmsDetailsQuery.QUERY
import com.tokopedia.buyerorder.detail.domain.queries.OmsDetailsQuery.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * created by @bayazidnasir on 19/8/2022
 */
@GqlQuery(QUERY_NAME, QUERY)
internal object OmsDetailsQuery {
    const val QUERY_NAME = "QueryOmsDetails"
    const val QUERY = """
        query OMSDETAILS(${"$"}orderCategoryStr: String, ${"$"}upstream:String, ${"$"}orderId: String, ${"$"}detail: Int, ${"$"}action: Int) {
          orderDetails(orderCategoryStr: ${"$"}orderCategoryStr,upstream:${"$"}upstream,orderId: ${"$"}orderId, details: ${"$"}detail, actions: ${"$"}action) {
            status {
              status
              statusText
              statusLabel
              iconUrl
              textColor
              backgroundColor
              fontSize
            }
            title {
              label
              value
              textColor
              backgroundColor
              imageUrl
            }
            metadata
            invoice {
              invoiceRefNum
              invoiceUrl
            }
            items {
              categoryID
              id
              imageUrl
              category
              title
              quantity
              price
              trackingURL
              trackingNumber
              invoiceID
              promotionAmount
              metaData
              actionButtons {
                label
                buttonType
                uri
                mappingUri
                weight
                key
                method
                uriWeb
                value
                name
                control
                header
                color{
                  textColor
                  border
                  background
                }
                body {
                  appURL
                  webURL
                  method
                  body
                }
              }
              tapActions {
                label
                weight
                control
                header
                buttonType
                value
                method
                key
                name
                header
                color {
                  textColor
                  border
                  background
                }
                body {
                  appURL
                  method
                  body
                }
              }
            }
            pricing {
              label
              value
              textColor
              backgroundColor
              imageUrl
            }
            detail {
              label
              value
            }
            paymentData {
              label
              value
              textColor
              backgroundColor
              imageUrl
            }
            actionButtons {
              label
              buttonType
              uri
              mappingUri
              weight
              key
              method
              uriWeb
              value
              name
              control
              header
              color {
                textColor
                border
                background
              }
              body {
                appURL
                webURL
                method
                body
              }
            }
            conditionalInfo {
              text
              color {
                border
                background
              }
            }
            contactUs {
              helpText
              helpUrl
            }
            paymentMethod {
              label
              value
            }
            payMethod{
              label
              value
            }
            beforeOrderId
            resolutionId
            driverDetails {
              name
              phone
              photo
              licenseNumber
              trackingURL
            }
            promoCode
            additionalInfo {
              label
              value
              imageUrl
              textColor
              backgroundColor
            }
            additionalTickerInfo {
              title
              notes
              urlDetail
              urlText
            }
          }
        }
    """
}