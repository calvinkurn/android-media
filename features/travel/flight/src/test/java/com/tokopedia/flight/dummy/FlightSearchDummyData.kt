package com.tokopedia.flight.dummy

import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice
import com.tokopedia.flight.promo_chips.data.model.DataPromoChips
import com.tokopedia.flight.promo_chips.data.model.FlightLowestPrice
import com.tokopedia.flight.search.data.cloud.single.Route
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import com.tokopedia.flight.search.presentation.model.FlightFareModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaModel
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.statistics.*

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
        true, true, true, true, true)

val JOURNEY_LIST_DATA = arrayListOf(
        FlightJourneyModel(
                "", "DummyId", false, true, "CGK",
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
                "", "DummyId", true, false, "CGK",
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
        "", "DummyId", true, true, "CGK",
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
        "", "DummyId", false, false, "BTJ",
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
        "", "DummyId", false, false, "BTJ",
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
        "", "DummyId", false, false, "BTJ",
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

val PROMO_CHIPS = FlightLowestPrice(listOf(DataPromoChips(
        "20210305", listOf(AirlinePrice("QZ","https://ecs7.tokopedia.net/img/attachment/2017/12/20/5512496/5512496_6367d556-f391-4270-9444-8e740fb2f2a4.png","Indonesia AirAsia", "Rp 471.901", 471901),
        AirlinePrice("QG","https://ecs7.tokopedia.net/img/attachment/2017/12/20/5512496/5512496_7f755496-e7cd-480d-bcc0-081c84828d37.png","Citilink Indonesia", "Rp 471.920", 471920)))))
val PROMO_CHIPS_EMPTY = FlightLowestPrice()