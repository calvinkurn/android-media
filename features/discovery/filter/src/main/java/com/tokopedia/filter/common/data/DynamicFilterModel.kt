package com.tokopedia.filter.common.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DynamicFilterModel() : Parcelable {

    @SerializedName("process_time")
    @Expose
    lateinit var processTime: String

    @SerializedName("data")
    @Expose
    var data = DataValue()

    @SerializedName("status")
    @Expose
    lateinit var status: String

    @SerializedName("isOfficialSelectedFlag")
    @Expose
    var isOfficialSelectedFlag = false

    constructor(data: DataValue, processTime: String, status: String, isOfficialFlag: Boolean) : this() {
        this.data = data
        this.processTime = processTime
        this.status = status
        this.isOfficialSelectedFlag = isOfficialFlag
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.processTime)
        dest.writeParcelable(this.data, flags)
        dest.writeString(this.status)
    }

    protected constructor(`in`: Parcel) : this() {
        this.processTime = `in`.readString()
        this.data = `in`.readParcelable(DataValue::class.java.getClassLoader())
        this.status = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<DynamicFilterModel> = object : Parcelable.Creator<DynamicFilterModel> {
            override fun createFromParcel(source: Parcel): DynamicFilterModel {
                return DynamicFilterModel(source)
            }

            override fun newArray(size: Int): Array<DynamicFilterModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
