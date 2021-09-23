package com.tokopedia.topchat.common.data

import androidx.annotation.DrawableRes
import com.tokopedia.topchat.R

class TopchatItemMenu constructor(
    _title: String,
    _icon: Int = 0,
    _hasCheck: Boolean = false
) {

    var title: String = _title
    var icon = _icon

    @DrawableRes
    var iconEnd = 0

    init {
        if (_hasCheck) {
            iconEnd = R.drawable.ic_topchat_check_green
        }
    }
}