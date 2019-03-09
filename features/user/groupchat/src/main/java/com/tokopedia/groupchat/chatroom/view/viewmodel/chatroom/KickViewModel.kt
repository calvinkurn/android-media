package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

/**
 * @author : Steven 26/02/19
 */
data class KickViewModel (
        var kickMessage: String = "",
        var kickTitle: String = "",
        var kickButtonTitle: String = "",
        var kickButtonUrl: String = "",
        var kickDuration: Long = 0L
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.kickMessage)
        dest.writeString(this.kickTitle)
        dest.writeString(this.kickButtonTitle)
        dest.writeString(this.kickButtonUrl)
        dest.writeLong(this.kickDuration)
    }

    protected constructor(`in`: Parcel):this() {
        this.kickMessage = `in`.readString()
        this.kickTitle = `in`.readString()
        this.kickButtonTitle = `in`.readString()
        this.kickButtonUrl = `in`.readString()
        this.kickDuration = `in`.readLong()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<KickViewModel> = object : Parcelable.Creator<KickViewModel> {
            override fun createFromParcel(source: Parcel): KickViewModel {
                return KickViewModel(source)
            }

            override fun newArray(size: Int): Array<KickViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
