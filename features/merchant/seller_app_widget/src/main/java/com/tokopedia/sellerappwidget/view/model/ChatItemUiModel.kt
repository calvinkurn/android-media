package com.tokopedia.sellerappwidget.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@Parcelize
data class ChatUiModel(
        val chats: List<ChatItemUiModel> = emptyList(),
        val unreads: Int = 0
): Parcelable

@Parcelize
data class ChatItemUiModel(
        val messageId: Int = 0,
        val messageKey: String = "",
        val userDisplayName: String = "",
        val lastMessage: String = "",
        val lastReplyTime: String = ""
) : Parcelable