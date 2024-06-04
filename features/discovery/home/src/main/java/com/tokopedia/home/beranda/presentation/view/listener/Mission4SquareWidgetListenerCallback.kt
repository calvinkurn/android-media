package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.home.analytics.v2.Mission4SquareWidgetTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.analytics.Mission4SquareTrackingMapper.asCardModel
import com.tokopedia.home_component.analytics.Mission4SquareTrackingMapper.asProductModel
import com.tokopedia.home_component.viewholders.mission.v3.Mission4SquareWidgetListener
import com.tokopedia.home_component.visitable.Mission4SquareUiModel

class Mission4SquareWidgetListenerCallback(
    val homeCategoryListener: HomeCategoryListener
) : Mission4SquareWidgetListener {

    override fun onMissionClicked(model: Mission4SquareUiModel, position: Int) {
        homeCategoryListener.onDynamicChannelClicked(model.data.appLink)
        shouldTrackWhenProductOrCardClicked(model, position)
    }

    override fun onMissionImpressed(model: Mission4SquareUiModel, position: Int) {
        shouldTrackGTMImpressWidgetWhenRendered(model, position)
    }

    private fun shouldTrackWhenProductOrCardClicked(model: Mission4SquareUiModel, position: Int) {
        if (model.isProduct()) {
            // GTM
            Mission4SquareWidgetTracker.sendMissionWidgetClickedToPdp(
                model,
                position,
                homeCategoryListener.userId
            )

            // ByteIO
            AppLogRecommendation.sendProductClickAppLog(model.asProductModel(model.isCache))
        } else {
            // GTM
            Mission4SquareWidgetTracker.sendMissionWidgetClicked(
                model,
                position,
                homeCategoryListener.userId
            )

            // ByteIO
            AppLogRecommendation.sendCardClickAppLog(model.asCardModel(model.isCache))
        }

        AppLogAnalytics.setGlobalParamOnClick(
            enterMethod = AppLogParam.ENTER_METHOD_FMT_PAGENAME.format("${model.data.pageName}_${model.cardPosition + 1}")
        )
    }

    private fun shouldTrackGTMImpressWidgetWhenRendered(model: Mission4SquareUiModel, position: Int) {
        if (model.isProduct()) {
            homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                Mission4SquareWidgetTracker.getMissionWidgetProductView(
                    model,
                    position,
                    homeCategoryListener.userId
                ) as HashMap<String, Any>
            )
        } else {
            homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                Mission4SquareWidgetTracker.getMissionWidgetView(
                    model,
                    position,
                    homeCategoryListener.userId
                ) as HashMap<String, Any>
            )
        }
    }

    override fun onMissionAppLogImpressed(model: Mission4SquareUiModel, position: Int) {
        // ByteIO
        if (model.isProduct()) {
            AppLogRecommendation.sendProductShowAppLog(model.asProductModel(model.isCache))
        } else {
            AppLogRecommendation.sendCardShowAppLog(model.asCardModel(model.isCache))
        }
    }
}
