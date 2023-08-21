package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

private const val KEY_PRODUCT_ID = "product_id"
private const val KEY_PRODUCT_NAME = "product_name"
private const val KEY_IMAGE_URL = "image_url"
private const val KEY_IMAGE_CLICK_URL = "image_click_url"

@Parcelize
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
) : ImpressHolder(), Parcelable
