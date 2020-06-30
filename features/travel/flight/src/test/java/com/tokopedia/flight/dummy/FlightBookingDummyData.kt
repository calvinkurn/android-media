package com.tokopedia.flight.dummy

import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.bookingV3.data.FlightBookingModel
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.searchV4.presentation.model.FlightFareModel
import com.tokopedia.flight.searchV4.presentation.model.FlightPriceModel
import com.tokopedia.flight.searchV4.presentation.model.FlightSearchPassDataModel
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