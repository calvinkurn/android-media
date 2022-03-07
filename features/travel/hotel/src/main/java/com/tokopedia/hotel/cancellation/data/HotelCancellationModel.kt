package com.tokopedia.hotel.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 27/04/20
 */

data class HotelCancellationModel(
        @SerializedName("cancelCartID")
        @Expose
        var cancelCartId: String = "",

        @SerializedName("cancelCartExpiry")
        @Expose
        val cancelCartExpiry: String = "",

        @SerializedName("property")
        @Expose
        val property: PropertyData = PropertyData(),

        @SerializedName("cancelPolicy")
        @Expose
        val cancelPolicy: CancelPolicy = CancelPolicy(),

        @SerializedName("cancelInfo")
        @Expose
        val cancelInfo: CancelInfo = CancelInfo(),

        @SerializedName("payment")
        @Expose
        val payment: PaymentData = PaymentData(),

        @SerializedName("reasons")
        @Expose
        val reasons: List<Reason> = listOf(),

        @SerializedName("footer")
        @Expose
        val footer: Footer = Footer(),

        @SerializedName("confirmationButton")
        @Expose
        val confirmationButton: ConfirmationButton = ConfirmationButton()
) {

    data class ConfirmationButton(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("desc")
            @Expose
            val desc: String = ""
    )

    data class Footer(
            @SerializedName("desc")
            @Expose
            val desc: String = "",

            @SerializedName("links")
            @Expose
            val links: List<String> = listOf()
    )

    data class Reason(
          @SerializedName("id")
          @Expose
          val id: String = "",

          @SerializedName("title")
          @Expose
          val title: String = "",

          @SerializedName("freeText")
          @Expose
          val freeText: Boolean = false
    )

    data class PaymentData(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("detail")
            @Expose
            val detail: List<PaymentDetail> = listOf(),

            @SerializedName("summary")
            @Expose
            val summary: List<PaymentDetail> = listOf(),

            @SerializedName("footer")
            @Expose
            val footer: Footer = Footer()
    ) {

        data class PaymentDetail(
                @SerializedName("title")
                @Expose
                val title: String = "",

                @SerializedName("amount")
                @Expose
                val amount: String = ""
        )
    }
    data class CancelInfo(
            @SerializedName("desc")
            @Expose
            val desc: String = "",

            @SerializedName("isClickable")
            @Expose
            val isClickable: Boolean = false,

            @SerializedName("longDesc")
            @Expose
            val longDesc: DescriptionData = DescriptionData()
    ) {
        data class DescriptionData(
                @SerializedName("title")
                @Expose
                val title: String = "",

                @SerializedName("desc")
                @Expose
                val desc: String = ""
        )
    }

    data class CancelPolicy(
           @SerializedName("title")
           @Expose
           val title: String = "",

           @SerializedName("policy")
           @Expose
           val policy: List<Policy> = listOf()
    ) {
        data class Policy(
                @SerializedName("title")
                @Expose
                val title: String = "",

                @SerializedName("desc")
                @Expose
                val desc: String  = "",

                @SerializedName("active")
                @Expose
                val active: Boolean = false,

                @SerializedName("feeInLocalCurrency")
                @Expose
                val feeInLocalCurrency: CurrencyData = CurrencyData(),

                @SerializedName("fee")
                @Expose
                val fee: CurrencyData = CurrencyData(),

                @SerializedName("styling")
                @Expose
                val styling: String = ""
        ) {
            data class CurrencyData(
                    @SerializedName("amountStr")
                    @Expose
                    val amountStr: String = "",

                    @SerializedName("amount")
                    @Expose
                    val amount: Long = 0,

                    @SerializedName("currency")
                    @Expose
                    val currency: String = ""
            )
        }
    }

    data class PropertyData(
            @SerializedName("propertyID")
            @Expose
            val propertyId: Long = 0,

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("checkInOut")
            @Expose
            val checkInOut: List<CheckInOut> = listOf(),

            @SerializedName("stayLength")
            @Expose
            val stayLength: String = "",

            @SerializedName("isDirectPayment")
            @Expose
            val isDirectPayment: Boolean = false,

            @SerializedName("room")
            @Expose
            val room: List<HotelRoomData> = listOf()
    ) {
        data class CheckInOut(
                @SerializedName("title")
                @Expose
                val title: String = "",

                @SerializedName("checkInOut")
                @Expose
                val checkInOut: CheckInOutData = CheckInOutData()
        ) {
            data class CheckInOutData(
                    @SerializedName("day")
                    @Expose
                    val day: String = "",

                    @SerializedName("date")
                    @Expose
                    val date: String = "",

                    @SerializedName("time")
                    @Expose
                    val time: String = ""
            )
        }

        data class HotelRoomData(
                @SerializedName("isBreakFastIncluded")
                @Expose
                val isBreakfastIncluded: Boolean = false,

                @SerializedName("maxOccupancy")
                @Expose
                val maxOccupancy: Int = 0,

                @SerializedName("roomName")
                @Expose
                val roomName: String = "",

                @SerializedName("roomContent")
                @Expose
                val roomContent: String = "",

                @SerializedName("isRefundAble")
                @Expose
                val isRefundable: Boolean = false,

                @SerializedName("isCCRequired")
                @Expose
                val isCCRequired: Boolean = false
        )
    }

    data class Response(
            @SerializedName("propertyGetCancellation")
            @Expose
            val response: CancellationDataAndMeta = CancellationDataAndMeta()
    )

    data class CancellationDataAndMeta(
            @SerializedName("data")
            @Expose
            val data: HotelCancellationModel = HotelCancellationModel(),

            @SerializedName("meta")
            @Expose
            val meta: CancellationMeta = CancellationMeta(),

            @Expose
            @SerializedName("content")
            val content: HotelCancellationError.Content = HotelCancellationError.Content()
    )

    data class CancellationMeta(
            @SerializedName("invoiceID")
            @Expose
            val invoiceId: String = ""
    )
}