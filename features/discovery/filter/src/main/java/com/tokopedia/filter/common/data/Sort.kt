package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Sort() : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String = ""

    @SerializedName("key")
    @Expose
    var key: String = ""

    @SerializedName("value")
    @Expose
    var value: String = ""

    @SerializedName(value = "input_type", alternate = [ "inputType" ])
    @Expose
    private var inputType: String = ""

    @SerializedName("applyFilter")
    @Expose
    var applyFilter: String = ""

    override fun toString(): String {
        return name
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.name)
        dest.writeString(this.key)
        dest.writeString(this.value)
        dest.writeString(this.inputType)
        dest.writeString(this.applyFilter)
    }

    protected constructor(`in`: Parcel) : this() {
        this.name = `in`.readString()
        this.key = `in`.readString()
        this.value = `in`.readString()
        this.inputType = `in`.readString()
        this.applyFilter = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Sort> = object : Parcelable.Creator<Sort> {
            override fun createFromParcel(source: Parcel): Sort {
                return Sort(source)
            }

            override fun newArray(size: Int): Array<Sort?> {
                return arrayOfNulls(size)
            }
        }
    }
}
