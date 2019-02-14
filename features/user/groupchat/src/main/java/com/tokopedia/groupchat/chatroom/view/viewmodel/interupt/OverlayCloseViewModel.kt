package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory
import com.tokopedia.groupchat.room.domain.pojo.BaseGroupChatPojo

/**
 * @author by yfsx on 14/12/18.
 */
class OverlayCloseViewModel : BaseGroupChatPojo, Visitable<GroupChatTypeFactory>, Parcelable {

    @SerializedName("closeable")
    @Expose
    var isCloseable: Boolean = false
    @SerializedName("status")
    @Expose
    var status: Int = 0

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    constructor() {}

    constructor(closeable: Boolean, status: Int) {
        this.isCloseable = closeable
        this.status = status
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (this.isCloseable) 1.toByte() else 0.toByte())
        dest.writeInt(this.status)
    }

    protected constructor(`in`: Parcel) {
        this.isCloseable = `in`.readByte().toInt() != 0
        this.status = `in`.readInt()
    }

    companion object {


        const val TYPE = "overlay_close"

        @JvmField
        val CREATOR: Parcelable.Creator<OverlayCloseViewModel> = object : Parcelable.Creator<OverlayCloseViewModel> {
            override fun createFromParcel(source: Parcel): OverlayCloseViewModel {
                return OverlayCloseViewModel(source)
            }

            override fun newArray(size: Int): Array<OverlayCloseViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
