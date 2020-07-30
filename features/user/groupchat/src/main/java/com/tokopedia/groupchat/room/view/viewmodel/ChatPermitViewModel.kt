package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable


/**
 * Created by mzennis on 2020-02-17.
 */
data class ChatPermitViewModel(
        @SerializedName("is_show_chat")
        @Expose
        val isChatDisabled: Boolean = false,

        @SerializedName("error_chat_message")
        @Expose
        val errorMessage: String = ""
) : Visitable<Any>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString()?:"")

    override fun type(typeFactory: Any?): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isChatDisabled) 1 else 0)
        parcel.writeString(errorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatPermitViewModel> {
        override fun createFromParcel(parcel: Parcel): ChatPermitViewModel {
            return ChatPermitViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatPermitViewModel?> {
            return arrayOfNulls(size)
        }

        const val TYPE = "chat_permit"
    }
}