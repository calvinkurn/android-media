package com.tokopedia.flight.dummy

import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.searchV4.data.cloud.single.Route
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel
import com.tokopedia.flight.searchV4.presentation.model.FlightFareModel
import com.tokopedia.flight.searchV4.presentation.model.FlightJourneyModel
import com.tokopedia.flight.searchV4.presentation.model.FlightSearchMetaModel
import com.tokopedia.flight.searchV4.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.searchV4.presentation.model.filter.TransitEnum
import com.tokopedia.flight.searchV4.presentation.model.statistics.*

/**
 * @author by furqan on 13/05/2020
 */
val SEARCH_TICKER_DATA = TravelTickerModel(
        "Dummy Ticker Title",
        "Dummy Ticker Message",
        "www.dummyurl.com",
        1,
        1,
        "",
        "",
        1,
        "Dummy Page",
        false
)

val SEARCH_STATISTICS_DATA = FlightSearchStatisticModel(
        100000,
        10000000,
        100,
        50000,
        arrayListOf(TransitStat(TransitEnum.DIRECT, 2000000, "Rp2.000.000"),
                TransitStat(TransitEnum.ONE, 1500000, "Rp1.500.000")),
        arrayListOf(AirlineStat(FlightAirlineModel("GA", "Garuda Indonesia", "Garuda", "www.dummylogo.com"), 2500000, "Rp2.500.000"),
                AirlineStat(FlightAirlineModel("BTK", "Batik Indonesia", "Batik", "www.dummylogo.com"), 2200000, "Rp2.200.000")),
        arrayListOf(DepartureStat(DepartureTimeEnum._00, 1000000, "Rp1.000.000"),
                DepartureStat(DepartureTimeEnum._06, 1500000, "Rp1.500.000"),
                DepartureStat(DepartureTimeEnum._12, 2000000, "Rp2.000.000"),
                DepartureStat(DepartureTimeEnum._18, 2500000, "Rp2.500.000")),
        arrayListOf(DepartureStat(DepartureTimeEnum._00, 1000000, "Rp1.000.000"),
                DepartureStat(DepartureTimeEnum._06, 1500000, "Rp1.500.000"),
                DepartureStat(DepartureTimeEnum._12, 2000000, "Rp2.000.000"),
                DepartureStat(DepartureTimeEnum._18, 2500000, "Rp2.500.000")),
        arrayListOf(RefundableStat(RefundableEnum.REFUNDABLE, 1000000, "Rp1.000.000")),
        true, true, true)

val JOURNEY_LIST_DATA = arrayListOf(
        FlightJourneyModel(
                "", "DummyId", "CGK",
                "Bandara International Soekarno Hatta", "",
                "10.00", 111111, "BTJ", "12.40",
                "Bandara International Sultan Iskandar Muda", "",
                212121, 0, 0, "2j 50m", 123123,
                "Rp1.500.000", 1500000, "", 0,
                false, "", false, RefundableEnum.NOT_REFUNDABLE,
                false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
                arrayListOf(Route("GA", "CGK", "", "BTJ", "", "2j 50m",
                        "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                        "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
                arrayListOf(), "", ""
        ),
        FlightJourneyModel(
                "", "DummyId", "CGK",
                "Bandara International Soekarno Hatta", "",
                "10.00", 111111, "BTJ", "12.40",
                "Bandara International Sultan Iskandar Muda", "",
                212121, 1, 1, "26j 50m", 123123,
                "Rp1.500.000", 1500000, "Rp1.000.000", 1000000,
                false, "", false, RefundableEnum.REFUNDABLE,
                false, FlightFareModel("Rp1.500.000", "Rp1.000.000", "", "", "", "", 1500000, 1000000, 0, 0, 0, 0),
                arrayListOf(Route("GA", "CGK", "", "BTJ", "", "2j 50m",
                        "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                        "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
                arrayListOf(), "", ""
        )
)

val META_MODEL_NEED_REFRESH = FlightSearchMetaModel(
        "CGK", "BTJ", "2020-11-11", true, -1, 11,
        arrayListOf("GA", "QA"), "asdasd", "", 0)

val META_MODEL_NOT_NEED_REFRESH = FlightSearchMetaModel(
        "CGK", "BTJ", "2020-11-11", false, -1, 11,
        arrayListOf("QA", "QC"), "asdasd", "", 1800)

val META_MODEL_NEED_REFRESH_MAX_RETRY = FlightSearchMetaModel(
        "CGK", "BTJ", "2020-11-11", true, -1, 11,
        arrayListOf("GA", "QA"), "asdasd", "", 0)

val DEPARTURE_JOURNEY = FlightJourneyModel(
        "", "DummyId", "CGK",
        "Bandara International Soekarno Hatta", "",
        "10.00", 111111, "BTJ", "12.40",
        "Bandara International Sultan Iskandar Muda", "",
        212121, 0, 0, "2j 50m", 123123,
        "Rp1.500.000", 1500000, "", 0,
        false, "", false, RefundableEnum.NOT_REFUNDABLE,
        false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
        arrayListOf(Route("GA", "CGK", "2020-08-11T07:50:00Z", "BTJ", "2020-08-11T08:55:00Z", "2j 50m",
                "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
        arrayListOf(), "", ""
)

val VALID_RETURN_JOURNEY = FlightJourneyModel(
        "", "DummyId", "BTJ",
        "Bandara International Sultan Iskandar Muda", "",
        "10.00", 111111, "CGK", "12.40",
        "Bandara International Soekarno Hatta", "",
        212121, 0, 0, "2j 50m", 123123,
        "Rp1.500.000", 1500000, "", 0,
        false, "", false, RefundableEnum.NOT_REFUNDABLE,
        false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
        arrayListOf(Route("GA", "CGK", "2020-12-12T07:50:00Z", "BTJ", "2020-12-12T08:55:00Z", "2j 50m",
                "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
        arrayListOf(), "", ""
)

val NOT_VALID_DIFF_HOUR_RETURN_JOURNEY = FlightJourneyModel(
        "", "DummyId", "BTJ",
        "Bandara International Sultan Iskandar Muda", "",
        "10.00", 111111, "CGK", "12.40",
        "Bandara International Soekarno Hatta", "",
        212121, 0, 0, "2j 50m", 123123,
        "Rp1.500.000", 1500000, "", 0,
        false, "", false, RefundableEnum.NOT_REFUNDABLE,
        false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
        arrayListOf(Route("GA", "CGK", "2020-08-11T13:50:00Z", "BTJ", "2020-08-11T14:55:00Z", "2j 50m",
                "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
        arrayListOf(), "", ""
)

val NOT_VALID_RETURN_JOURNEY = FlightJourneyModel(
        "", "DummyId", "BTJ",
        "Bandara International Sultan Iskandar Muda", "",
        "10.00", 111111, "CGK", "12.40",
        "Bandara International Soekarno Hatta", "",
        212121, 0, 0, "2j 50m", 123123,
        "Rp1.500.000", 1500000, "", 0,
        false, "", false, RefundableEnum.NOT_REFUNDABLE,
        false, FlightFareModel("Rp1.500.000", "", "", "", "", "", 1500000, 0, 0, 0, 0, 0),
        arrayListOf(Route("GA", "CGK", "2020-08-11T07:50:00Z", "BTJ", "2020-08-11T08:55:00Z", "2j 50m",
                "", arrayListOf(), "123", false, arrayListOf(), 0, arrayListOf(), "Garuda", "www.dummylogo.com",
                "Bandara International Soekarno Hatta", "", "Bandara International Sultan Iskandar Muda", "", "")),
        arrayListOf(), "", ""
)

val COMBO_KEY = "DUMMY_COMBOKEY"