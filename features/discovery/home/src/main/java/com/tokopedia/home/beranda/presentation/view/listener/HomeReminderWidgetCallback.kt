package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.home_component.listener.ReminderWidgetListener
import com.tokopedia.home_component.listener.SalamWidgetListener
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.visitable.ReminderWidgetModel

class HomeReminderWidgetCallback(
        val rechargeListener: RechargeRecommendationListener,
        val salamListener: SalamWidgetListener
) : ReminderWidgetListener {
    override fun onReminderWidgetClickListener(reminderData: ReminderWidgetModel) {
        val reminder = reminderData.data.reminders.firstOrNull()
        reminder?.let {
            when (reminderData.source) {
                ReminderEnum.RECHARGE -> {
                    rechargeListener.onRechargeRecommendationClickListener(it)
                    rechargeListener.onRechargeRecommendationDeclineClickListener(it)
                }
                ReminderEnum.SALAM -> {
                    salamListener.onSalamWidgetClickListener(it)
                }
            }
        }
    }

    override fun onReminderWidgetDeclineClickListener(reminderData: ReminderWidgetModel, toggleTracking: Boolean) {
        val reminder = reminderData.data.reminders.firstOrNull()
        reminder?.let {
            when (reminderData.source) {
                ReminderEnum.RECHARGE -> {
                    rechargeListener.onRechargeRecommendationDeclineClickListener(it, toggleTracking)
                }
                ReminderEnum.SALAM -> {
                    salamListener.onSalamWidgetDeclineClickListener(it, toggleTracking)
                }
            }
        }
    }

    override fun onReminderWidgetImpressionListener(reminderData: ReminderWidgetModel) {
        val reminder = reminderData.data.reminders.firstOrNull()
        reminder?.let {
            when (reminderData.source) {
                ReminderEnum.RECHARGE -> {
                    rechargeListener.onRechargeRecommendationImpressionListener(it)
                }
                ReminderEnum.SALAM -> {
                    salamListener.onSalamWidgetImpressionListener(it)
                }
            }
        }
    }

    override fun getReminderWidgetData(reminderData: ReminderWidgetModel) {
        //do nothing
    }
}