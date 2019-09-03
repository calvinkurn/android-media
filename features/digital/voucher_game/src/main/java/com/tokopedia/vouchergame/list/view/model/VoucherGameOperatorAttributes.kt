package com.tokopedia.vouchergame.list.view.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam

class VoucherGameOperatorAttributes(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image")
        @Expose
        val image: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("help_cta")
        @Expose
        val helpCta: String = "",
        @SerializedName("help_text")
        @Expose
        val helpText: String = "",
        @SerializedName("help_image")
        @Expose
        val helpImage: String = "",
        @SerializedName("operator_label")
        @Expose
        val operatorLabel: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(imageUrl)
        parcel.writeString(description)
        parcel.writeString(helpCta)
        parcel.writeString(helpText)
        parcel.writeString(helpImage)
        parcel.writeString(operatorLabel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VoucherGameOperatorAttributes> {
        override fun createFromParcel(parcel: Parcel): VoucherGameOperatorAttributes {
            return VoucherGameOperatorAttributes(parcel)
        }

        override fun newArray(size: Int): Array<VoucherGameOperatorAttributes?> {
            return arrayOfNulls(size)
        }
    }
}
