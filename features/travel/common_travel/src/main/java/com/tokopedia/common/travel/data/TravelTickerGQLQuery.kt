package com.tokopedia.common.travel.data

import com.tokopedia.common.travel.data.TravelTickerGQLQuery.TRAVEL_TICKER
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * @author by furqan on 03/09/2020
 */
@GqlQuery("QueryTravelTicker", TRAVEL_TICKER)
object TravelTickerGQLQuery {
    const val TRAVEL_TICKER = """
        query TravelGetTicker(${'$'}tickerRequest: TravelTickerRequest!) {
          TravelGetTicker(input: ${'$'}tickerRequest)  {
           title
            message
            url
            Type
            Status
            EndTime
            StartTime
            Instance
            Device
            Page
            IsPeriod
          }
        }
    """
}