package com.tokopedia.settingnotif.usersetting.view.dataview

import android.os.Parcelable
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class NotificationStateDataView(
        var name: String = "",
        var icon: String = "",
        var key: String = "",
        var status: Boolean = false
): Parcelable