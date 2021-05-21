package com.tokopedia.catalog.model.raw

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ComponentData(
        @SerializedName("name")
        val name : String?,
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: String?,
        @SerializedName("icon")
        val icon: String?,
        @SerializedName("row")
        val specificationsRow : List<SpecificationsRow>? = listOf(),
        @SerializedName("url")
        val url : String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("videoId")
        val videoId : String?,
        @SerializedName("thumbnail")
        val thumbnail : String?,
        @SerializedName("title")
        val title : String?,
        @SerializedName("author")
        val author : String?

){
    @Parcelize
    data class SpecificationsRow  (
            @SerializedName("key")
            val key: String,
            @SerializedName("value")
            val value: String
    ) : Parcelable

}

@Parcelize
data class FullSpecificationsComponentData(
        val name : String?,
        val icon : String?,
        val specificationsRow : List<ComponentData.SpecificationsRow>?) : Parcelable


@Parcelize
data class TopSpecificationsComponentData(
        val key : String?,
        val value : String?,
        val icon : String?) : Parcelable

@Parcelize
data class VideoComponentData(
        val url : String?,
        val type : String?,
        val videoId : String?,
        val thumbnail : String?,
        val title : String?,
        val author : String?) : Parcelable
