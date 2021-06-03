package com.tokopedia.flight.booking.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * @author by jessica on 2019-10-25
 */

data class FlightCart(

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta(),

        @SerializedName("included")
        @Expose
        val included: List<Included> = listOf(),

        @SerializedName("data")
        @Expose
        val cartData: CartData = CartData()
) {

    data class Included(
            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("attributes")
            @Expose
            val attributes: Attributes = Attributes()
    )

    data class Attributes(
            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("short_name")
            @Expose
            val shortName: String = "",

            @SerializedName("logo")
            @Expose
            val logo: String = "",

            @SerializedName("city")
            @Expose
            val city: String = ""
    )

    data class CartData(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("status")
            @Expose
            val status: String = "",

            @SerializedName("newPrice")
            @Expose
            val newPrice: List<NewPrice> = listOf(),

            @SerializedName("flight")
            @Expose
            val flight: Flight = Flight(),

            @SerializedName("voucher")
            @Expose
            val voucher: Voucher = Voucher()
    )

    data class NewPrice(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("fare")
            @Expose
            val fare: Fare = Fare()
    )

    data class Meta(
            @SerializedName("needRefresh")
            @Expose
            val needRefresh: Boolean = false,

            @SerializedName("refreshTime")
            @Expose
            val refreshTime: Int = 0,

            @SerializedName("maxRetry")
            @Expose
            val maxRetry: Int = 0
    )

    data class Response(
            @SerializedName("flightCart")
            @Expose
            val flightCart: FlightCart = FlightCart()
    )

    data class Flight(
            @SerializedName("email")
            @Expose
            val email: String = "",

            @SerializedName("phone")
            @Expose
            val phone: String = "",

            @SerializedName("contactName")
            @Expose
            val contactName: String = "",

            @SerializedName("countryID")
            @Expose
            val countryId: String = "",

            @SerializedName("totalAdult")
            @Expose
            val totalAdult: String = "",

            @SerializedName("totalAdultNumeric")
            @Expose
            val totalAdultNumeric: Int = 0,

            @SerializedName("totalChild")
            @Expose
            val totalChild: String = "",

            @SerializedName("totalChildNumeric")
            @Expose
            val totalChildNumeric: Int = 0,

            @SerializedName("totalInfant")
            @Expose
            val totalInfant: String = "",

            @SerializedName("totalInfantNumeric")
            @Expose
            val totalInfantNumeric: Int = 0,

            @SerializedName("totalPrice")
            @Expose
            val totalPrice: String = "",

            @SerializedName("totalPriceNumeric")
            @Expose
            val totalPriceNumeric: Int = 0,

            @SerializedName("currency")
            @Expose
            val currency: String = "",

            @SerializedName("isDomestic")
            @Expose
            val isDomestic: Boolean = true,

            @SerializedName("repriceTime")
            @Expose
            val repriceTime: Int = 0,

            @SerializedName("adult")
            @Expose
            val adult: Int = 1,

            @SerializedName("child")
            @Expose
            val child: Int = 0,

            @SerializedName("infant")
            @Expose
            val infant: Int = 0,

            @SerializedName("class")
            @Expose
            val flightClass: Int = 0,

            @SerializedName("priceDetail")
            @Expose
            val priceDetail: List<PriceDetail> = listOf(),

            @SerializedName("amenityOptions")
            @Expose
            val amenityOptions: List<Amenity> = listOf(),

            @SerializedName("insuranceOptions")
            @Expose
            val insuranceOptions: List<Insurance> = listOf(),

            @SerializedName("journeys")
            @Expose
            val journeys: List<Journey> = listOf(),

            @SerializedName("mandatoryDOB")
            @Expose
            val mandatoryDob: Boolean = false
    )

    @Parcelize
    data class PriceDetail(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceNumeric")
            @Expose
            val priceNumeric: Int = 0,

            val priceDetailId: String = ""
    ): Parcelable {
            fun idEqualsToInsuranceId(other: Insurance): Boolean = other.id == priceDetailId
    }

    data class Amenity(
            @SerializedName("departureAirportID")
            @Expose
            val departureAirportId: String = "",

            @SerializedName("arrivalAirportID")
            @Expose
            val arrivalAirportId: String = "",

            @SerializedName("type")
            @Expose
            val type: Int = 0,

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceNumeric")
            @Expose
            val priceNumeric: Int = 0,

            @SerializedName("description")
            @Expose
            val detail: String = "",

            @SerializedName("journeyID")
            @Expose
            val journeyId: String = "",

            @SerializedName("itemID")
            @Expose
            val itemId: String = "",

            @SerializedName("key")
            @Expose
            val key: String = "",

            @SerializedName("items")
            @Expose
            val items: List<AmenityItem> = listOf()
    )

    data class AmenityItem(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceNumeric")
            @Expose
            val priceNumeric: Int = 0,

            @SerializedName("description")
            @Expose
            val description: String = "",

            @SerializedName("currency")
            @Expose
            val currency: String = ""
    )

    @Parcelize
    data class Insurance(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("description")
            @Expose
            val description: String = "",

            @SerializedName("totalPriceNumeric")
            @Expose
            val totalPriceNumeric: Int = 0,

            @SerializedName("defaultChecked")
            @Expose
            val defaultChecked: Boolean = false,

            @SerializedName("tncAggrement")
            @Expose
            val tncAgreement: String = "",

            @SerializedName("tncURL")
            @Expose
            val tncUrl: String = "",

            @SerializedName("benefits")
            @Expose
            val benefits: List<Benefit>
    ): Parcelable

    data class Voucher(
            @SerializedName("enableVoucher")
            @Expose
            val enableVoucher: Boolean = false,

            @SerializedName("isCouponActive")
            @Expose
            val isCouponActive: Int = 0,

            @SerializedName("defaultPromo")
            @Expose
            val defaultPromo: String = "",

            @SerializedName("autoApply")
            @Expose
            val autoApply: AutoApply = AutoApply()
    )

    data class AutoApply(
            @SerializedName("success")
            @Expose
            val success: Boolean = false,

            @SerializedName("code")
            @Expose
            val code: String = "",

            @SerializedName("isCoupon")
            @Expose
            val isCoupon: Int = 0,

            @SerializedName("discountAmount")
            @Expose
            val discountAmount: Int = 0,

            @SerializedName("discountPrice")
            @Expose
            val discountPrice: String = "",

            @SerializedName("discountedAmount")
            @Expose
            val discountedAmount: Int = 0,

            @SerializedName("discountedPrice")
            @Expose
            val discountedPrice: String = "",

            @SerializedName("titleDescription")
            @Expose
            val titleDescription: String = "",

            @SerializedName("messageSuccess")
            @Expose
            val messageSuccess: String = "",

            @SerializedName("promoID")
            @Expose
            val promoId: Int = 0
    )

    @Parcelize
    data class Benefit(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("description")
            @Expose
            val description: String = "",

            @SerializedName("icon")
            @Expose
            val icon: String = ""
    ): Parcelable

    data class Journey(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("searchTerm")
            @Expose
            val searchTerm: String = "",

            @SerializedName("departureAirportID")
            @Expose
            val departureAirportId: String = "",

            @SerializedName("departureTime")
            @Expose
            val departureTime: String = "",

            @SerializedName("departureTerminal")
            @Expose
            val departureTerminal: String = "",

            @SerializedName("arrivalAirportID")
            @Expose
            val arrivalAirportId: String = "",

            @SerializedName("arrivalTime")
            @Expose
            val arrivalTime: String = "",

            @SerializedName("arrivalTerminal")
            @Expose
            val arrivalTerminal: String = "",

            @SerializedName("totalTransit")
            @Expose
            val totalTransit: Int = 0,

            @SerializedName("totalStop")
            @Expose
            val totalStop: Int = 0,

            @SerializedName("addDayArrival")
            @Expose
            val addDayArrival: Int = 0,

            @SerializedName("duration")
            @Expose
            val duration: String = "",

            @SerializedName("durationMinute")
            @Expose
            val durationMinute: Int = 0,

            @SerializedName("durationLong")
            @Expose
            val durationLong: String = "",

            @SerializedName("fare")
            @Expose
            val fare: Fare = Fare(),

            @SerializedName("routes")
            @Expose
            val routes: List<Route> = listOf(),

            @SerializedName("passengers")
            @Expose
            val passengers: List<Passenger> = listOf(),

            @SerializedName("insurances")
            @Expose
            val insurances: List<JourneyInsurance> = listOf()
    )

    data class Fare(
            @SerializedName("adult")
            @Expose
            val adult: String = "",

            @SerializedName("child")
            @Expose
            val child: String = "",

            @SerializedName("infant")
            @Expose
            val infant: String = "",

            @SerializedName("adultNumeric")
            @Expose
            val adultNumeric: Int = 0,

            @SerializedName("childNumeric")
            @Expose
            val childNumeric: Int = 0,

            @SerializedName("infantNumeric")
            @Expose
            val infantNumeric: Int = 0
    )

    data class Route(
            @SerializedName("operatingAirlineID")
            @Expose
            val operatingAirlineId: String = "",

            @SerializedName("departureAirportID")
            @Expose
            val departureAirportId: String = "",

            @SerializedName("departureTime")
            @Expose
            val departureTime: String = "",

            @SerializedName("arrivalAirportID")
            @Expose
            val arrivalAirportId: String = "",

            @SerializedName("arrivalTime")
            @Expose
            val arrivalTime: String = "",

            @SerializedName("airlineID")
            @Expose
            val airlineId: String = "",

            @SerializedName("flightNumber")
            @Expose
            val flightNumber: String = "",

            @SerializedName("duration")
            @Expose
            val duration: String = "",

            @SerializedName("layover")
            @Expose
            val layover: String = "",

            @SerializedName("refundable")
            @Expose
            val refundable: Boolean = false,

            @SerializedName("departureTerminal")
            @Expose
            val departureTerminal: String = "",

            @SerializedName("arrivalTerminal")
            @Expose
            val arrivalTerminal: String = "",

            @SerializedName("stop")
            @Expose
            val stop: Int = 0,

            @SerializedName("stopDetail")
            @Expose
            val stopDetail: List<StopDetail> = listOf(),

            @SerializedName("infos")
            @Expose
            val infos: List<Info> = listOf(),

            @SerializedName("amenities")
            @Expose
            val amenities: List<com.tokopedia.flight.search.data.cloud.single.Amenity> = listOf()
    )

    data class StopDetail(
            @SerializedName("code")
            @Expose
            val code: String = "",

            @SerializedName("city")
            @Expose
            val city: String = ""
    )

    data class Info(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class AmenityIconLabel(
            @SerializedName("icon")
            @Expose
            val icon: String = "",

            @SerializedName("label")
            @Expose
            val label: String = ""
    )

    data class Passenger(
            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("firstName")
            @Expose
            val firstName: String = "",

            @SerializedName("lastName")
            @Expose
            val lastName: String = "",

            @SerializedName("dob")
            @Expose
            val dob: String = "",

            @SerializedName("nationality")
            @Expose
            val nationality: String = "",

            @SerializedName("passportNo")
            @Expose
            val passportNo: String = "",

            @SerializedName("passportCountry")
            @Expose
            val passportCountry: String = "",

            @SerializedName("passportExpiry")
            @Expose
            val passportExpiry: String = "",

            @SerializedName("amenities")
            @Expose
            val amenities: List<PassengerAmenity> = listOf()
    )

    data class PassengerAmenity(
            @SerializedName("departureAirportID")
            @Expose
            val departureAirportId: String = "",

            @SerializedName("arrivalAirportID")
            @Expose
            val arrivalAirportId: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceNumeric")
            @Expose
            val priceNumeric: Int = 0,

            @SerializedName("detail")
            @Expose
            val detail: String = "",

            @SerializedName("journeyID")
            @Expose
            val journeyId: String = "",

            @SerializedName("itemID")
            @Expose
            val itemId: String = "",

            @SerializedName("key")
            @Expose
            val key: String = ""
    )

    data class JourneyInsurance(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("paidAmount")
            @Expose
            val paidAmount: String = "",

            @SerializedName("paidAmountNumeric")
            @Expose
            val paidAmountNumeric: Int = 0,

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("tagLine")
            @Expose
            val tagLine: String = ""
    )

}