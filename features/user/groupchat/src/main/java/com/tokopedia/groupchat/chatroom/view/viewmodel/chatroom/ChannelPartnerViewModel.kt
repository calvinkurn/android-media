package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * @author by milhamj on 26/03/18.
 */

data class ChannelPartnerViewModel (
        var partnerTitle: String = "",
        var child: List<ChannelPartnerChildViewModel> = arrayListOf()
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.partnerTitle)
        dest.writeList(this.child)
    }

    protected constructor(`in`: Parcel):this() {
        this.partnerTitle = `in`.readString()
        this.child = ArrayList()
        `in`.readList(this.child, ChannelPartnerChildViewModel::class.java.classLoader)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ChannelPartnerViewModel> = object : Parcelable.Creator<ChannelPartnerViewModel> {
            override fun createFromParcel(source: Parcel): ChannelPartnerViewModel {
                return ChannelPartnerViewModel(source)
            }

            override fun newArray(size: Int): Array<ChannelPartnerViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
