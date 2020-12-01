package com.tokopedia.sellerappwidget.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@Parcelize
data class ChatUiModel(
        val userDisplayName: String = "",
        val message: String = "",
        val time: String = ""
) : Parcelable