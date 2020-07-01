package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.ReminderWidgetModel

class RechargeHomepageReminderWidgetCallback(val listener: OnItemBindListener): ReminderWidgetListener {
    override fun onReminderWidgetClickListener(reminderData: ReminderWidgetModel) {

    }

    override fun onReminderWidgetDeclineClickListener(reminderData: ReminderWidgetModel, toggleTracking: Boolean) {

    }

    override fun onReminderWidgetImpressionListener(reminderData: ReminderWidgetModel) {

    }

    override fun getReminderWidget(reminderEnum: ReminderEnum) {

    }
}