package com.tokopedia.flight.dummy

import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.homepage.presentation.model.FlightFare
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.presentation.model.FlightFareData
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday

/**
 * @author by furqan on 08/05/2020
 */

val BANNER_DATA = TravelCollectiveBannerModel(
        arrayListOf(
                TravelCollectiveBannerModel.Banner(
                        "1",
                        "Banner Dummy 1",
                        TravelCollectiveBannerModel.Attribute(
                                "Description Banner Dummy 1",
                                "",
                                "tokopedia://dummy",
                                "www.dummyimage.com",
                                ""
                        )
                ),
                TravelCollectiveBannerModel.Banner(
                        "2",
                        "Banner Dummy 2",
                        TravelCollectiveBannerModel.Attribute(
                                "Description Banner Dummy 2",
                                "",
                                "tokopedia://dummy",
                                "www.dummyimage.com",
                                ""
                        )
                )
        )
)

val TICKER_DATA = TravelTickerModel(
        "Dummy Ticker Title",
        "Dummy Ticker Message",
        "www.dummyticker.com",
        1,
        1,
        "",
        "",
        1,
        "Dummy Page",
        false
)

val HOLIDAY_EMPTY_DATA = TravelCalendarHoliday.HolidayData(arrayListOf())

val HOLIDAY_WITH_DATA = TravelCalendarHoliday.HolidayData(arrayListOf(
        TravelCalendarHoliday("holiday1",
                TravelCalendarHoliday.HolidayAttribute(
                        "2020-11-11",
                        "Libur 1"
                )),
        TravelCalendarHoliday("holiday2",
                TravelCalendarHoliday.HolidayAttribute(
                        "2020-11-12",
                        "Libur 2"
                )),
        TravelCalendarHoliday("holiday3",
                TravelCalendarHoliday.HolidayAttribute(
                        "2020-11-13",
                        "Libur 3"
                )),
        TravelCalendarHoliday("holiday4",
                TravelCalendarHoliday.HolidayAttribute(
                        "2020-11-14",
                        "Libur 4"
                ))
))

val FARE_CALENDAR_DATA = FlightFareData(
        FlightFare("dummyId",
                arrayListOf(
                        FlightFareAttributes("2020-02-03", 0, "Rp.0", "Rp.200", true),
                        FlightFareAttributes("2020-02-02", 200, "Rp.200", "Rp.300", false)
                )
        )
)