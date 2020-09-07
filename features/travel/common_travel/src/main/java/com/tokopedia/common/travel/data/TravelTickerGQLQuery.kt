package com.tokopedia.common.travel.data

/**
 * @author by furqan on 03/09/2020
 */
object TravelTickerGQLQuery {
    val TRAVEL_TICKER = """
        query travelTicker(${'$'}did:String!,${'$'}instanceName:String!,${'$'}tickerPage:String!){
            travelTicker(did:${'$'}did,instanceName:${'$'}instanceName,tickerPage:${'$'}tickerPage) {
              Title
              Message
              URL
              Type
              Status
              EndTime
              StartTime
              Instances
              Device
              Page
              IsPeriod
            }
        }
    """.trimIndent()
}