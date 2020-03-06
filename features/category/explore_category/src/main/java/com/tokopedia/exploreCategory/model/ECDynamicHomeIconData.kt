package com.tokopedia.exploreCategory.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ECDynamicHomeIconData(
        @SerializedName("dynamicHomeIcon")
        val dynamicHomeIcon: DynamicHomeIcon?
) {
    data class DynamicHomeIcon(
            @SerializedName("categoryGroup")
            val categoryGroup: List<CategoryGroup?>?
    ) {
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
        ) {
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
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readInt(),
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString()) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeInt(id)
                    parcel.writeString(name)
                    parcel.writeString(url)
                    parcel.writeString(imageUrl)
                    parcel.writeString(applinks)
                    parcel.writeString(categoryLabel)
                    parcel.writeString(buIdentifier)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<CategoryRow> {
                    override fun createFromParcel(parcel: Parcel): CategoryRow {
                        return CategoryRow(parcel)
                    }

                    override fun newArray(size: Int): Array<CategoryRow?> {
                        return arrayOfNulls(size)
                    }
                }
            }
        }
    }
}