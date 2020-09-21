package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 15/07/20.
 */

@Parcelize
data class MenuGridIconNotificationItemViewModel(
        var resourceId: Int = 0,
        var description: String = "",
        var applink: String = "",
        var count: Int = 0,
        var titleTrack : String = "",
        var sectionTrack: String = "") : Parcelable