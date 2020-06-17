package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ReminderData

interface SalamWidgetListener {
    fun onSalamWidgetClickListener(reminderData: ReminderData)
    fun onSalamWidgetDeclineClickListener(reminderData: ReminderData)
    fun onSalamWidgetDeclineTrackingListener(reminderData: ReminderData)
    fun onSalamWidgetImpressionListener(reminderData: ReminderData)
    fun getSalamWidget(position:Int)
}