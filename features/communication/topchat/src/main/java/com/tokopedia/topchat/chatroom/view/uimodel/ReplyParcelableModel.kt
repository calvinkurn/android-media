package com.tokopedia.topchat.chatroom.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReplyParcelableModel(
    var messageId: String,
    var msg: String,
    var replyTime: String
) : Parcelable