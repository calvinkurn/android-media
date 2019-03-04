package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by milhamj on 26/03/18.
 */

class ChannelPartnerChildViewModel : Parcelable {
    var partnerId: String? = null
        private set
    var partnerAvatar: String? = null
        private set
    var partnerName: String? = null
        private set
    var partnerUrl: String? = null
        private set

    constructor(partnerId: String, partnerAvatar: String, partnerName: String,
                partnerUrl: String) {
        this.partnerId = partnerId
        this.partnerAvatar = partnerAvatar
        this.partnerName = partnerName
        this.partnerUrl = partnerUrl
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.partnerId)
        dest.writeString(this.partnerAvatar)
        dest.writeString(this.partnerName)
        dest.writeString(this.partnerUrl)
    }

    protected constructor(`in`: Parcel) {
        this.partnerId = `in`.readString()
        this.partnerAvatar = `in`.readString()
        this.partnerName = `in`.readString()
        this.partnerUrl = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<ChannelPartnerChildViewModel> = object : Parcelable.Creator<ChannelPartnerChildViewModel> {
            override fun createFromParcel(source: Parcel): ChannelPartnerChildViewModel {
                return ChannelPartnerChildViewModel(source)
            }

            override fun newArray(size: Int): Array<ChannelPartnerChildViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
