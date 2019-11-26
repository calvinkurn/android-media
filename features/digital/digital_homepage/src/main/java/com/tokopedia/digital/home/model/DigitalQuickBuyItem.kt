package com.tokopedia.digital.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DigitalQuickBuyItem(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("title_1")
        @Expose
        val title1st: String = "",
        @SerializedName("desc_1")
        @Expose
        val desc1st: String = "",
        @SerializedName("title_2")
        @Expose
        val title2nd: String = "",
        @SerializedName("desc_2")
        @Expose
        val desc2nd: String = "",
        @SerializedName("tag_name")
        @Expose
        val tagName: String = "",
        @SerializedName("tag_type")
        @Expose
        val tagType: Int = 0,
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",
        @SerializedName("price_prefix")
        @Expose
        val pricePrefix: String = "",
        @SerializedName("template_id")
        @Expose
        val templateId: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeString(url)
        parcel.writeString(applink)
        parcel.writeString(title1st)
        parcel.writeString(desc1st)
        parcel.writeString(title2nd)
        parcel.writeString(desc2nd)
        parcel.writeString(tagName)
        parcel.writeInt(tagType)
        parcel.writeString(price)
        parcel.writeString(originalPrice)
        parcel.writeString(pricePrefix)
        parcel.writeInt(templateId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalQuickBuyItem> {
        override fun createFromParcel(parcel: Parcel): DigitalQuickBuyItem {
            return DigitalQuickBuyItem(parcel)
        }

        override fun newArray(size: Int): Array<DigitalQuickBuyItem?> {
            return arrayOfNulls(size)
        }
    }
}