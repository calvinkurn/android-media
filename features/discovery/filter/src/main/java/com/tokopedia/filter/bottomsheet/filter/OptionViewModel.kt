package com.tokopedia.filter.bottomsheet.filter

import android.os.Parcelable
import com.tokopedia.filter.common.data.Option
import kotlinx.android.parcel.Parcelize

@Parcelize
class OptionViewModel(
    val option: Option
): Parcelable {
    var isSelected = false
}