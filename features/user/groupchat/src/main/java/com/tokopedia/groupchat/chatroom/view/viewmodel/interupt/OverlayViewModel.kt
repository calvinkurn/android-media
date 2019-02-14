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
 data class OverlayViewModel (
        @SerializedName("closeable")
        @Expose
        var isCloseable: Boolean = true,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("assets")
        @Expose
        var interuptViewModel: InteruptViewModel? = null
) : BaseGroupChatPojo(), Visitable<GroupChatTypeFactory>, Parcelable {

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (this.isCloseable) 1.toByte() else 0.toByte())
        dest.writeInt(this.status)
        dest.writeParcelable(this.interuptViewModel, flags)
    }

    protected constructor(`in`: Parcel) : this() {
        this.isCloseable = `in`.readByte().toInt() != 0
        this.status = `in`.readInt()
        this.interuptViewModel = `in`.readParcelable(InteruptViewModel::class.java.classLoader)
    }

    companion object {


        const val TYPE = "overlay_message"

        @JvmField
        val CREATOR: Parcelable.Creator<OverlayViewModel> = object : Parcelable.Creator<OverlayViewModel> {
            override fun createFromParcel(source: Parcel): OverlayViewModel {
                return OverlayViewModel(source)
            }

            override fun newArray(size: Int): Array<OverlayViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
