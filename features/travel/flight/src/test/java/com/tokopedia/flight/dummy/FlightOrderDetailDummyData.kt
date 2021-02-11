package com.tokopedia.flight.dummy

import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationDetailPassengerModel
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.orderdetail.data.OrderDetailCancellation
import com.tokopedia.flight.orderdetail.presentation.model.*
import com.tokopedia.usecase.coroutines.Success

/**
 * @author by furqan on 16/10/2020
 */
val DUMMY_FAILED_ORDER_DETAIL_DATA = FlightOrderDetailDataModel(
        1,
        "0001-01-01T00:00:00Z",
        600,
        "Gagal",
        1,
        "1234567890",
        "Muhammad Furqan",
        "email@email.com",
        "123456789012",
        "ID",
        "Rp1.000.000",
        1000000,
        "Rp0",
        0,
        "Rp0",
        0,
        "Rp1.000.000",
        1000000,
        "Rp",
        "dummy pdf",
        true,
        false,
        "Ekonomi",
        "dummy contact us URL",
        false,
        FlightOrderDetailPaymentModel(
                1,
                1,
                "Paid",
                "BCA Virtual Account",
                "dummy icon",
                "0001-01-01T00:00:00Z",
                "",
                "ididid",
                "",
                0,
                "Rp0",
                0,
                "rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "BCA",
                "Jakarta",
                "1234567890",
                "Furqan",
                "Rp1.000.000"
        ),
        arrayListOf(
                FlightOrderDetailJourneyModel(
                        1,
                        1,
                        "123",
                        "0001-01-01T00:00:00Z",
                        "departure airport",
                        "aceh",
                        "321",
                        "0001-01-01T00:00:00Z",
                        "arrival airport",
                        "jakarta",
                        0,
                        0,
                        0,
                        "2j",
                        120,
                        FlightOrderDetailFareModel(
                                1000000,
                                0,
                                0
                        ),
                        arrayListOf(
                                FlightOrderDetailRouteModel(
                                        "123",
                                        "departure time",
                                        "departure airport",
                                        "departure city",
                                        "321",
                                        "arrival time",
                                        "arrival airport",
                                        "arrival city",
                                        "123ASD",
                                        "890",
                                        "Seulawah Air",
                                        "",
                                        "",
                                        "AN-12345",
                                        "2j",
                                        120,
                                        "",
                                        0,
                                        false,
                                        "2",
                                        "",
                                        0,
                                        "",
                                        arrayListOf(),
                                        arrayListOf(),
                                        FlightOrderDetailFreeAmenityModel(
                                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                false,
                                                false,
                                                false,
                                                arrayListOf()
                                        )
                                )
                        ),
                        FlightOrderDetailWebCheckInModel(
                                "Check In",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                        )
                )
        ),
        arrayListOf(
                FlightOrderDetailPassengerModel(
                        1,
                        1,
                        1,
                        "Dewasa",
                        1,
                        "Tuan",
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(),
                        arrayListOf()
                )
        ),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
)

val DUMMY_ORDER_DETAIL_JOURNEY_MULTI_AIRLINE = FlightOrderDetailJourneyModel(
        1,
        1,
        "123",
        "0001-01-01T00:00:00Z",
        "departure airport",
        "aceh",
        "321",
        "0001-01-01T00:00:00Z",
        "arrival airport",
        "jakarta",
        0,
        0,
        0,
        "2j",
        120,
        FlightOrderDetailFareModel(
                1000000,
                0,
                0
        ),
        arrayListOf(
                FlightOrderDetailRouteModel(
                        "123",
                        "departure time",
                        "departure airport",
                        "departure city",
                        "321",
                        "arrival time",
                        "arrival airport",
                        "arrival city",
                        "123ASD",
                        "890",
                        "Seulawah Air",
                        "logo seulawah air",
                        "",
                        "AN-12345",
                        "2j",
                        120,
                        "",
                        0,
                        true,
                        "2",
                        "",
                        0,
                        "",
                        arrayListOf(),
                        arrayListOf(),
                        FlightOrderDetailFreeAmenityModel(
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                false,
                                false,
                                false,
                                arrayListOf()
                        )
                ),
                FlightOrderDetailRouteModel(
                        "123",
                        "departure time",
                        "departure airport",
                        "departure city",
                        "321",
                        "arrival time",
                        "arrival airport",
                        "arrival city",
                        "123ASD",
                        "890",
                        "Garuda Indonesia",
                        "logo garuda indonesia",
                        "",
                        "AN-12345",
                        "2j",
                        120,
                        "",
                        0,
                        true,
                        "2",
                        "",
                        0,
                        "",
                        arrayListOf(),
                        arrayListOf(),
                        FlightOrderDetailFreeAmenityModel(
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                false,
                                false,
                                false,
                                arrayListOf()
                        )
                )
        ),
        FlightOrderDetailWebCheckInModel(
                "Check In",
                "",
                "",
                "",
                "",
                "",
                ""
        )
)

val DUMMY_ORDER_DETAIL_DATA = FlightOrderDetailDataModel(
        1,
        "0001-01-01T00:00:00Z",
        700,
        "Berhasil",
        1,
        "1234567890",
        "Muhammad Furqan",
        "email@email.com",
        "123456789012",
        "ID",
        "Rp1.000.000",
        1000000,
        "Rp0",
        0,
        "Rp0",
        0,
        "Rp1.000.000",
        1000000,
        "Rp",
        "dummy pdf",
        true,
        false,
        "Ekonomi",
        "dummy contact us URL",
        false,
        FlightOrderDetailPaymentModel(
                1,
                1,
                "Paid",
                "BCA Virtual Account",
                "dummy icon",
                "0001-01-01T00:00:00Z",
                "",
                "ididid",
                "",
                0,
                "Rp0",
                0,
                "rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "BCA",
                "Jakarta",
                "1234567890",
                "Furqan",
                "Rp1.000.000"
        ),
        arrayListOf(
                FlightOrderDetailJourneyModel(
                        1,
                        1,
                        "CGK",
                        "0001-01-01T00:00:00Z",
                        "departure airport",
                        "aceh",
                        "BTJ",
                        "0001-01-01T00:00:00Z",
                        "arrival airport",
                        "jakarta",
                        0,
                        0,
                        0,
                        "2j",
                        120,
                        FlightOrderDetailFareModel(
                                1000000,
                                100,
                                10
                        ),
                        arrayListOf(
                                FlightOrderDetailRouteModel(
                                        "123",
                                        "departure time",
                                        "departure airport",
                                        "departure city",
                                        "321",
                                        "arrival time",
                                        "arrival airport",
                                        "arrival city",
                                        "123ASD",
                                        "890",
                                        "Seulawah Air",
                                        "",
                                        "",
                                        "AN-12345",
                                        "2j",
                                        120,
                                        "",
                                        0,
                                        true,
                                        "2",
                                        "",
                                        0,
                                        "",
                                        arrayListOf(),
                                        arrayListOf(),
                                        FlightOrderDetailFreeAmenityModel(
                                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                false,
                                                false,
                                                false,
                                                arrayListOf()
                                        )
                                )
                        ),
                        FlightOrderDetailWebCheckInModel(
                                "Check In",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                        )
                )
        ),
        arrayListOf(
                FlightOrderDetailPassengerModel(
                        1,
                        1,
                        0,
                        "Dewasa",
                        1,
                        "Tuan",
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(
                                FlightOrderDetailAmenityModel(
                                        "CGK",
                                        "BTJ",
                                        1,
                                        "Rp1.000",
                                        1000,
                                        "Bagasi 10kg"
                                ),
                                FlightOrderDetailAmenityModel(
                                        "CGK",
                                        "BTJ",
                                        2,
                                        "Rp1.000",
                                        1000,
                                        "Makanan Enak Banget"
                                ),
                                FlightOrderDetailAmenityModel(
                                        "CGK",
                                        "BTJ",
                                        3,
                                        "Rp1.000",
                                        1000,
                                        "Wrong Input"
                                )
                        ),
                        arrayListOf()
                ),
                FlightOrderDetailPassengerModel(
                        1,
                        1,
                        1,
                        "Anak",
                        1,
                        "Tuan",
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(),
                        arrayListOf()
                ),
                FlightOrderDetailPassengerModel(
                        1,
                        1,
                        2,
                        "Bayi",
                        1,
                        "Tuan",
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(),
                        arrayListOf()
                )
        ),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(
                FlightOrderDetailCancellationModel(
                        cancelId = 1234567890,
                        cancelDetails = arrayListOf(
                                FlightOrderDetailCancellationModel.OrderDetailCancellationDetail(
                                        journeyId = 1,
                                        passengerId = 1,
                                        refundedGateway = "",
                                        refundedTime = ""
                                )
                        ),
                        createTime = "2021-11-11T10:10:10Z",
                        updateTime = "",
                        estimatedRefund = "Rp1.000.000",
                        estimatedRefundNumeric = 1000000,
                        realRefund = "Rp1.000.000",
                        realRefundNumeric = 1000000,
                        status = 1,
                        statusStr = "Berhasil",
                        statusType = "",
                        refundInfo = "Refund Info",
                        refundDetail = FlightOrderDetailCancellationModel.OrderDetailRefundDetailModel(
                                arrayListOf(),
                                arrayListOf(),
                                arrayListOf(),
                                arrayListOf()
                        )
                )
        )
)

val DUMMY_ORDER_DETAIL_DATA_WITH_MULTI_AIRLINE = FlightOrderDetailDataModel(
        1,
        "0001-01-01T00:00:00Z",
        700,
        "Berhasil",
        1,
        "1234567890",
        "Muhammad Furqan",
        "email@email.com",
        "123456789012",
        "ID",
        "Rp1.000.000",
        1000000,
        "Rp0",
        0,
        "Rp0",
        0,
        "Rp1.000.000",
        1000000,
        "Rp",
        "dummy pdf",
        true,
        false,
        "Ekonomi",
        "dummy contact us URL",
        false,
        FlightOrderDetailPaymentModel(
                1,
                1,
                "Paid",
                "BCA Virtual Account",
                "dummy icon",
                "0001-01-01T00:00:00Z",
                "",
                "ididid",
                "",
                0,
                "Rp0",
                0,
                "rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "BCA",
                "Jakarta",
                "1234567890",
                "Furqan",
                "Rp1.000.000"
        ),
        arrayListOf(DUMMY_ORDER_DETAIL_JOURNEY_MULTI_AIRLINE),
        arrayListOf(
                FlightOrderDetailPassengerModel(
                        1,
                        1,
                        1,
                        "Dewasa",
                        1,
                        "Tuan",
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(),
                        arrayListOf()
                )
        ),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
)

val DUMMY_ORDER_DETAIL_JOURNEY_ONE_AIRLINE = FlightOrderDetailJourneyModel(
        1,
        1,
        "123",
        "2020-11-11T08:00:00Z",
        "departure airport",
        "aceh",
        "321",
        "0001-01-01T10:00:00Z",
        "arrival airport",
        "jakarta",
        0,
        0,
        0,
        "2j",
        120,
        FlightOrderDetailFareModel(
                1000000,
                0,
                0
        ),
        arrayListOf(
                FlightOrderDetailRouteModel(
                        "123",
                        "departure time",
                        "departure airport",
                        "departure city",
                        "321",
                        "arrival time",
                        "arrival airport",
                        "arrival city",
                        "123ASD",
                        "890",
                        "Seulawah Air",
                        "logo seulawah air",
                        "",
                        "AN-12345",
                        "2j",
                        120,
                        "",
                        0,
                        true,
                        "2",
                        "",
                        0,
                        "",
                        arrayListOf(),
                        arrayListOf(),
                        FlightOrderDetailFreeAmenityModel(
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                false,
                                false,
                                false,
                                arrayListOf()
                        )
                ),
                FlightOrderDetailRouteModel(
                        "123",
                        "departure time",
                        "departure airport",
                        "departure city",
                        "321",
                        "arrival time",
                        "arrival airport",
                        "arrival city",
                        "123ASD",
                        "890",
                        "Seulawah Air",
                        "logo seulawah air",
                        "",
                        "AN-12345",
                        "2j",
                        120,
                        "",
                        0,
                        false,
                        "2",
                        "",
                        0,
                        "",
                        arrayListOf(),
                        arrayListOf(),
                        FlightOrderDetailFreeAmenityModel(
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                FlightOrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                false,
                                false,
                                false,
                                arrayListOf()
                        )
                )
        ),
        FlightOrderDetailWebCheckInModel(
                "Check In",
                "",
                "",
                "",
                "",
                "",
                ""
        )
)

val DUMMY_CROSS_SELL = Success(
        TravelCrossSelling(
                items = arrayListOf(
                        TravelCrossSelling.Item(
                                product = "Dummy Cross Selling",
                                title = "This is Dummy Data",
                                content = "Want to use your dummy data?",
                                prefix = "",
                                imageUrl = "",
                                uriWeb = "",
                                uri = "",
                                value = ""
                        )
                ),
                meta = TravelCrossSelling.Meta(
                        "Lengkapi perjalananmu",
                        "",
                        ""
                )
        )
)

val DUMMY_CANCELLATION_LIST_DATA = arrayListOf(
        FlightOrderCancellationListModel(
                orderId = "1234567890",
                cancellationDetail = FlightOrderCancellationDetailModel(
                        refundId = 0,
                        createTime = "",
                        realRefund = "",
                        status = 0,
                        passengers = arrayListOf(
                                FlightOrderCancellationDetailPassengerModel(
                                        id = 1,
                                        type = 1,
                                        typeString = "dewasa",
                                        title = 1,
                                        titleString = "Tuan",
                                        firstName = "Muhammad",
                                        lastName = "Furqan",
                                        departureAirportId = "BTJ",
                                        arrivalAirportId = "CGK",
                                        journeyId = 1,
                                        amenities = arrayListOf()
                                )
                        ),
                        journeys = arrayListOf(
                                FlightOrderDetailJourneyModel(
                                        id = 1,
                                        status = 700,
                                        departureId = "BTJ",
                                        departureTime = "2020-11-11T10:10:10Z",
                                        departureAirportName = "Bandara Intl. Sultan Iskandar Muda",
                                        departureCityName = "Banda Aceh",
                                        arrivalId = "CGK",
                                        arrivalTime = "2020-11-11T11:11:11Z",
                                        arrivalAirportName = "Bandara Intl. Soekarno Hatta",
                                        arrivalCityName = "Jakarta",
                                        totalTransit = 0,
                                        totalStop = 0,
                                        addDayArrival = 0,
                                        duration = "1jam",
                                        durationMinute = 60,
                                        fare = FlightOrderDetailFareModel(10000, 0, 0),
                                        routes = arrayListOf(),
                                        webCheckIn = FlightOrderDetailWebCheckInModel(
                                                title = "",
                                                subtitle = "",
                                                startTime = "",
                                                endTime = "",
                                                iconUrl = "",
                                                appUrl = "",
                                                webUrl = ""
                                        ),
                                        airlineLogo = "",
                                        airlineName = "",
                                        refundableInfo = false
                                )
                        ),
                        statusStr = "",
                        statusType = "",
                        refundInfo = "",
                        refundDetail = OrderDetailCancellation.OrderDetailRefundDetail()
                )
        )
)