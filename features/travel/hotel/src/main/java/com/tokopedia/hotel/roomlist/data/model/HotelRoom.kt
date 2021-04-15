package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.roomlist.presentation.adapter.RoomListTypeFactory

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoom(
        @SerializedName("ID")
        @Expose
        val roomId: String = "",

        @SerializedName("available")
        @Expose
        val available: Boolean = true,

        @SerializedName("roomQtyRequired")
        @Expose
        val roomQtyReqiured: Int = 1,

        @SerializedName("breakfastInfo")
        @Expose
        val breakfastInfo: RoomBreakfastInfo = RoomBreakfastInfo(),

        @SerializedName("occupancyInfo")
        @Expose
        val occupancyInfo: RoomOccupancyInfo = RoomOccupancyInfo(),

        @SerializedName("depositInfo")
        @Expose
        val depositInfo: DepositInfo = DepositInfo(),

        @SerializedName("refundInfo")
        @Expose
        val refundInfo: RefundInfo = RefundInfo(),

        @SerializedName("creditcardInfo")
        @Expose
        val creditCardInfo: CreditCardInfo = CreditCardInfo(),

        @SerializedName("numberRoomLeft")
        @Expose
        val numberRoomLeft: Int = 0,

        @SerializedName("roomPrice")
        @Expose
        val roomPrice: HotelRoomPrice = HotelRoomPrice(),

        @SerializedName("roomPolicy")
        @Expose
        val roomPolicy: List<RoomPolicy> = listOf(),

        @SerializedName("cancelPolicy")
        @Expose
        val cancelPolicy: List<CancelPolicy> = listOf(),

        @SerializedName("refundableUntil")
        @Expose
        val refundableUntil: String = "",

        @SerializedName("roomInfo")
        @Expose
        val roomInfo: HotelRoomInfo = HotelRoomInfo(),

        @SerializedName("bedInfo")
        @Expose
        val bedInfo: String = "",

        @SerializedName("taxes")
        @Expose
        val taxes: String = "",

        @SerializedName("extraBedInfo")
        @Expose
        val extraBedInfo: ExtraBedInfo = ExtraBedInfo(),

        var additionalPropertyInfo: AdditionalPropertyInfo = AdditionalPropertyInfo(),

        @SerializedName("isDirectPayment")
        @Expose
        val isDirectPayment: Boolean = false,

        @SerializedName("isDirectPaymentString")
        @Expose
        val isDirectPaymentString: String = ""

) : Visitable<RoomListTypeFactory> {

    override fun type(typeFactory: RoomListTypeFactory) = typeFactory.type(this)

    data class RoomBreakfastInfo(
            @SerializedName("isBreakFastIncluded")
            @Expose
            val isBreakfastIncluded: Boolean = false,

            @SerializedName("mealPlan")
            @Expose
            val mealPlan: String = "",

            @SerializedName("breakFast")
            @Expose
            val breakFast: String = "",

            @SerializedName("icon")
            @Expose
            val icon: String = "",

            @SerializedName("iconUrl")
            @Expose
            val iconUrl: String = ""
    )

    data class RoomOccupancyInfo(
            @SerializedName("maxOccupancy")
            @Expose
            val maxOccupancy: Int = 0,

            @SerializedName("maxFreeChild")
            @Expose
            val maxFreeChild: Int = 0,

            @SerializedName("occupancyText")
            @Expose
            val occupancyText: String = ""
    )

    data class DepositInfo(
            @SerializedName("isNeedDeposit")
            @Expose
            val isNeedDeposit: Boolean = false,

            @SerializedName("depositText")
            @Expose
            val depositText: String = ""
    )

    data class RefundInfo(
            @SerializedName("isRefundAble")
            @Expose
            val isRefundable: Boolean = false,

            @SerializedName("refundStatus")
            @Expose
            val refundStatus: String = "",

            @SerializedName("icon")
            @Expose
            val icon: String = "",

            @SerializedName("iconUrl")
            @Expose
            val iconUrl: String = ""
    )

    data class CreditCardInfo(
            @SerializedName("isCCRequired")
            @Expose
            val isCCRequired: Boolean = false,

            @SerializedName("isCCDomesticRequired")
            @Expose
            val isCCDomesticRequired: Boolean = false,

            @SerializedName("info")
            @Expose
            val creditCardInfo: String = "",

            @SerializedName("header")
            @Expose
            val header: String = ""
    )

    data class RoomPolicy(
            @SerializedName("class")
            @Expose
            val roomPolicyClass: String = "",

            @SerializedName("content")
            @Expose
            val content: String = ""
    )

    data class CancelPolicy(
            @SerializedName("subHeader")
            @Expose
            val subheader: String = "",

            @SerializedName("content")
            @Expose
            val content: String = ""
    )

    data class ExtraBedInfo(
            @SerializedName("IsFreeExtraBed")
            @Expose
            val isFreeExtraBed: Boolean = false,

            @SerializedName("Content")
            @Expose
            val content: String = ""
    )

    data class AdditionalPropertyInfo(

            val propertyId: Int = 0,

            val isAddressRequired: Boolean = false,

            val isCvCRequired: Boolean = false,

            val isDirectPayment: Boolean = false,

            val isEnabled: Boolean = false,

            val propertyName: String = "",

            val hotelTagging: String = ""
    )
}



