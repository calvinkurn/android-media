package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeRecommendationTracking
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_CONTENT_ID
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase.Companion.PARAM_UUID
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.home_component.listener.SalamWidgetListener
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderWidget

class ReminderWidgetComponentCallback (val context: Context?,val viewModel: HomeViewModel,
                                       val homeCategoryListener: HomeCategoryListener):
        RechargeRecommendationListener, SalamWidgetListener {

    override fun onRechargeRecommendationClickListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            RechargeRecommendationTracking.homeRechargeRecommendationOnClickTracker(
                    it, mapRemindertoRechargeRecommendationData(reminderData)
            )
        }
        context?.let {
            RouteManager.route(it, reminderData.appLink)
        }
    }

    override fun onRechargeRecommendationDeclineClickListener(reminderData: ReminderData) {
        // Trigger decline listener to remove widget from homepage
        RechargeRecommendationTracking.homeRechargeRecommendationOnCloseTracker(
                mapRemindertoRechargeRecommendationData(reminderData))

        val requestParams = mapOf(
                PARAM_UUID to reminderData.UUID,
                PARAM_CONTENT_ID to reminderData.id.toString())
        viewModel.declineRechargeRecommendationItem(requestParams)
    }

    override fun onRechargeRecommendationImpressionListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            RechargeRecommendationTracking.homeRechargeRecommendationImpressionTracker(
                    it, mapRemindertoRechargeRecommendationData(reminderData)
            )
        }
    }

    override fun onSalamWidgetClickListener(reminderData: ReminderData) {
        context?.let {
            RouteManager.route(it, reminderData.appLink)
        }
    }

    override fun onSalamWidgetDeclineClickListener(reminderData: ReminderData) {

    }

    private fun mapRemindertoRechargeRecommendationData(reminderData: ReminderData): RechargeRecommendationData{
        reminderData.let {
            return RechargeRecommendationData(
                    contentID = it.id.toString(),
                    mainText = it.mainText,
                    subText = it.subText,
                    applink = it.appLink,
                    link = it.link,
                    iconURL = it.iconURL,
                    title = it.title,
                    backgroundColor = it.backgroundColor,
                    buttonText = it.buttonText
            )
        }
    }
}