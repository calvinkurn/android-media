package com.tokopedia.common.topupbills.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 02/09/19.
 */
class TopupBillsBanner(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("img_url")
        @Expose
        val imageUrl: String,
        @SerializedName("link_url")
        @Expose
        val linkUrl: String,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("promo_code")
        @Expose
        val promoCode: String,
        @SerializedName("app_link")
        @Expose
        val applinkUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imageUrl)
        parcel.writeString(linkUrl)
        parcel.writeString(title)
        parcel.writeString(promoCode)
        parcel.writeString(applinkUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopupBillsBanner> {
        override fun createFromParcel(parcel: Parcel): TopupBillsBanner {
            return TopupBillsBanner(parcel)
        }

        override fun newArray(size: Int): Array<TopupBillsBanner?> {
            return arrayOfNulls(size)
        }
    }

}