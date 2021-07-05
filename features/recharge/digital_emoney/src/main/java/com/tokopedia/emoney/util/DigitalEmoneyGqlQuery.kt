package com.tokopedia.emoney.util

object DigitalEmoneyGqlQuery {
    val rechargeEmoneyInquiryBalance = """
        query rechargeEmoneyInquiryBalance(${'$'}type: RechargeEmoneyRequestType!,${'$'}id: Int!,${'$'}attributes: RechargeEmoneyAttributesInput!) {
          rechargeEmoneyInquiry(type:${'$'}type, id:${'$'}id, attributes:${'$'}attributes) {
            id
            type
            attributes{
              button_text
              card_number
              image_issuer
              last_balance
              payload
              status
            }
            error {
              id
              title
              status
            }
          }
        }
    """.trimIndent()
}