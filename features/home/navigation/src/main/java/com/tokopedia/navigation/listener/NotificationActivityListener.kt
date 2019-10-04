package com.tokopedia.navigation.listener

import com.tokopedia.coachmark.CoachMarkItem

interface NotificationActivityListener {
    fun showOnBoarding(coachMarkItems: ArrayList<CoachMarkItem>, tag: String)
}