package com.tokopedia.common.travel.ticker

/**
 * @author by furqan on 18/02/19
 */

class TravelTickerInstanceId {
    companion object {
        val FLIGHT = "FLIGHT"
        val KAI = "TRAIN"
        val HOTEL = "HOTEL"
    }
}

class TravelTickerFlightPage {
    companion object {
        val HOME = "home"
        val SEARCH = "search"
        val BOOK = "book"
        val SUMMARY = "summary"
    }
}

class TravelTickerKAIPage {
    companion object {
        val HOME = "home"
        val SEARCH = "search"
        val BOOK = "book"
        val SEAT = "seat"
        val SUMMARY = "summary"
    }
}

class TravelTickerHotelPage {
    companion object {
        val HOME = "home"
        val SEARCH_LIST = "searchList"
        val SEARCH_DETAIL = "searchDetail"
        val BOOK = "book"
    }
}