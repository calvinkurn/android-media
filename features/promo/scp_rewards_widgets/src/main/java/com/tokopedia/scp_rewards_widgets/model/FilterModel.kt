package com.tokopedia.scp_rewards_widgets.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterModel(
    val id: Long? = null,
    val text: String? = null,
    val iconImageURL: String? = null
) : Parcelable {
    var isSelected: Boolean = false
}
