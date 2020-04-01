package com.tokopedia.notifcenter.data.entity

import androidx.fragment.app.Fragment

data class NotificationTabItem(
        val title: String = "",
        val fragment: Fragment?= null
)