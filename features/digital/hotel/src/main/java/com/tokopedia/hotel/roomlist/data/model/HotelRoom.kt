package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoom(
        @SerializedName("ID")
        @Expose
        val roomId: String,

        @SerializedName("breakfastInfo")
        @Expose
        val breakfastInfo: RoomBreakfastInfo,

        @SerializedName("occupancyInfo")
        @Expose
        val occupancyInfo: RoomOccupancyInfo,

        @SerializedName("depositInfo")
        @Expose
        val depositInfo: DepositInfo,

        @SerializedName("refundInfo")
        @Expose
        val refundInfo: RefundInfo,

        @SerializedName("creditcardInfo")
        @Expose
        val creditCardInfo: CreditCardInfo,

        @SerializedName("numberRoomLeft")
        @Expose
        val numberRoomLeft: Int,

        @SerializedName("roomPrice")
        @Expose
        val roomPrice: List<HotelRoomPrice>,

        @SerializedName("roomPolicy")
        @Expose
        val roomPolicy: List<RoomPolicy>,

        @SerializedName("cancelPolicy")
        @Expose
        val cancelPolicy: List<CancelPolicy>,

        @SerializedName("refundableUntil")
        @Expose
        val refundableUntil: String,


        @SerializedName("roomInfo")
        @Expose
        val roomInfo: HotelRoomInfo,


        @SerializedName("bedInfo")
        @Expose
        val bedInfo: String,


        @SerializedName("taxes")
        @Expose
        val taxes: String,

        @SerializedName("extraBedInfo")
        @Expose
        val extraBedInfo: ExtraBedInfo

        ) {
    data class RoomBreakfastInfo(
            @SerializedName("isBreakFastIncluded")
            @Expose
            val isBreakfastIncluded: Boolean,

            @SerializedName("mealPlan")
            @Expose
            val mealPlan: String,

            @SerializedName("breakFast")
            @Expose
            val breakFast: String
    )

    data class RoomOccupancyInfo(
            @SerializedName("maxOccupancy")
            @Expose
            val maxOccupancy: Int,

            @SerializedName("maxFreeChild")
            @Expose
            val maxFreeChild: Int,

            @SerializedName("occupancyText")
            @Expose
            val occupancyText: String
    )

    data class DepositInfo(
            @SerializedName("isNeedDeposit")
            @Expose
            val isNeedDeposit: Boolean,

            @SerializedName("depositText")
            @Expose
            val depositText: String
    )

    data class RefundInfo(
            @SerializedName("isRefundAble")
            @Expose
            val isRefundable: Boolean,

            @SerializedName("refundStatus")
            @Expose
            val refundStatus: String
    )

    data class CreditCardInfo(
            @SerializedName("isCCRequired")
            @Expose
            val isCCRequired: Boolean,

            @SerializedName("info")
            @Expose
            val creditCardInfo: String
    )

    data class RoomPolicy(
            @SerializedName("class")
            @Expose
            val roomPolicyClass: String,

            @SerializedName("content")
            @Expose
            val content: String
    )

    data class CancelPolicy(
            @SerializedName("subHeader")
            @Expose
            val subheader: String,

            @SerializedName("content")
            @Expose
            val content: String
    )

    data class ExtraBedInfo(
            @SerializedName("IsFreeExtraBed")
            @Expose
            val isFreeExtraBed: Boolean,

            @SerializedName("Content")
            @Expose
            val content: String
    )

}



