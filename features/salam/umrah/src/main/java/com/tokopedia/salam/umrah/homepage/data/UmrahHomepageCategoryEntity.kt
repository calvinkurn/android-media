package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahHotel
import com.tokopedia.salam.umrah.common.data.UmrahVariant
import com.tokopedia.salam.umrah.homepage.presentation.adapter.factory.UmrahHomepageFactory

data class UmrahHomepageCategoryEntity(
        val umrahCategories: List<UmrahCategories> = arrayListOf()
):UmrahHomepageModel(){
        companion object{
                val LAYOUT = R.layout.partial_umrah_home_page_category
        }

        override fun type(typeFactory: UmrahHomepageFactory): Int {
           return typeFactory.type(this)
        }
}

data class UmrahCategories(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("coverImageUrl")
        @Expose
        val coverImageURL: String = "",
        @SerializedName("slugName")
        @Expose
        val slugName: String = "",
        @SerializedName("startingPrice")
        @Expose
        val startingPrice: String = "",
        @SerializedName("product")
        @Expose
        val product: List<Products> = arrayListOf()
)

data class Products(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("availableSeat")
        @Expose
        val availableSeat: Int = 0,
        @SerializedName("banner")
        @Expose
        val banner: List<String> = arrayListOf(),
        @SerializedName("slugName")
        @Expose
        val slugName: String="",
        @SerializedName("travelAgent")
        @Expose
        val travelAgent: TravelAgent = TravelAgent(),
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("slashPrice")
        @Expose
        val slashPrice: Int = 0,
        @SerializedName("originalPrice")
        @Expose
        val originalPrice: Int = 0,
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
)

data class TravelAgent(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("logoUrl")
        @Expose
        val logoUrl: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""

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