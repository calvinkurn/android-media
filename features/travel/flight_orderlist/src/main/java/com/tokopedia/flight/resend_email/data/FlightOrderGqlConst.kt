package com.tokopedia.flight.resend_email.data

/**
 * @author by furqan on 12/11/2020
 */
object FlightOrderGqlConst {
    val QUERY_RESEND_E_TICKET = """
        query FlightResendEmail(${'$'}invoiceID:String!,${'$'}email:String!) {
          flightResendEmail(invoiceID:${'$'}invoiceID, email:${'$'}email) {
            meta {
              status
            }
          }
        }
    """.trimIndent()
}