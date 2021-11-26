package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

class TickerDataView(
    val text: String = "",
    val query: String = "",
    val typeId: Int = 0,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keyword: String = "",
    val dimension90: String = "",
) : ImpressHolder(),
    Parcelable,
    Visitable<ProductListTypeFactory?>,
    SearchComponentTracking by searchComponentTracking(
        keyword = keyword,
        componentId = componentId,
        applink = query,
        dimension90 = dimension90,
    ) {

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(text)
        dest.writeString(query)
        dest.writeInt(typeId)
    }

    constructor(parcel: Parcel): this(
        text = parcel.readString() ?: "",
        query = parcel.readString() ?: "",
        typeId = parcel.readInt(),
    )

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TickerDataView> = object : Parcelable.Creator<TickerDataView> {
            override fun createFromParcel(source: Parcel): TickerDataView {
                return TickerDataView(source)
            }

            override fun newArray(size: Int): Array<TickerDataView?> {
                return arrayOfNulls(size)
            }
        }
    }
}