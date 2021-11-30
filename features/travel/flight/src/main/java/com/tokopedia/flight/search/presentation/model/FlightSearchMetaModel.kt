package com.tokopedia.flight.search.presentation.model

/**
 * @author by furqan on 25/06/2020
 */
class FlightSearchMetaModel(val departureAirport: String = "",
                            val arrivalAirport: String = "",
                            val date: String = "",
                            val isNeedRefresh: Boolean = false,
                            val refreshTime: Int = 0,
                            val maxRetry: Int = 0,
                            val airlines: List<String> = listOf(),
                            val searchRequestId: String = "",
                            val internationalTag: String = "",
                            val backgroundRefreshTime: Int = 0)