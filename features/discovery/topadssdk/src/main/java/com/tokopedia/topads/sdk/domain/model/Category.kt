package com.tokopedia.topads.sdk.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

private const val KEY_ID = "id"

data class Category(
    @SerializedName(KEY_ID)
    var id: Int = 0
) : Parcelable {

    constructor(jSONObject: JSONObject) : this() {
        if (!jSONObject.isNull(KEY_ID)) {
            id = jSONObject.getInt(KEY_ID)
        }
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(parcel: Parcel): Category {
                return Category(parcel)
            }

            override fun newArray(size: Int): Array<Category?> {
                return arrayOfNulls(size)
            }
        }
    }
}
