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

    val rechargeBniTapcashQuery="""mutation rechargeUpdateBalanceEmoneyBniTapcash(${'$'}cardData: String!) {
        rechargeUpdateBalanceEmoneyBniTapcash(cardData: ${'$'}cardData) {
            attributes {
                cryptogram
                rrn
                amount
                button_text
                image_issuer
                card_number
            }
            error {
                id
                title
                status
            }
        }
    }""".trimIndent()
}