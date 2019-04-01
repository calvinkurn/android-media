package com.tokopedia.hotel.search.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory

data class Property(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("address")
        @Expose
        val address: String = "",

        @SerializedName("priceAmount")
        @Expose
        val priceAmount: Float = 0f,

        @SerializedName("price")
        @Expose
        val price: String = "0",

        @SerializedName("image")
        @Expose
        val image: String = "",

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
        val location: Location = Location()
): Visitable<PropertyAdapterTypeFactory> {

        override fun type(typeFactory: PropertyAdapterTypeFactory?): Int {
            return typeFactory?.type(this) ?: 0
        }

        data class Review(
            @SerializedName("reviewScore")
            @Expose
            val score: Int = 0,

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
}