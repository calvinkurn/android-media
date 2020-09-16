package com.tokopedia.home_component.listener

import com.tokopedia.home_component.visitable.ReminderWidgetModel

interface ReminderWidgetListener {
    fun onReminderWidgetClickListener(reminderData: ReminderWidgetModel)
    fun onReminderWidgetDeclineClickListener(reminderData: ReminderWidgetModel,toggleTracking: Boolean = false)
    fun onReminderWidgetImpressionListener(reminderData: ReminderWidgetModel)
    fun getReminderWidgetData(reminderData: ReminderWidgetModel)
}