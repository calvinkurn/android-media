package com.tokopedia.flight.search.presentation.model

/**
 * @author by furqan on 25/06/2020
 */
class FlightSearchMetaModel(val departureAirport: String,
                            val arrivalAirport: String,
                            val date: String,
                            val isNeedRefresh: Boolean,
                            val refreshTime: Int,
                            val maxRetry: Int,
                            val airlines: List<String>,
                            val searchRequestId: String,
                            val internationalTag: String,
                            val backgroundRefreshTime: Int)