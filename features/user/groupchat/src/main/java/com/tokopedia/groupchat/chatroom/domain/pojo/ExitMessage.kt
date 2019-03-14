package com.tokopedia.groupchat.chatroom.domain.pojo

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian on 03/05/18.
 */

class ExitMessage : Parcelable {

    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("body")
    @Expose
    var body: String = ""


    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.title)
        dest.writeString(this.body)
    }

    protected constructor(`in`: Parcel) {
        this.title = `in`.readString()
        this.body = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<ExitMessage> = object : Parcelable.Creator<ExitMessage> {
            override fun createFromParcel(source: Parcel): ExitMessage {
                return ExitMessage(source)
            }

            override fun newArray(size: Int): Array<ExitMessage?> {
                return arrayOfNulls(size)
            }
        }
    }
}
