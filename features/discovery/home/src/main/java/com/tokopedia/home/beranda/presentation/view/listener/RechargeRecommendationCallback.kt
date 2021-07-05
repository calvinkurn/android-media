package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeRecommendationTracking
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.home_component.model.ReminderData

class RechargeRecommendationCallback (val context: Context?,
                                      val homeCategoryListener: HomeCategoryListener): RechargeRecommendationListener{

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

    override fun onRechargeRecommendationDeclineClickListener(reminderData: ReminderData,toggleTracking: Boolean) {
        // Trigger decline listener to remove widget from homepage

        if(toggleTracking){
            RechargeRecommendationTracking.homeRechargeRecommendationOnCloseTracker(
                    mapRemindertoRechargeRecommendationData(reminderData))
        }

        val requestParams = mapOf(
                DeclineRechargeRecommendationUseCase.PARAM_UUID to reminderData.UUID,
                DeclineRechargeRecommendationUseCase.PARAM_CONTENT_ID to reminderData.id)
        homeCategoryListener.declineRechargeRecommendationItem(requestParams)
    }

    override fun onRechargeRecommendationImpressionListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            RechargeRecommendationTracking.homeRechargeRecommendationImpressionTracker(
                    it, mapRemindertoRechargeRecommendationData(reminderData)
            )
        }
    }

    override fun getRechargeRecommendation() {
        homeCategoryListener.getRechargeRecommendation()
    }

    private fun mapRemindertoRechargeRecommendationData(reminderData: ReminderData): RechargeRecommendationData {
        reminderData.let {
            return RechargeRecommendationData(
                    contentID = it.id,
                    mainText = it.mainText,
                    subText = it.subText,
                    applink = it.appLink,
                    link = it.link,
                    iconURL = it.iconURL,
                    title = it.title,
                    backgroundColor = it.backgroundColor.firstOrNull() ?: "",
                    buttonText = it.buttonText
            )
        }
    }

}