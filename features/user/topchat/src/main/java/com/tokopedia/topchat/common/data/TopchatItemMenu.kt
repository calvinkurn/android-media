package com.tokopedia.topchat.common.data

import androidx.annotation.DrawableRes
import com.tokopedia.topchat.R

class TopchatItemMenu {
    var title: String
    var icon = 0

    @DrawableRes
    var iconEnd = 0

    constructor(title: String) {
        this.title = title
    }

    constructor(title: String, icon: Int) {
        this.title = title
        this.icon = icon
    }

    constructor(title: String, hasCheck: Boolean) {
        this.title = title
        if (hasCheck) {
            iconEnd = R.drawable.ic_topchat_check_green
        }
    }
}