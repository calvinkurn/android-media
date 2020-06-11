package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderWidget

interface ReminderWidgetListener {
    fun onContentClickListener(applink: String, source:String, reminderWidget: ReminderWidget)
    fun onDeclineClickListener(requestParams: Map<String, String>)
    fun onImpressionListener(reminderData: ReminderData)
}