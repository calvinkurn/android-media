package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo

/**
 * @author by yfsx on 14/12/18.
 */
 data class OverlayViewModel (
        @SerializedName("type")
        @Expose
        var type: String = "",
        //For Webview
        @SerializedName("link_url")
        @Expose
        var redirectUrl: String = "",
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

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readParcelable(InteruptViewModel::class.java.classLoader)) {
    }

    override fun type(typeFactory: GroupChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {


        const val TYPE = "overlay_message"
        const val TYPE_CTA = "cta"
        const val TYPE_WEBVIEW = "webview"

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(redirectUrl)
        parcel.writeByte(if (isCloseable) 1 else 0)
        parcel.writeInt(status)
        parcel.writeParcelable(interuptViewModel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

}
