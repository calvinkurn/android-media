package com.tokopedia.recommendation_widget_common

import android.os.Parcel
import android.os.Parcelable

data class RecommendationParams(var userId: Int = 0,
                                var source: String? = DEFAULT_VALUE_X_SOURCE,
                                var pageNumber: Int = 0,
                                var pageName: String? = DEFAULT_PAGE_NAME,
                                var productIds : ArrayList<Int> = arrayListOf()): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            arrayListOf<Int>().apply {
                parcel.readList(this, Integer::class.java.classLoader)
            }) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(source)
        parcel.writeInt(pageNumber)
        parcel.writeString(pageName)
        parcel.writeList(productIds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecommendationParams> {
        override fun createFromParcel(parcel: Parcel): RecommendationParams {
            return RecommendationParams(parcel)
        }

        override fun newArray(size: Int): Array<RecommendationParams?> {
            return arrayOfNulls(size)
        }
    }
}