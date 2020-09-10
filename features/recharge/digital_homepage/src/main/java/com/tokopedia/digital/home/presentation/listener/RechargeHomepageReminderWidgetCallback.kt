package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.visitable.ReminderWidgetModel

class RechargeHomepageReminderWidgetCallback(val listener: RechargeHomepageItemListener): ReminderWidgetListener {
    override fun onReminderWidgetClickListener(reminderData: ReminderWidgetModel) {
        listener.onRechargeReminderWidgetClicked(reminderData.visitableId()?.toIntOrNull() ?: -1)
    }

    override fun onReminderWidgetDeclineClickListener(reminderData: ReminderWidgetModel, toggleTracking: Boolean) {
        listener.onRechargeReminderWidgetClosed(reminderData.visitableId()?.toIntOrNull() ?: -1)
    }

    override fun onReminderWidgetImpressionListener(reminderData: ReminderWidgetModel) {
        listener.onRechargeReminderWidgetImpression(reminderData.visitableId()?.toIntOrNull() ?: -1)
    }

    override fun getReminderWidgetData(reminderData: ReminderWidgetModel) {
        listener.loadRechargeSectionData(reminderData.visitableId()?.toIntOrNull() ?: -1)
    }
}