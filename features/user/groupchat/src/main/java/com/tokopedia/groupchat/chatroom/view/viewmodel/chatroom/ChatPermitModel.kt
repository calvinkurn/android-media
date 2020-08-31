package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-02-17.
 */
data class ChatPermitModel(
    @SerializedName("is_show_chat")
    @Expose
    val isChatDisabled: Boolean = false,
    @SerializedName("error_chat_message")
    @Expose
    val errorMessage: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString()?:"")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isChatDisabled) 1 else 0)
        parcel.writeString(errorMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatPermitModel> {
        override fun createFromParcel(parcel: Parcel): ChatPermitModel {
            return ChatPermitModel(parcel)
        }

        override fun newArray(size: Int): Array<ChatPermitModel?> {
            return arrayOfNulls(size)
        }
    }

}