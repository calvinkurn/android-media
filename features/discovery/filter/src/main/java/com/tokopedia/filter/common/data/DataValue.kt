package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class DataValue() : Parcelable {

    lateinit var selected: String
    lateinit var selectedOb: String

    @SerializedName("filter")
    @Expose
    var filter: List<Filter> = ArrayList()

    @SerializedName("sort")
    @Expose
    var sort: List<Sort> = ArrayList()


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.selected)
        dest.writeString(this.selectedOb)
        dest.writeTypedList(this.filter)
        dest.writeTypedList(this.sort)
    }

    protected constructor(`in`: Parcel) : this() {
        this.selected = `in`.readString()
        this.selectedOb = `in`.readString()
        this.filter = `in`.createTypedArrayList(Filter.CREATOR)
        this.sort = `in`.createTypedArrayList(Sort.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DataValue> = object : Parcelable.Creator<DataValue> {
            override fun createFromParcel(source: Parcel): DataValue {
                return DataValue(source)
            }

            override fun newArray(size: Int): Array<DataValue?> {
                return arrayOfNulls(size)
            }
        }
    }
}
