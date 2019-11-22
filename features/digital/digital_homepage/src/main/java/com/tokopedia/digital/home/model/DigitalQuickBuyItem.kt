package com.tokopedia.digital.home.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DigitalQuickBuyItem(
        @SerializedName("id")
        @Expose
        val id: Int? = null,
        @SerializedName("name")
        @Expose
        val name: String? = null,
        @SerializedName("image_url")
        @Expose
        val imageUrl: String? = null,
        @SerializedName("url")
        @Expose
        val url: String? = null,
        @SerializedName("applink")
        @Expose
        val applink: String? = null,
        @SerializedName("title_1")
        @Expose
        val title1st: String? = null,
        @SerializedName("desc_1")
        @Expose
        val desc1st: String? = null,
        @SerializedName("title_2")
        @Expose
        val title2nd: String? = null,
        @SerializedName("desc_2")
        @Expose
        val desc2nd: String? = null,
        @SerializedName("tag_name")
        @Expose
        val tagName: String? = null,
        @SerializedName("tag_type")
        @Expose
        val tagType: Int,
        @SerializedName("price")
        @Expose
        val price: String? = null,
        @SerializedName("original_price")
        @Expose
        val originalPrice: String? = null,
        @SerializedName("price_prefix")
        @Expose
        val pricePrefix: String? = null,
        @SerializedName("template_id")
        @Expose
        val templateId: Int? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
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
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
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
        parcel.writeValue(templateId)
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