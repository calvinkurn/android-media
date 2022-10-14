package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_CODE = "code"
private const val KEY_TITLE = "title"
private const val KEY_DETAIL = "detail"

data class Error(
    @SerializedName(KEY_CODE)
    @Expose
    var code: Int = 0,

    @SerializedName(KEY_TITLE)
    @Expose
    var title: String = "",

    @SerializedName(KEY_DETAIL)
    @Expose
    var detail: String = ""
) : Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_CODE)) {
            code = jSONObject.getInt(KEY_CODE)
        }
        if (!jSONObject.isNull(KEY_TITLE)) {
            title = jSONObject.getString(KEY_TITLE)
        }
        if (!jSONObject.isNull(KEY_DETAIL)) {
            detail = jSONObject.getString(KEY_DETAIL)
        }
    }

    constructor(parcel: Parcel) : this() {
        code = parcel.readInt()
        title = parcel.readString() ?: ""
        detail = parcel.readString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(code)
        parcel.writeString(title)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Error> {
        override fun createFromParcel(parcel: Parcel): Error {
            return Error(parcel)
        }

        override fun newArray(size: Int): Array<Error?> {
            return arrayOfNulls(size)
        }
    }
}
