package com.tokopedia.groupchat.room.domain.pojo.channelinfo

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 23/10/18
 */
class SettingGroupChat : Parcelable {

    @SerializedName("ping_interval")
    @Expose
    var pingInterval: Int = 0
        private set
    @SerializedName("max_chars")
    @Expose
    var maxChar: Int = 0
        private set
    @SerializedName("max_retries")
    @Expose
    var maxRetries: Int = 0
        private set
    @SerializedName("min_reconnect_delay")
    @Expose
    var delay: Int = 0
        private set

    constructor() {
        pingInterval = DEFAULT_PING
        maxRetries = DEFAULT_MAX_RETRIES
        delay = DEFAULT_DELAY
        maxChar = DEFAULT_MAX_CHAR
    }

    constructor(`in`: Parcel) {
        pingInterval = `in`.readInt()
        maxChar = `in`.readInt()
        maxRetries = `in`.readInt()
        delay = `in`.readInt()

        if (pingInterval == 0) {
            pingInterval = DEFAULT_PING
        }
        if (maxRetries == 0) {
            maxRetries = DEFAULT_MAX_RETRIES
        }
        if (delay == 0) {
            delay = DEFAULT_DELAY
        }
        if (maxChar == 0) {
            maxChar = DEFAULT_MAX_CHAR
        }


    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(pingInterval)
        dest.writeInt(maxChar)
        dest.writeInt(maxRetries)
        dest.writeInt(delay)
    }

    companion object {

        val DEFAULT_PING = 10000
        val DEFAULT_MAX_RETRIES = 3
        val DEFAULT_MAX_CHAR = 200
        val DEFAULT_DELAY = 5000

        val CREATOR: Parcelable.Creator<SettingGroupChat> = object : Parcelable.Creator<SettingGroupChat> {
            override fun createFromParcel(`in`: Parcel): SettingGroupChat {
                return SettingGroupChat(`in`)
            }

            override fun newArray(size: Int): Array<SettingGroupChat?> {
                return arrayOfNulls(size)
            }
        }
    }
}
