package com.tokopedia.flashsale.management.common.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SellerStatus(
        @SerializedName("is_eligible")
        @Expose val isEligible: Boolean = false,

        @SerializedName("is_god_seller")
        @Expose val isGodSeller: Boolean = false,

        @SerializedName("is_shop_active")
        @Expose val isShopActive: Boolean = false,

        @SerializedName("is_visible")
        @Expose val isVisible: Boolean = false,

        @SerializedName("join_status")
        @Expose val joinStatus: Boolean = false,

        @SerializedName("show_onboard")
        @Expose val showOnboard: Boolean = false,

        @SerializedName("submit_status")
        @Expose val submitStatus: Boolean = false,

        @SerializedName("ticker_message")
        @Expose val tickerMessage: String = ""
): Parcelable{

        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readString()) {
        }

        data class Response(
                @SerializedName("getCampaignSellerStatus")
                val getCampaignSellerStatus: GetCampaignSellerStatus
        )

        data class GetCampaignSellerStatus(
                @SerializedName("data")
                val sellerStatus: SellerStatus
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (isEligible) 1 else 0)
                parcel.writeByte(if (isGodSeller) 1 else 0)
                parcel.writeByte(if (isShopActive) 1 else 0)
                parcel.writeByte(if (isVisible) 1 else 0)
                parcel.writeByte(if (joinStatus) 1 else 0)
                parcel.writeByte(if (showOnboard) 1 else 0)
                parcel.writeByte(if (submitStatus) 1 else 0)
                parcel.writeString(tickerMessage)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<SellerStatus> {
                override fun createFromParcel(parcel: Parcel): SellerStatus {
                        return SellerStatus(parcel)
                }

                override fun newArray(size: Int): Array<SellerStatus?> {
                        return arrayOfNulls(size)
                }
        }
}