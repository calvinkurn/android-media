package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ReminderData

interface RechargeRecommendationListener {
    fun onRechargeRecommendationClickListener(reminderData: ReminderData)
    fun onRechargeRecommendationDeclineClickListener(reminderData: ReminderData, toggleTracking: Boolean = false)
    fun onRechargeRecommendationImpressionListener(reminderData: ReminderData)

}