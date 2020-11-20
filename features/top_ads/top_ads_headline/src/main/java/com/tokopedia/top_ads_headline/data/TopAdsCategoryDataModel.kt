package com.tokopedia.top_ads_headline.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopAdsCategoryDataModel(val count: Int = 0,
                                   val id: String = "",
                                   val name: String = "",
                                   val type: Int = 0,
                                   var isSelected: Boolean = false) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other is TopAdsCategoryDataModel && other.id == this.id && other.name == this.name
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}