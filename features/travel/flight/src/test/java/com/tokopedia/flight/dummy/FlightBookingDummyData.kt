package com.tokopedia.flight.dummy

import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.booking.data.*
import com.tokopedia.flight.booking.data.mapper.FlightBookingMapper
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightFareModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.sessioncommon.data.profile.ProfileInfo
import com.tokopedia.sessioncommon.data.profile.ProfilePojo

/**
 * @author by furqan on 29/06/2020
 */
val DUMMY_PROFILE = ProfilePojo(
        profileInfo = ProfileInfo(
                userId = "123456",
                fullName = "Testing User",
                firstName = "Testing",
                email = "testinguser@email.com",
                birthday = "2020-11-11",
                gender = "male",
                isPhoneVerified = true,
                profilePicture = "",
                isCreatedPassword = true,
                isLoggedIn = true
        )
)

val DUMMY_BOOKING_PASSENGER = arrayListOf<FlightBookingPassengerModel>(
        FlightBookingPassengerModel().apply {
            passengerId = "asdasdasd"
            flightBookingLuggageMetaViewModels = arrayListOf()
            flightBookingMealMetaViewModels = arrayListOf()
            headerTitle = ""
            passengerBirthdate = "1995-11-11"
            passengerFirstName = "My Name"
            passengerLastName = "Hi"
        }
)

val DUMMY_CART_PRICE = arrayListOf<FlightCart.PriceDetail>(
        FlightCart.PriceDetail("Anak", "Rp100.000", 100000, ""),
        FlightCart.PriceDetail("Dewasa", "Rp200.000", 200000, "")
)

val DUMMY_OTHER_PRICE = arrayListOf<FlightCart.PriceDetail>(
        FlightCart.PriceDetail("Asuransi", "Rp900.000", 900000, "insurance")
)

val DUMMY_AMENITY_PRICE = arrayListOf<FlightCart.PriceDetail>(
        FlightCart.PriceDetail("Makanan", "Rp10.000", 10000, "")
)

val DUMMY_BOOKING_MODEL = FlightBookingModel("dummyDeparture",
        "dummyReturn",
        "2020-11-11",
        "dummyDepartureTerm",
        "dummyReturnTerm",
        "",
        true,
        true,
        FlightPriceModel(
                FlightFareModel(
                        "Rp100.000",
                        "",
                        "",
                        "",
                        "",
                        "",
                        100000,
                        0,
                        0,
                        0,
                        0,
                        0
                ),
                FlightFareModel(
                        "Rp100.000",
                        "",
                        "",
                        "",
                        "",
                        "",
                        100000,
                        0,
                        0,
                        0,
                        0,
                        0
                ),
                ""
        ),
        FlightSearchPassDataModel(
                "2020-11-11",
                "2020-12-12",
                true,
                FlightPassengerModel(1, 0, 0),
                FlightAirportModel().apply {
                    airportCode = "BTJ"
                    airportName = "Sultan Iskandar Muda Intl. Airport"
                    countryName = "Indonesia"
                    cityAirports = arrayListOf()
                    cityName = ""
                    cityCode = ""
                    cityId = ""
                },
                FlightAirportModel().apply {
                    airportCode = "BTJ"
                    airportName = "Sultan Iskandar Muda Intl. Airport"
                    countryName = "Indonesia"
                    cityAirports = arrayListOf()
                    cityName = ""
                    cityCode = ""
                    cityId = ""
                },
                FlightClassModel(1, "Ekonomi"),
                "",
                ""
        ),
        arrayListOf())

val DUMMY_BOOKING_INTERNATIONAL_MODEL = FlightBookingModel("dummyDeparture",
        "dummyReturn",
        "2020-11-11",
        "dummyDepartureTerm",
        "dummyReturnTerm",
        "",
        false,
        true,
        FlightPriceModel(
                FlightFareModel(
                        "Rp100.000",
                        "",
                        "",
                        "",
                        "",
                        "",
                        100000,
                        0,
                        0,
                        0,
                        0,
                        0
                ),
                FlightFareModel(
                        "Rp100.000",
                        "",
                        "",
                        "",
                        "",
                        "",
                        100000,
                        0,
                        0,
                        0,
                        0,
                        0
                ),
                ""
        ),
        FlightSearchPassDataModel(
                "2020-11-11",
                "2020-12-12",
                true,
                FlightPassengerModel(1, 0, 0),
                FlightAirportModel().apply {
                    airportCode = "BTJ"
                    airportName = "Sultan Iskandar Muda Intl. Airport"
                    countryName = "Indonesia"
                    cityAirports = arrayListOf()
                    cityName = ""
                    cityCode = ""
                    cityId = ""
                },
                FlightAirportModel().apply {
                    airportCode = "BTJ"
                    airportName = "Sultan Iskandar Muda Intl. Airport"
                    countryName = "Indonesia"
                    cityAirports = arrayListOf()
                    cityName = ""
                    cityCode = ""
                    cityId = ""
                },
                FlightClassModel(1, "Ekonomi"),
                "",
                ""
        ),
        arrayListOf())

val DUMMY_BOOKING_FLIGHT_DETAIL = arrayListOf(
        FlightDetailModel("dummy departure id",
                "dummy departure term",
                "CGK",
                "Jakarta",
                "2020-11-11T10:10:10Z",
                "BTJ",
                "Banda Aceh",
                "2020-11-11T14:14:14Z",
                0,
                "Rp10.000",
                10000,
                "",
                RefundableEnum.REFUNDABLE,
                10000,
                0,
                0,
                1,
                0,
                0,
                arrayListOf(),
                arrayListOf(),
                "Ekonomi"),
        FlightDetailModel("dummy return id",
                "dummy return term",
                "CGK",
                "Jakarta",
                "2020-11-11T10:10:10Z",
                "BTJ",
                "Banda Aceh",
                "2020-11-11T14:14:14Z",
                0,
                "Rp10.000",
                10000,
                "",
                RefundableEnum.REFUNDABLE,
                10000,
                0,
                0,
                1,
                0,
                0,
                arrayListOf(),
                arrayListOf(),
                "Ekonomi"))

val DUMMY_BOOKING_NEW_PRICE = arrayListOf<FlightCart.NewPrice>(
        FlightCart.NewPrice(
                "dummy departure id",
                FlightCart.Fare("Rp1000",
                        "Rp0",
                        "Rp0",
                        1000, 0, 0)
        ),
        FlightCart.NewPrice(
                "dummy return id",
                FlightCart.Fare("Rp5000",
                        "Rp0",
                        "Rp0",
                        5000, 0, 0)
        )
)

val DUMMY_MEALS_AMENITIES = arrayListOf(
        FlightBookingAmenityMetaModel().apply {
            arrivalId = "dummy arrival id"
            departureId = "dummy departure id"
            journeyId = "dummy journey id"
            key = "dummy key"
            description = "dummy description"
            amenities = arrayListOf(
                    FlightBookingAmenityModel().apply {
                        id = "dummy id"
                        title = "makanan"
                        price = "Rp10.000"
                        priceNumeric = 10000
                        departureId = "dummy departure id"
                        arrivalId = "dummy arrival id"
                        amenityType = FlightBookingMapper.AMENITY_MEAL
                    }
            )
        },
        FlightBookingAmenityMetaModel().apply {
            arrivalId = "dummy arrival id"
            departureId = "dummy departure id"
            journeyId = "dummy journey id"
            key = "dummy key"
            description = "dummy description"
            amenities = arrayListOf(
                    FlightBookingAmenityModel().apply {
                        id = "dummy id"
                        title = "makanan"
                        price = "Rp10.000"
                        priceNumeric = 10000
                        departureId = "dummy departure id"
                        arrivalId = "dummy arrival id"
                        amenityType = FlightBookingMapper.AMENITY_MEAL
                    }
            )
        }
)

val DUMMY_LUGGAGE_AMENITIES = arrayListOf(
        FlightBookingAmenityMetaModel().apply {
            arrivalId = "dummy arrival id"
            departureId = "dummy departure id"
            journeyId = "dummy journey id"
            key = "dummy key"
            description = "dummy description"
            amenities = arrayListOf(
                    FlightBookingAmenityModel().apply {
                        id = "dummy id"
                        title = "bagasi"
                        price = "Rp10.000"
                        priceNumeric = 10000
                        departureId = "dummy departure id"
                        arrivalId = "dummy arrival id"
                        amenityType = FlightBookingMapper.AMENITY_LUGGAGE
                    }
            )
        },
        FlightBookingAmenityMetaModel().apply {
            arrivalId = "dummy arrival id"
            departureId = "dummy departure id"
            journeyId = "dummy journey id"
            key = "dummy key"
            description = "dummy description"
            amenities = arrayListOf(
                    FlightBookingAmenityModel().apply {
                        id = "dummy id"
                        title = "bagasi"
                        price = "Rp10.000"
                        priceNumeric = 10000
                        departureId = "dummy departure id"
                        arrivalId = "dummy arrival id"
                        amenityType = FlightBookingMapper.AMENITY_LUGGAGE
                    }
            )
        }
)

val DUMMY_CHECKOUT = FlightCheckoutData(
        "dummy checkout id",
        "tokopedia://dummyRedirectUrl",
        "",
        "",
        "",
        FlightCheckoutData.CheckoutParameter(),
        ""
)

val DUMMY_ATC = FlightAddToCartData.Response(
        FlightAddToCartData(
                id = "dummy cart id",
                meta = Meta("dummy request id")
        )
)

val DUMMY_INSURANCE = FlightCart.Insurance(
        "dummy insurance id",
        "insurance name",
        "insurance description",
        10000,
        false,
        "insurance tnc",
        "insurance url",
        arrayListOf()

)