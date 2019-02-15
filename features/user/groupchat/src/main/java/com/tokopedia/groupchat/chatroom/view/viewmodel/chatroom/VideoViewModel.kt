package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo

/**
 * @author by StevenFredian on 24/09/18.
 */

class VideoViewModel : BaseGroupChatPojo, Visitable<GroupChatTypeFactory>, Parcelable {

    @SerializedName("video_id")
    @Expose
    var videoId: String? = null

    constructor(videoId: String) {
        this.videoId = videoId
    }

    protected constructor(`in`: Parcel) {
        videoId = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(videoId)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return 0
    }

    companion object {
        const val TYPE = "video"

        @JvmField
        val CREATOR: Parcelable.Creator<VideoViewModel> = object : Parcelable.Creator<VideoViewModel> {
            override fun createFromParcel(`in`: Parcel): VideoViewModel {
                return VideoViewModel(`in`)
            }

            override fun newArray(size: Int): Array<VideoViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
