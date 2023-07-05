package com.tokopedia.buyerorder.recharge.data

/**
 * @author by furqan on 26/10/2021
 */
class RechargeOrderDetailGQL {
    companion object {
        val ORDER_DETAIL_QUERY = """
            query OMSDETAILS(${'$'}orderCategoryStr: String, ${'$'}upstream:String, ${'$'}orderId: String, ${'$'}detail: Int, ${'$'}action: Int) {
              orderDetails(orderCategoryStr: ${'$'}orderCategoryStr,upstream:${'$'}upstream,orderId: ${'$'}orderId, details: ${'$'}detail, actions: ${'$'}action) {
                purchasedItems {
                  name
                  price
                }
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
                  url
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
                digitalPaymentInfoMessage {
                  message
                  urlText
                  appLink
                  webLink
                }
              }
            }
        """.trimIndent()
    }
}
