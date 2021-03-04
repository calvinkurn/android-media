package com.tokopedia.flight.searchV4.data

/**
 * @author by furqan on 03/09/2020
 */
object FlightSearchGQLQuery {
    val SEARCH_SINGLE = """
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
    """.trimIndent()

    val SEARCH_COMBINE = """
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
    """.trimIndent()
}