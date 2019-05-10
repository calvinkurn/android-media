package com.tokopedia.hotel.orderdetail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 09/05/19
 */

data class HotelTransportDetail(
        @SerializedName("fares")
        @Expose
        val fares: List<Fare> = listOf(),

        @SerializedName("conditionalInfoTop")
        @Expose
        val conditionalInfoTop: ConditionalInfo = ConditionalInfo(),

        @SerializedName("conditionalInfoBottom")
        @Expose
        val conditionalInfoBottom: ConditionalInfo = ConditionalInfo(),

        @SerializedName("guestDetail")
        @Expose
        val guestDetail: TitleText = TitleText(),

        @SerializedName("propertyDetail")
        @Expose
        val propertyDetail: PropertyDetail = PropertyDetail(),

        @SerializedName("payment")
        @Expose
        val payment: Payment = Payment(),

        @SerializedName("cancellation")
        @Expose
        val cancellation: Cancellation = Cancellation()
) {
    data class Fare(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class ConditionalInfo(
            @SerializedName("text")
            @Expose
            val text: String = ""
    )

    data class TitleText(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("text")
            @Expose
            val text: String = ""
    )

    data class PropertyDetail(
            @SerializedName("bookingKey")
            @Expose
            val bookingKey: TitleText = TitleText(),

            @SerializedName("image")
            @Expose
            val image: List<Image> = listOf(),

            @SerializedName("checkInOut")
            @Expose
            val checkInOut: List<CheckInOut> = listOf(),

            @SerializedName("propertyInfo")
            @Expose
            val propertyInfo: PropertyInfo = PropertyInfo(),

            @SerializedName("room")
            @Expose
            val room: List<Room> = listOf(),

            @SerializedName("specialRequest")
            @Expose
            val specialRequest: List<TitleText> = listOf(),

            @SerializedName("extraInfo")
            @Expose
            val extraInfo: String = ""
    )

    data class Image(
            @SerializedName("urlSquare60")
            @Expose
            val urlSquare60: String = "",

            @SerializedName("urlOriginal")
            @Expose
            val urlOriginal: String = "",

            @SerializedName("urlMax300")
            @Expose
            val urlMax300: String = ""
    )

    data class CheckInOut(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("text")
            @Expose
            val text: String = "",

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

            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("amenities")
            @Expose
            val amenities: List<Amenity> = listOf()
    ) {
        data class Amenity(
                @SerializedName("text")
                @Expose
                val text: String
        )
    }

    data class Payment(
            @SerializedName("detail")
            @Expose
            val detail: List<TitleText> = listOf(),

            @SerializedName("summary")
            @Expose
            val summary: List<TitleText> = listOf()
    )

    data class Cancellation(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("cancellationPolicies")
            @Expose
            val cancellationPolicies: List<CancellationPolicy> = listOf(),

            @SerializedName("contactInfo")
            @Expose
            val contactInfo: List<ContactInfo> = listOf()
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
                val active: Boolean = false,

                @SerializedName("isClickable")
                @Expose
                val isClickable: Boolean = false
        )

        data class ContactInfo(
                @SerializedName("number")
                @Expose
                val number: String = ""
        )
    }
}