package com.tokopedia.flight.cancellation.data

import com.tokopedia.flight.cancellation.data.CancelEstimateQuery.CANCEL_ESTIMATE
import com.tokopedia.flight.cancellation.data.CancelPassengerQuery.CANCEL_PASSENGER
import com.tokopedia.flight.cancellation.data.CancelRequestQuery.CANCEL_REQUEST
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 06/07/2020
 */

@GqlQuery("QueryCancelPassenger", CANCEL_PASSENGER)
internal object CancelPassengerQuery{
    const val CANCEL_PASSENGER = """
        query cancelPassenger(${'$'}invoiceID:String!){
          flightCancelPassenger(invoiceID:${'$'}invoiceID){
            passengers {
              relationID
              journeyID
              passengerID
              type
              title
              firstName
              lastName
              dob
              nationality
              passportNo
              passportCountry
              passportExpiry
              relations
            }
            nonCancellables {
              relationID
              journeyID
              passengerID
              type
              title
              firstName
              lastName
              dob
              nationality
              passportNo
              passportCountry
              passportExpiry
              relations
              status
              statusStr
            }
            reasons
            included {
              type
              key
              attributes {
                title
                requiredDocs
              }
            }
          }
        }
    """
}

@GqlQuery("QueryCancelRequest", CANCEL_REQUEST)
internal object CancelRequestQuery{
    const val CANCEL_REQUEST = """
        mutation cancelRequest(${'$'}data:FlightCancelRequestArgs) {
          flightCancelRequest(input:${'$'}data) {
            id
            status
            details {
              id
              journeyID
              passengerID
            }
          }
        }
    """
}

@GqlQuery("QueryCancelEstimate", CANCEL_ESTIMATE)
internal object CancelEstimateQuery{
    const val CANCEL_ESTIMATE = """
        mutation estimatedFlight(${'$'}data:FlightEstimatedArgs) {
          flightEstimated(input:${'$'}data) {
            details {
              journeyID
              passengerID
              estimatedRefund
            }
            totalValue
            totalValueNumeric
            showEstimate
            estimationExistPolicy
            estimationNotExistPolicy
            nonRefundableText
          }
        }
    """
}