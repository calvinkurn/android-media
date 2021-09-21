package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.SalamWidgetTracking
import com.tokopedia.home.beranda.domain.interactor.DeclineSalamWIdgetUseCase
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidgetData
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.SalamWidgetListener
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.user.session.UserSessionInterface

class SalamWidgetCallback (val context: Context?,
                           val homeCategoryListener: HomeCategoryListener,
                           val userSessionInterface: UserSessionInterface):SalamWidgetListener{

    override fun onSalamWidgetClickListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            SalamWidgetTracking.homeSalamWidgetOnClickTracker(
                    it, mapRemindertoSalamWidgetData(reminderData)
            )
        }

        context?.let {
            RouteManager.route(it, reminderData.appLink)
        }
    }

    override fun onSalamWidgetDeclineClickListener(reminderData: ReminderData, toggleTracking:Boolean) {

        if (toggleTracking) {
            SalamWidgetTracking.homeSalamWidgetOnCloseTracker(mapRemindertoSalamWidgetData(reminderData))

            val requestParams = mapOf(
                    DeclineSalamWIdgetUseCase.PARAM_WIDGET_ID to reminderData.id.toInt()
            )
            homeCategoryListener.declineSalamItem(requestParams)
        }
    }

    override fun onSalamWidgetImpressionListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            SalamWidgetTracking.homeSalamWidgetImpressionTracker(
                    it, mapRemindertoSalamWidgetData(reminderData), userSessionInterface.userId
            )
        }
    }

    override fun getSalamWidget() {
        homeCategoryListener.getSalamWidget()
    }

    private fun mapRemindertoSalamWidgetData(reminderData: ReminderData): SalamWidgetData {
        reminderData.let {
            return SalamWidgetData(
                    id = it.id.toInt(),
                    mainText = it.mainText,
                    subText = it.subText,
                    appLink = it.appLink,
                    link = it.link,
                    iconURL = it.iconURL,
                    title = it.title,
                    backgroundColor = it.backgroundColor.firstOrNull() ?: "",
                    buttonText = it.buttonText
            )
        }
    }
}