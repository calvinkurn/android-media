package com.tokopedia.digital.product.view.model

import android.os.Parcel
import android.os.Parcelable

class DigitalCategoryDetailPassData() : Parcelable {

    constructor(parcel: Parcel) : this()

    class Builder {

        fun appLinks(getDataApplinks: String): Builder {
            return this
        }

        fun categoryId(categoryId: String): Builder {
            return this
        }

        fun categoryName(categoryName: String): Builder {
            return this
        }

        fun url(url: String): Builder {
            return this
        }

        fun build(): DigitalCategoryDetailPassData {
            return DigitalCategoryDetailPassData()
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DigitalCategoryDetailPassData> {
        override fun createFromParcel(parcel: Parcel): DigitalCategoryDetailPassData {
            return DigitalCategoryDetailPassData(parcel)
        }

        override fun newArray(size: Int): Array<DigitalCategoryDetailPassData?> {
            return arrayOfNulls(size)
        }
    }
}