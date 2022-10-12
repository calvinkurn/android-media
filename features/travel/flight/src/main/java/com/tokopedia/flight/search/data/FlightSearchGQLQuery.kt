package com.tokopedia.flight.search.data

import com.tokopedia.flight.search.data.FlightSearchSingleQuery.QUERY_SINGLE
import com.tokopedia.flight.search.data.FlightSearchCombineQuery.QUERY_COMBINE
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 03/09/2020
 */

@GqlQuery("QueryFlightSearchSingle", QUERY_SINGLE)
internal object FlightSearchSingleQuery{
    const val QUERY_SINGLE = """
        query Searching(${'$'}data:SearchSingleArgs) {
          flightSearch(input:${'$'}data) {
            data {
              id
              term
              hasFreeRapidTest
              isSeatDistancing
              departureAirportID
              departureTime
              departureTimeInt
              arrivalAirportID
              arrivalTime
              arrivalTimeInt
              totalTransit
              addDayArrival
              totalStop
              duration
              durationMinute
              durationLong
              total
              totalNumeric
              beforeTotal
              showSpecialPriceTag
              routes {
                airlineID
                departureAirportID
                departureTime
                arrivalAirportID
                arrivalTime
                duration
                layover
                flightNumber
                refundable
                stop
                operatingAirlineID
                amenities {
                  icon
                  label
                }
                stopDetail {
                  code
                  city
                }
                infos {
                  label
                  value
                }
              }
              fare {
                adult
                child
                infant
                adultNumeric
                childNumeric
                infantNumeric
              }
              label1
              label2
            }
            error {
              id
              status
              title
            }
            included {
              type
              id
              attributes {
                name
                shortName
                logo
                city
              }
            }
            meta {
              needRefresh
              refreshTime
              maxRetry
              adult
              child
              infant
              requestID
              internationalTransitTag
              backgroundRefreshTime
            }
          }
        }
    """
}

@GqlQuery("QueryFlightSearchCombine", QUERY_COMBINE)
internal object FlightSearchCombineQuery{
    const val QUERY_COMBINE = """
        query searchCombineV3(${'$'}data: SearchCombineArgs!) {
          flightSearchCombineV3(input: ${'$'}data) {
            data {
              journeys {
                journeyID
              }
              combos {
                fares {
                  adultPrice
                  childPrice
                  infantPrice
                  adultPriceNumeric
                  childPriceNumeric
                  infantPriceNumeric
                }
                combination
                comboKey
                isBestPairing
                total
                totalNumeric
              }
            }
            meta {
              needRefresh
              refreshTime
              maxRetry
              adult
              child
              infant
              requestID
            }
            error {
              id
              status
              title
            }
          }
        }
    """
}