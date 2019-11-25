package com.tokopedia.filter.newdynamicfilter.analytics

import android.os.Parcel
import android.os.Parcelable

class FilterTrackingData() : Parcelable {
    var event: String? = null
    var filterCategory: String? = null
    var categoryId: String? = null
    var prefix: String? = null

    constructor(event: String, filterCategory: String, categoryId: String, prefix: String) : this() {
        this.event = event
        this.filterCategory = filterCategory
        this.categoryId = categoryId
        this.prefix = prefix
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.event)
        dest.writeString(this.filterCategory)
        dest.writeString(this.categoryId)
        dest.writeString(this.prefix)
    }

    protected constructor(`in`: Parcel) : this() {
        this.event = `in`.readString()
        this.filterCategory = `in`.readString()
        this.categoryId = `in`.readString()
        this.prefix = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<FilterTrackingData> = object : Parcelable.Creator<FilterTrackingData> {
            override fun createFromParcel(source: Parcel): FilterTrackingData {
                return FilterTrackingData(source)
            }

            override fun newArray(size: Int): Array<FilterTrackingData?> {
                return arrayOfNulls(size)
            }
        }
    }
}
