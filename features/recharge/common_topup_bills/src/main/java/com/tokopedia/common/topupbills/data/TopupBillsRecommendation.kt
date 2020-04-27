package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 28/05/19.
 */
class TopupBillsRecommendation(
        @SerializedName("iconUrl")
        @Expose
        val iconUrl: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("clientNumber")
        @Expose
        val clientNumber: String = "",
        @SerializedName("appLink")
        @Expose
        val applink: String = "",
        @SerializedName("webLink")
        @Expose
        val weblink: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("categoryId")
        @Expose
        val categoryId: Int = 0,
        @SerializedName("productId")
        @Expose
        val productId: Int = 0,
        @SerializedName("isATC")
        @Expose
        val isAtc: Boolean = false,
        @SerializedName("operatorID")
        @Expose
        val operatorId: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = "",
        var position: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(iconUrl)
                parcel.writeString(title)
                parcel.writeString(clientNumber)
                parcel.writeString(applink)
                parcel.writeString(weblink)
                parcel.writeString(type)
                parcel.writeInt(categoryId)
                parcel.writeInt(productId)
                parcel.writeByte(if (isAtc) 1 else 0)
                parcel.writeInt(operatorId)
                parcel.writeString(description)
                parcel.writeInt(position)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<TopupBillsRecommendation> {
                override fun createFromParcel(parcel: Parcel): TopupBillsRecommendation {
                        return TopupBillsRecommendation(parcel)
                }

                override fun newArray(size: Int): Array<TopupBillsRecommendation?> {
                        return arrayOfNulls(size)
                }
        }


}