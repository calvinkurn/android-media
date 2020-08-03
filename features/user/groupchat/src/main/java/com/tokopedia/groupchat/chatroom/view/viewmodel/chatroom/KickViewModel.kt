package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author : Steven 26/02/19
 */
@Parcelize
data class KickViewModel (
        var kickMessage: String = "",
        var kickTitle: String = "",
        var kickButtonTitle: String = "",
        var kickButtonUrl: String = "",
        var kickDuration: Long = 0L
) : Parcelable