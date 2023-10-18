package com.tokopedia.smartbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SmartBillsCatalogMenu(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("app_link")
        @Expose
        val applink: String = "",
        @SerializedName("slug_name")
        @Expose
        val slugName: String = "",
): Parcelable
