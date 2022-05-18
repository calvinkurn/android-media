package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ReminderData

interface SalamWidgetListener {
    fun onSalamWidgetClickListener(reminderData: ReminderData)
    fun onSalamWidgetDeclineClickListener(reminderData: ReminderData, toggleTracking: Boolean = false)
    fun onSalamWidgetImpressionListener(reminderData: ReminderData)
}