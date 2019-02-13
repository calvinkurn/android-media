package com.tokopedia.groupchat.chatroom.kotlin.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.kotlin.domain.pojo.BaseGroupChatPojo
import com.tokopedia.groupchat.chatroom.kotlin.view.adapter.chatroom.typefactory.GroupChatTypeFactory

/**
 * @author by StevenFredian on 15/05/18.
 */

class AdsViewModel : BaseGroupChatPojo, Visitable<GroupChatTypeFactory>, Parcelable {

    @SerializedName("ads_url")
    @Expose
    var adsUrl: String? = null
    @SerializedName("ads_link")
    @Expose
    var adsLink: String? = null
    @SerializedName("ads_id")
    @Expose
    var adsId: String? = null

    constructor(adsUrl: String, adsLink: String, adsId: String) {
        this.adsUrl = adsUrl
        this.adsLink = adsLink
        this.adsId = adsId
    }

    protected constructor(`in`: Parcel) {
        adsUrl = `in`.readString()
        adsLink = `in`.readString()
        adsId = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(adsUrl)
        dest.writeString(adsLink)
        dest.writeString(adsId)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return 0
    }

    companion object {
        val TYPE = "ads"

        @JvmField
        val CREATOR: Parcelable.Creator<AdsViewModel> = object : Parcelable.Creator<AdsViewModel> {
            override fun createFromParcel(`in`: Parcel): AdsViewModel {
                return AdsViewModel(`in`)
            }

            override fun newArray(size: Int): Array<AdsViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
