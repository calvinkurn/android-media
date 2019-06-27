package com.tokopedia.iris.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.DEFAULT_SERVICE_TIME
import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 10/18/18.
 */
data class Configuration(
        @SerializedName("row_limit") var maxRow: Int = DEFAULT_MAX_ROW,
        @SerializedName("interval") var intervals: Long = TimeUnit.MINUTES.toMillis(DEFAULT_SERVICE_TIME), // default 15 minutes
        var isEnabled: Boolean = true
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(maxRow)
        parcel.writeLong(intervals)
        parcel.writeByte(if (isEnabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Configuration> {
        override fun createFromParcel(parcel: Parcel): Configuration {
            return Configuration(parcel)
        }

        override fun newArray(size: Int): Array<Configuration?> {
            return arrayOfNulls(size)
        }
    }
}