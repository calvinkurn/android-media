package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.pdp.data.UmrahFAQContent
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpAirlineModel
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpFeaturedFacilityModel
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpItineraryModel
import com.tokopedia.salam.umrah.travel.presentation.adapter.UmrahTravelProductAdapterTypeFactory

/**
 * @author by M on 30/10/19
 */
data class UmrahProductModel(
        @SerializedName("umrahProduct")
        @Expose
        val umrahProduct: UmrahProduct
) {
    data class UmrahProduct(
            @SerializedName("ui")
            val ui: UmrahProductUI = UmrahProductUI(),

            @SerializedName("id")
            @Expose
            var id: String = "",

            @SerializedName("availableSeat")
            @Expose
            var availableSeat: Int = 0,

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("slugName")
            @Expose
            val slugName: String = "",

            @SerializedName("departureDate")
            val departureDate: String = "",

            @SerializedName("returningDate")
            val returningDate: String = "",

            @SerializedName("banner")
            @Expose
            val banners: List<String> = listOf(),

            @SerializedName("travelAgent")
            @Expose
            val travelAgent: TravelAgent = TravelAgent(),

            @SerializedName("hotels")
            val hotels: List<UmrahHotel> = listOf(),

            @SerializedName("airlines")
            val airlines: List<UmrahPdpAirlineModel> = listOf(),

            @SerializedName("itineraries")
            val itineraries: List<UmrahPdpItineraryModel> = listOf(),

            @SerializedName("featuredFacilities")
            val featuredFacilites: List<UmrahPdpFeaturedFacilityModel> = listOf(),

            @SerializedName("facilities")
            val facilities: List<String> = listOf(),

            @SerializedName("nonFacilities")
            val nonFacilities: List<String> = listOf(),

            @SerializedName("variant")
            val variants: List<UmrahVariant> = listOf(),

            @SerializedName("terms")
            val additionalInformation: List<String> = listOf(),

            @SerializedName("durationDays")
            val durationDays: Int = 0,

            @SerializedName("transitCity")
            val transitCity: UmrahCity = UmrahCity(),

            @SerializedName("originalPrice")
            val originalPrice: Int = 0,

            @SerializedName("slashPrice")
            val slashPrice: Int = 0,

            @SerializedName("downPaymentPrice")
            val downPaymentPrice: Int = 0,

            @SerializedName("faq")
            val faqs: UmrahFAQ = UmrahFAQ()
    ): Visitable<UmrahTravelProductAdapterTypeFactory> {
            override fun type(typeFactory: UmrahTravelProductAdapterTypeFactory?): Int =
                    typeFactory?.type() ?: 0
        data class UmrahProductUI(
                @SerializedName("travelDates")
                val travelDates: String = "",

                @SerializedName("travelDurations")
                val travelDurations: String = "",

                @SerializedName("hotelStars")
                val hotelStars: String = "",

                @SerializedName("variant")
                val variant: String = "",

                @SerializedName("transitCity")
                val transitCity: String = "",

                @SerializedName("departureDate")
                val departureDate: String = "",

                @SerializedName("returningDate")
                val returningDate: String = "",

                @SerializedName("availableSeat")
                @Expose
                val availableSeat: String = ""
        )

        data class UmrahFAQ(
                @SerializedName("allContentsLink")
                val allContentsLink: String = "",

                @SerializedName("contents")
                val contents: List<UmrahFAQContent> = listOf()
        )
    }
}