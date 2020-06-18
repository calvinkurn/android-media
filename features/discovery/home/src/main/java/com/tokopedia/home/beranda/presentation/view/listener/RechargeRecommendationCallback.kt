package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.RechargeRecommendationTracking
import com.tokopedia.home.beranda.domain.interactor.DeclineRechargeRecommendationUseCase
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendationData
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home_component.listener.RechargeRecommendationListener
import com.tokopedia.home_component.model.ReminderData

class RechargeRecommendationCallback (val context: Context?, val viewModel: HomeViewModel,
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

    override fun onRechargeRecommendationDeclineClickListener(reminderData: ReminderData) {
        // Trigger decline listener to remove widget from homepage
        val requestParams = mapOf(
                DeclineRechargeRecommendationUseCase.PARAM_UUID to reminderData.UUID,
                DeclineRechargeRecommendationUseCase.PARAM_CONTENT_ID to reminderData.id.toString())
        viewModel.declineRechargeRecommendationItem(requestParams)
    }

    override fun onRechargeRecommendationDeclineTrackingListener(reminderData: ReminderData) {
        RechargeRecommendationTracking.homeRechargeRecommendationOnCloseTracker(
                mapRemindertoRechargeRecommendationData(reminderData))
    }

    override fun onRechargeRecommendationImpressionListener(reminderData: ReminderData) {
        homeCategoryListener.getTrackingQueueObj()?.let {
            RechargeRecommendationTracking.homeRechargeRecommendationImpressionTracker(
                    it, mapRemindertoRechargeRecommendationData(reminderData)
            )
        }
    }

    override fun getRechargeRecommendation() {
        viewModel.getRechargeRecommendation()
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
                    backgroundColor = it.backgroundColor,
                    buttonText = it.buttonText
            )
        }
    }

}