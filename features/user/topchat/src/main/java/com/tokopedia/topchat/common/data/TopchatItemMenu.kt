package com.tokopedia.topchat.common.data

import androidx.annotation.DrawableRes
import com.tokopedia.topchat.R

class TopchatItemMenu constructor(
    val title: String,
    val icon: Int = 0,
    val hasCheck: Boolean = false,
    val id: Int = -1,
    val iconUnify: Int? = null,
    val showNewLabel: Boolean = false
) {

    @DrawableRes
    var iconEnd = 0

    init {
        if (hasCheck) {
            iconEnd = R.drawable.ic_topchat_check_green
        }
    }

    companion object {
        const val ID_CHAT_SETTING = 1
    }
}