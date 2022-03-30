package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by dhaba
 */
@Parcelize
data class ViewAllCard(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("contentType")
    val contentType: String = "",
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("description")
    val description: String = "",
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @Expose
    @SerializedName("gradientColor")
    val gradientColor: ArrayList<String> = arrayListOf()
) : Parcelable