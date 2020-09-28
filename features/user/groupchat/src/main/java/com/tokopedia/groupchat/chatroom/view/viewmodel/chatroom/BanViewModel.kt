package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 26/02/19
 */
@Parcelize
data class BanViewModel (
        var bannedMessage: String = "",
        var bannedTitle: String = "",
        var bannedButtonTitle: String = "",
        var bannedButtonUrl: String = ""
) : Parcelable