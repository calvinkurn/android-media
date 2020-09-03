package com.tokopedia.shop.common.graphql.data.shopinfo

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Parcelize
data class BBInfo(
        @SerializedName("bbName")
        @Expose
        val name: String = "",

        @SerializedName("bbDesc")
        @Expose
        val desc: String = "",

        @SerializedName("bbNameEN")
        @Expose
        val nameEN: String = "",

        @SerializedName("bbDescEN")
        @Expose
        val descEN: String = ""
): Parcelable