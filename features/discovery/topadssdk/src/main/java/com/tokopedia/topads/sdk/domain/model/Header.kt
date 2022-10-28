package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_META_DATA = "meta"
private const val KEY_TOTAL_DATA = "total_data"
private const val KEY_PROCESS_TIME = "process_time"

data class Header(
    @SerializedName(KEY_TOTAL_DATA)
    @Expose
    var totalData: Int = 0,

    @SerializedName(KEY_PROCESS_TIME)
    @Expose
    var processTime: Double = 0.0,

    @SerializedName(KEY_META_DATA)
    @Expose
    var metaData: MetaData = MetaData()
) : Parcelable {


    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_META_DATA)) {
            metaData = MetaData(jSONObject.getJSONObject(KEY_META_DATA))
        }
        if (!jSONObject.isNull(KEY_PROCESS_TIME)) {
            processTime = jSONObject.getDouble(KEY_PROCESS_TIME)
        }
        if (!jSONObject.isNull(KEY_TOTAL_DATA)) {
            totalData = jSONObject.getInt(KEY_TOTAL_DATA)
        }
    }

    constructor(parcel: Parcel) : this() {
        totalData = parcel.readInt()
        processTime = parcel.readDouble()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(totalData)
        dest.writeDouble(processTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Header> {
        override fun createFromParcel(parcel: Parcel): Header {
            return Header(parcel)
        }

        override fun newArray(size: Int): Array<Header?> {
            return arrayOfNulls(size)
        }
    }
}
