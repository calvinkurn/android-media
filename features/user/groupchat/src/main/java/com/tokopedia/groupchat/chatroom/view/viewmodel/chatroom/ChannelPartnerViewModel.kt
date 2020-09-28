package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by milhamj on 26/03/18.
 */
@Parcelize
data class ChannelPartnerViewModel (
        var partnerTitle: String = "",
        var child: List<ChannelPartnerChildViewModel> = arrayListOf()
) : Parcelable
