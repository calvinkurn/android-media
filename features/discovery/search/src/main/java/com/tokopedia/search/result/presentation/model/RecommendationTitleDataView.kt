package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class RecommendationTitleDataView(
        val title: String = "",
        val seeMoreUrl: String = "",
        val pageName: String = "",
) : Parcelable, Visitable<ProductListTypeFactory?> {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(seeMoreUrl)
        dest.writeString(pageName)
    }

    constructor(parcel: Parcel): this(
            title = parcel.readString() ?: "",
            seeMoreUrl = parcel.readString() ?: "",
            pageName = parcel.readString() ?: "",
    )

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RecommendationTitleDataView> = object : Parcelable.Creator<RecommendationTitleDataView> {
            override fun createFromParcel(parcel: Parcel): RecommendationTitleDataView {
                return RecommendationTitleDataView(parcel)
            }

            override fun newArray(size: Int): Array<RecommendationTitleDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}