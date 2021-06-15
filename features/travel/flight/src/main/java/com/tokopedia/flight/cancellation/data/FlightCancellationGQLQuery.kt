package com.tokopedia.flight.cancellation.data

/**
 * @author by furqan on 06/07/2020
 */
object FlightCancellationGQLQuery {

    val CANCEL_PASSENGER = """
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
    """.trimIndent()

    val CANCEL_REQUEST = """
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
    """.trimIndent()

    val CANCEL_ESTIMATE = """
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
    """.trimIndent()

}