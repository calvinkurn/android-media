package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

/**
 * @author : Steven 26/02/19
 */
data class BanViewModel (
        var bannedMessage: String = "",
        var bannedTitle: String = "",
        var bannedButtonTitle: String = "",
        var bannedButtonUrl: String = ""
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.bannedMessage)
        dest.writeString(this.bannedTitle)
        dest.writeString(this.bannedButtonTitle)
        dest.writeString(this.bannedButtonUrl)
    }

    protected constructor(`in`: Parcel):this() {
        this.bannedMessage = `in`.readString()
        this.bannedTitle = `in`.readString()
        this.bannedButtonTitle = `in`.readString()
        this.bannedButtonUrl = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<BanViewModel> = object : Parcelable.Creator<BanViewModel> {
            override fun createFromParcel(source: Parcel): BanViewModel {
                return BanViewModel(source)
            }

            override fun newArray(size: Int): Array<BanViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
