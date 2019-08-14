package com.tokopedia.hotel.orderdetail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 09/05/19
 */

data class HotelTransportDetail(

        @SerializedName("paymentType")
        @Expose
        val paymentType: String = "",

        @SerializedName("isShowEVoucher")
        @Expose
        val isShowEVoucher: Boolean = false,

        @SerializedName("guestDetail")
        @Expose
        val guestDetail: TitleContent = TitleContent(),

        @SerializedName("propertyDetail")
        @Expose
        val propertyDetail: List<PropertyDetail> = listOf(),

        @SerializedName("cancellationPolicies")
        @Expose
        val cancellation: Cancellation = Cancellation(),

        @SerializedName("contactInfo")
        @Expose
        val contactInfo: List<ContactInfo> = listOf()
) {

    data class PropertyDetail(

            @SerializedName("propertyCountry")
            @Expose
            val propertyCountry: String = "",

            @SerializedName("appLink")
            @Expose
            val applink: String = "",

            @SerializedName("bookingKey")
            @Expose
            val bookingKey: TitleContent = TitleContent(),

            @SerializedName("image")
            @Expose
            val image: List<Image> = listOf(),

            @SerializedName("checkInOut")
            @Expose
            val checkInOut: List<CheckInOut> = listOf(),

            @SerializedName("stayLength")
            @Expose
            val stayLength: TitleContent = TitleContent(),

            @SerializedName("propertyInfo")
            @Expose
            val propertyInfo: PropertyInfo = PropertyInfo(),

            @SerializedName("room")
            @Expose
            val room: List<Room> = listOf(),

            @SerializedName("specialRequest")
            @Expose
            val specialRequest: TitleContent = TitleContent(),

            @SerializedName("extraInfo")
            @Expose
            val extraInfo: ExtraInfo = ExtraInfo()
    )

    data class Image(
            @SerializedName("urlMax360")
            @Expose
            val urlMax360: String = "",

            @SerializedName("urlSquare60")
            @Expose
            val urlSquare60: String = "",

            @SerializedName("urlOriginal")
            @Expose
            val urlOriginal: String = ""
    )

    data class CheckInOut(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("content")
            @Expose
            val content: String = "",

            @SerializedName("checkInOut")
            @Expose
            val checkInOut: CheckInOutData
    ) {
        data class CheckInOutData(
                @SerializedName("date")
                @Expose
                val date: String = "",

                @SerializedName("day")
                @Expose
                val day: String = "",

                @SerializedName("time")
                @Expose
                val time: String = ""
        )
    }

    data class PropertyInfo(
            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("address")
            @Expose
            val address: String = "",

            @SerializedName("starRating")
            @Expose
            val starRating: Int = 0
    )

    data class Room(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("content")
            @Expose
            val content: String = "",

            @SerializedName("amenities")
            @Expose
            val amenities: List<Amenity> = listOf()
    ) {
        data class Amenity(
                @SerializedName("content")
                @Expose
                val content: String
        )
    }

    data class Cancellation(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("content")
            @Expose
            val content: String = "",

            @SerializedName("isClickable")
            @Expose
            val isClickable: Boolean = false,

            @SerializedName("policies")
            @Expose
            val cancellationPolicies: List<CancellationPolicy> = listOf()
    ) {
        data class CancellationPolicy(
                @SerializedName("shortTitle")
                @Expose
                val shortTitle: String = "",

                @SerializedName("longTitle")
                @Expose
                val longTitle: String = "",

                @SerializedName("shortDesc")
                @Expose
                val shortDesc: String = "",

                @SerializedName("longDesc")
                @Expose
                val longDesc: String = "",

                @SerializedName("active")
                @Expose
                val active: Boolean = false
        )
    }

    data class ContactInfo(
            @SerializedName("number")
            @Expose
            val number: String = ""
    )

    data class ExtraInfo(
            @SerializedName("content")
            @Expose
            val content: String = "",

            @SerializedName("uri")
            @Expose
            val uri: String = "",

            @SerializedName("uriWeb")
            @Expose
            val uriWeb: String = "",

            @SerializedName("isClickable")
            @Expose
            val isClickable: Boolean = false,

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("shortDesc")
            @Expose
            val shortDesc: String = "",

            @SerializedName("longDesc")
            @Expose
            val longDesc: String = ""

    )
}