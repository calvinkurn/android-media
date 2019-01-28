package com.tokopedia.topads.common.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VoucherShop(
        @SerializedName("benefit_type")
        @Expose
        val benefitType: String? = null,
        @SerializedName("benefit_value")
        @Expose
        val benefitValue: Int = 0,
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
        @SerializedName("max_limit")
        @Expose
        val maxLimit: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("period_lang_en")
        @Expose
        val periodLangEN: String = "",
        @SerializedName("period_lang_id")
        @Expose
        val periodLangID: String = "",
        @SerializedName("remaining_days")
        @Expose
        val remainingDays: Int = 0): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(benefitType)
                parcel.writeInt(benefitValue)
                parcel.writeString(code)
                parcel.writeString(link)
                parcel.writeInt(maxLimit)
                parcel.writeString(name)
                parcel.writeString(periodLangEN)
                parcel.writeString(periodLangID)
                parcel.writeInt(remainingDays)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<VoucherShop> {
                override fun createFromParcel(parcel: Parcel): VoucherShop {
                        return VoucherShop(parcel)
                }

                override fun newArray(size: Int): Array<VoucherShop?> {
                        return arrayOfNulls(size)
                }
        }
}