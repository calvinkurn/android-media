package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomInfo(
   @SerializedName("description")
   @Expose
   val description: String = "",

   @SerializedName("name")
   @Expose
   val name: String = "",

   @SerializedName("bathRoomCount")
   @Expose
   val bathRoomCount: Int = 0,

   @SerializedName("size")
   @Expose
   val size: Int = 0,

   @SerializedName("maxGuest")
   @Expose
   val maxGuest: Int = 0,

   @SerializedName("facility")
   @Expose
   val facility: List<Facility> = listOf(),

   @SerializedName("MAIN_FACILITY")
   @Expose
   val mainFacility: List<Facility> = listOf(),


   @SerializedName("roomImages")
   @Expose
   val roomImages: List<RoomImage> = listOf(),


   @SerializedName("maxPrice")
   @Expose
   val maxPrice: Double = 0.0,


   @SerializedName("minPrice")
   @Expose
   val minPrice: Double = 0.0

   ) {
    data class Facility(
            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("icon")
            @Expose
            val icon: String = "",

            @SerializedName("iconUrl")
            @Expose
            val iconUrl: String = ""
    )

    data class RoomImage(
            @SerializedName("urlMax300")
            @Expose
            val url300: String = "",

            @SerializedName("urlOriginal")
            @Expose
            val urlOriginal: String = "",

            @SerializedName("urlSquare")
            @Expose
            val urlSquare: String = ""
    )
}