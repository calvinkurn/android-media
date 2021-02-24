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
        val specificationsRow : List<SpecificationsRow>? = listOf()

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