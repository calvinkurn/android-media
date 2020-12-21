package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.hoteldetail.data.entity.PropertySafetyBadge
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory

data class Property(
        @SerializedName("id")
        @Expose
        val id: Long = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("address")
        @Expose
        val address: String = "",

        @SerializedName("roomPrice")
        @Expose
        val roomPrice: List<PropertyPrice> = listOf(),

        @SerializedName("roomAvailability")
        @Expose
        val roomAvailability: Int = 0,

        @SerializedName("image")
        @Expose
        val image: List<Image> = listOf(),

        @SerializedName("star")
        @Expose
        val star: Int = 0,

        @SerializedName("features")
        @Expose
        val features: List<String> = listOf(),

        @SerializedName("review")
        @Expose
        val review: Review = Review(),

        @SerializedName("location")
        @Expose
        val location: Location = Location(),

        @SerializedName("isDirectPayment")
        @Expose
        val isDirectPayment: Boolean = false,

        @SerializedName("safetyBadge")
        @Expose
        val propertySafetyBadge: PropertySafetyBadge = PropertySafetyBadge()
) : Visitable<PropertyAdapterTypeFactory> {

    override fun type(typeFactory: PropertyAdapterTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    data class Review(
            @SerializedName("reviewScore")
            @Expose
            val score: Float = 0.0f,

            @SerializedName("reviewDescription")
            @Expose
            val description: String = ""
    )

    data class Location(
            @SerializedName("cityName")
            @Expose
            val cityName: String = "",

            @SerializedName("description")
            @Expose
            val description: String = "",

            @SerializedName("latitude")
            @Expose
            val latitude: Float = 0f,

            @SerializedName("longitude")
            @Expose
            val longitude: Float = 0f
    )

    data class Image(
            @SerializedName("isLogoPhoto")
            @Expose
            val isLogoPhoto: Boolean = false,

            @SerializedName("urlSquare60")
            @Expose
            val urlSquare60: String = "",

            @SerializedName("mainPhoto")
            @Expose
            val mainPhoto: Boolean = false,

            @SerializedName("urlOriginal")
            @Expose
            val urlOriginal: String = "",

            @SerializedName("urlMax300")
            @Expose
            val urlMax300: String = ""
    )
}