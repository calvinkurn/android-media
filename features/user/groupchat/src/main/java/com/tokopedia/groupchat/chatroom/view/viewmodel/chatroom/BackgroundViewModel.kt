package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author by StevenFredian .
 */

class BackgroundViewModel() : Visitable<Any>, Parcelable {


    @SerializedName("background_url")
    @Expose
    var url: String = ""

    @SerializedName("background_default")
    @Expose
    var default: Int = 0

    constructor(`in`: Parcel) : this() {
        this.url = `in`.readString()
        this.default = `in`.readInt()
    }

    companion object {

        const val TYPE = "background"

        @JvmField
        val CREATOR: Parcelable.Creator<BackgroundViewModel> = object : Parcelable.Creator<BackgroundViewModel> {
            override fun createFromParcel(parcel: Parcel): BackgroundViewModel {
                return BackgroundViewModel(parcel)
            }

            override fun newArray(size: Int): Array<BackgroundViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun type(typeFactory: Any): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeInt(default)
    }

    override fun describeContents(): Int {
        return 0
    }

}

