package com.tokopedia.flight.resend_eticket.data

import com.tokopedia.flight.resend_eticket.data.FlightOrderGqlConst.QUERY_RESEND_E_TICKET
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 12/11/2020
 */

@GqlQuery("QueryFlightOrder", QUERY_RESEND_E_TICKET)
internal object FlightOrderGqlConst {
    const val QUERY_RESEND_E_TICKET = """
        query FlightResendEmail(${'$'}invoiceID:String!,${'$'}email:String!) {
          flightResendEmail(invoiceID:${'$'}invoiceID, email:${'$'}email) {
            meta {
              status
            }
          }
        }
    """
}