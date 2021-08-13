package com.tokopedia.common.travel.data

/**
 * @author by furqan on 03/09/2020
 */
object TravelTickerGQLQuery {
    val TRAVEL_TICKER = """
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
    """.trimIndent()
}