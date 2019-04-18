package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomInfo(
   @SerializedName("description")
   @Expose
   val description: String,

   @SerializedName("name")
   @Expose
   val name: String,

   @SerializedName("bathRoomCount")
   @Expose
   val bathRoomCount: Int,

   @SerializedName("size")
   @Expose
   val size: Int,

   @SerializedName("maxGuest")
   @Expose
   val maxGuest: Int,

   @SerializedName("facility")
   @Expose
   val facility: List<Facility>,

   @SerializedName("mainFacility")
   @Expose
   val mainFacility: List<Facility>,


   @SerializedName("roomImages")
   @Expose
   val roomImages: List<RoomImage>,


   @SerializedName("maxPrice")
   @Expose
   val maxPrice: Double,


   @SerializedName("minPrice")
   @Expose
   val minPrice: Double

   ) {
    data class Facility(
            @SerializedName("name")
            @Expose
            val name: String,

            @SerializedName("icon")
            @Expose
            val icon: String
    )

    data class RoomImage(
            @SerializedName("urlMax300")
            @Expose
            val url300: String,

            @SerializedName("urlOriginal")
            @Expose
            val urlOriginal: String,

            @SerializedName("urlSquare")
            @Expose
            val urlSquare: String
    )
}