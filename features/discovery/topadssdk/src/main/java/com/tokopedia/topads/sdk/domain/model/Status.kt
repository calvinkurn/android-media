package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_ERROR_CODE = "error_code"
private const val KEY_MESSAGE = "message"

data class Status(
    @SerializedName(KEY_ERROR_CODE)
    @Expose
    var errorCode: Int = 0,

    @SerializedName(KEY_MESSAGE)
    var message: String = ""
) : Parcelable {

    constructor(jsonObject: JSONObject) : this() {
        if (!jsonObject.isNull(KEY_ERROR_CODE)) {
            errorCode = jsonObject.getInt(KEY_ERROR_CODE)
        }
        if (!jsonObject.isNull(KEY_MESSAGE)) {
            message = jsonObject.getString(KEY_MESSAGE)
        }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readInt()
        parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(errorCode)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Status> {
        override fun createFromParcel(parcel: Parcel): Status {
            return Status(parcel)
        }

        override fun newArray(size: Int): Array<Status?> {
            return arrayOfNulls(size)
        }
    }

}
