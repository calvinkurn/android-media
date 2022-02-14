package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class SuggestionDataView(
        val suggestionText: String = "",
        val suggestedQuery: String = "",
        val suggestion: String = "",
        val componentId: String = "",
        val trackingOption: Int = 0,
        val keyword: String = "",
        val dimension90: String = "",
        val trackingValue: String = "",
) : ImpressHolder(),
    Parcelable,
    Visitable<ProductListTypeFactory?>,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keyword,
        dimension90 = dimension90,
        valueName = trackingValue,
        componentId = componentId,
        applink = suggestedQuery,
    ) {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(suggestionText)
        dest.writeString(suggestedQuery)
        dest.writeString(suggestion)
    }

    constructor(parcel: Parcel) : this(
        suggestionText = parcel.readString() ?: "",
        suggestedQuery = parcel.readString() ?: "",
        suggestion = parcel.readString() ?: ""
    )

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SuggestionDataView> = object : Parcelable.Creator<SuggestionDataView> {
            override fun createFromParcel(source: Parcel): SuggestionDataView {
                return SuggestionDataView(source)
            }

            override fun newArray(size: Int): Array<SuggestionDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}