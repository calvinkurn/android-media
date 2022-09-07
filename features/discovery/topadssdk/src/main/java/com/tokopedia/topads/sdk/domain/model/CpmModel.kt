package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.util.*


private const val KEY_HEADER = "header"
private const val KEY_STATUS = "status"
private const val KEY_DATA = "data"
private const val KEY_ERROR = "errors"

data class CpmModel(
    @SerializedName(KEY_ERROR)
    var error: Error? = Error(),

    @SerializedName(KEY_STATUS)
    var status: Status? = Status(),

    @SerializedName(KEY_HEADER)
    var header: Header? = Header(),

    @SerializedName(KEY_DATA)
    var data: MutableList<CpmData>? = ArrayList()
) : Parcelable {

    constructor(jsonObject: JSONObject) : this() {
        if (!jsonObject.isNull(KEY_ERROR)) {
            error = Error(jsonObject.getJSONArray(KEY_ERROR).getJSONObject(0))
        }
        if (!jsonObject.isNull(KEY_HEADER)) {
            header = Header(jsonObject.getJSONObject(KEY_HEADER))
        }
        if (!jsonObject.isNull(KEY_STATUS)) {
            status = Status(jsonObject.getJSONObject(KEY_STATUS))
        }
        if (!jsonObject.isNull(KEY_DATA)) {
            val dataArray = jsonObject.getJSONArray(KEY_DATA)
            for (i in 0 until dataArray.length()) {
                data?.add(CpmData(dataArray.getJSONObject(i)))
            }
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Error::class.java.classLoader),
        parcel.readParcelable(Status::class.java.classLoader),
        parcel.readParcelable(Header::class.java.classLoader),
        parcel.createTypedArrayList(CpmData.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(error, flags)
        parcel.writeParcelable(status, flags)
        parcel.writeParcelable(header, flags)
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CpmModel> {
        override fun createFromParcel(parcel: Parcel): CpmModel {
            return CpmModel(parcel)
        }

        override fun newArray(size: Int): Array<CpmModel?> {
            return arrayOfNulls(size)
        }
    }
}
