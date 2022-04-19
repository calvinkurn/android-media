package com.tokopedia.digital.home.old.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
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
): Parcelable