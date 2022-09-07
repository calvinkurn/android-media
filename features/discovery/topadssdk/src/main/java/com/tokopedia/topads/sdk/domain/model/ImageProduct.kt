package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import org.json.JSONObject

private const val KEY_PRODUCT_ID = "product_id"
private const val KEY_PRODUCT_NAME = "product_name"
private const val KEY_IMAGE_URL = "image_url"
private const val KEY_IMAGE_CLICK_URL = "image_click_url"

data class ImageProduct(
    @SerializedName(KEY_PRODUCT_ID)
    @Expose
    var productId: String = "",

    @SerializedName(KEY_PRODUCT_NAME)
    @Expose
    var productName: String = "",

    @SerializedName(KEY_IMAGE_URL)
    @Expose
    var imageUrl: String = "",

    @SerializedName(KEY_IMAGE_CLICK_URL)
    @Expose
    var imageClickUrl: String = "",
) : ImpressHolder(), Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_PRODUCT_ID)) {
            productId = jSONObject.getString(KEY_PRODUCT_ID)
        }
        if (!jSONObject.isNull(KEY_PRODUCT_NAME)) {
            productName = jSONObject.getString(KEY_PRODUCT_NAME)
        }
        if (!jSONObject.isNull(KEY_IMAGE_URL)) {
            imageUrl = jSONObject.getString(KEY_IMAGE_URL)
        }
        if (!jSONObject.isNull(KEY_IMAGE_CLICK_URL)) {
            imageClickUrl = jSONObject.getString(KEY_IMAGE_CLICK_URL)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readString()
        parcel.readString()
        parcel.readString()
        parcel.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(productId)
        dest.writeString(productName)
        dest.writeString(imageUrl)
        dest.writeString(imageClickUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ImageProduct> = object : Parcelable.Creator<ImageProduct> {
            override fun createFromParcel(parcel: Parcel): ImageProduct {
                return ImageProduct(parcel)
            }

            override fun newArray(size: Int): Array<ImageProduct?> {
                return arrayOfNulls(size)
            }
        }
    }
}
