package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class SuggestionDataView(
        val suggestionText: String = "",
        val suggestedQuery: String = "",
        val suggestion: String = "",
) : Parcelable, Visitable<ProductListTypeFactory?> {

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