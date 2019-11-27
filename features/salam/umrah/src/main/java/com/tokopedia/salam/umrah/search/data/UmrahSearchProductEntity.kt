package com.tokopedia.salam.umrah.search.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.salam.umrah.common.data.UmrahHotel
import com.tokopedia.salam.umrah.common.data.UmrahVariant
import com.tokopedia.salam.umrah.search.presentation.adapter.UmrahSearchAdapterTypeFactory

/**
 * @author by firman on 18/10/2019
 */
data class UmrahSearchProductEntity(
        @SerializedName("umrahSearchProducts")
        @Expose
        val umrahSearchProducts: List<UmrahSearchProduct> = arrayListOf()
)

data class UmrahSearchProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("banner")
        @Expose
        val banners: List<String> = listOf(),
        @SerializedName("slugName")
        @Expose
        val slugName: String = "",
        @SerializedName("availableSeat")
        @Expose
        val availableSeat: Int = 0,
        @SerializedName("travelAgent")
        @Expose
        val travelAgent: TravelAgent = TravelAgent(),
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("originalPrice")
        @Expose
        val originalPrice: Int = 0,
        @SerializedName("slashPrice")
        @Expose
        val slashPrice: Int = 0,
        @SerializedName("downPaymentPrice")
        @Expose
        val downPaymentPrice: Int = 0,
        @SerializedName("departureCity")
        @Expose
        val departureCity: City = City(),
        @SerializedName("arrivalCity")
        @Expose
        val arrivalCity: City = City(),
        @SerializedName("returnFromCity")
        @Expose
        val returnFromCity: City = City(),
        @SerializedName("transitCity")
        @Expose
        val transitCity: City = City(),
        @SerializedName("departureDate")
        @Expose
        val departureDate: String = "",
        @SerializedName("returningDate")
        @Expose
        val returningDate: String = "",
        @SerializedName("durationDays")
        @Expose
        val durationDays: Int = 0,
        @SerializedName("hotels")
        @Expose
        val hotels: List<UmrahHotel> = arrayListOf(),
        @SerializedName("airlines")
        @Expose
        val airlines: List<AirLines> = arrayListOf(),
        @SerializedName("facilites")
        @Expose
        val facilites: List<String> = arrayListOf(),
        @SerializedName("nonFacilities")
        @Expose
        val nonFacilities: List<String> = arrayListOf(),
        @SerializedName("term")
        @Expose
        val term: List<String> = arrayListOf(),
        @SerializedName("itineraries")
        @Expose
        val itineraries: List<Itinerari> = arrayListOf(),
        @SerializedName("variant")
        @Expose
        val variant: List<UmrahVariant> = arrayListOf()
) : Visitable<UmrahSearchAdapterTypeFactory> {
    override fun type(typeFactory: UmrahSearchAdapterTypeFactory?): Int =
            typeFactory?.type() ?: 0
}

data class TravelAgent(
        @SerializedName("id")
        @Expose
        val id: String = ""
)

data class City(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("countryCode")
        @Expose
        val countryCode: String = ""
)

data class AirLines(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("logoUrl")
        @Expose
        val logoUrl: String = "",
        @SerializedName("type")
        @Expose
        val type: Int = 0
)

data class Itinerari(
        @SerializedName("id")
        @Expose
        val day: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = ""
)