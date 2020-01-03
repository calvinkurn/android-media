package com.tokopedia.notifcenter.listener

import com.tokopedia.coachmark.CoachMarkItem

interface NotificationActivityListener {
    fun showOnBoarding(coachMarkItems: ArrayList<CoachMarkItem>, tag: String)
}