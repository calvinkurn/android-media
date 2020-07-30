package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Search() : Parcelable {

    @SerializedName("searchable")
    @Expose
    var searchable: Int = 0
    @SerializedName("placeholder")
    @Expose
    var placeholder: String = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.searchable)
        dest.writeString(this.placeholder)
    }

    protected constructor(`in`: Parcel) : this() {
        this.searchable = `in`.readInt()
        this.placeholder = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Search> = object : Parcelable.Creator<Search> {

            override fun createFromParcel(source: Parcel): Search {
                return Search(source)
            }

            override fun newArray(size: Int): Array<Search?> {
                return arrayOfNulls(size)
            }
        }
    }
}
