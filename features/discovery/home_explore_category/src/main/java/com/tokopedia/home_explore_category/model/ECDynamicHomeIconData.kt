package com.tokopedia.exploreCategory.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ECDynamicHomeIconData(
        @SerializedName("dynamicHomeIcon")
        val dynamicHomeIcon: DynamicHomeIcon?
) : Parcelable {
    @Parcelize
    data class DynamicHomeIcon(
            @SerializedName("categoryGroup")
            val categoryGroup: List<CategoryGroup?>?
    ) : Parcelable {
        @Parcelize
        data class CategoryGroup(
                @SerializedName("id")
                val id: Int,
                @SerializedName("title")
                val title: String?,
                @SerializedName("desc")
                val desc: String?,
                @SerializedName("categoryRows")
                val categoryRows: List<CategoryRow?>?,
                @SerializedName("isClosed")
                var isOpen : Boolean = false
        ) : Parcelable {
            @Parcelize
            data class CategoryRow(
                    @SerializedName("id")
                    val id: Int,
                    @SerializedName("name")
                    val name: String?,
                    @SerializedName("url")
                    val url: String?,
                    @SerializedName("imageUrl")
                    val imageUrl: String?,
                    @SerializedName("applinks")
                    val applinks: String?,
                    @SerializedName("categoryLabel")
                    val categoryLabel: String?,
                    @SerializedName("bu_identifier")
                    val buIdentifier: String?
            ) : Parcelable
        }
    }
}