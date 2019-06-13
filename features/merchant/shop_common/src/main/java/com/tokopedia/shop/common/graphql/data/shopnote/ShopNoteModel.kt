package com.tokopedia.shop.common.graphql.data.shopnote

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 08/08/18.
 */

data class ShopNoteModel(@SerializedName("id")
                         @Expose
                         val id: String? = null,
                         @SerializedName("title")
                         @Expose
                         val title: String? = null,
                         @SerializedName("content")
                         @Expose
                         val content: String? = null,
                         @SerializedName("isTerms")
                         @Expose
                         val terms: Boolean = false,
                         @SerializedName("position")
                         @Expose
                         val position: Int = 0,
                         @SerializedName("url")
                         @Expose
                         val url: String = "",
                         @SerializedName("updateTime")
                         @Expose
                         val updateTime: String? = null) {

}
