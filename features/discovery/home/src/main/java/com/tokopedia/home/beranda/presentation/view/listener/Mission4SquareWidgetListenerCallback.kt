package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
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
        shouldTrackWhenProductOrCardClicked(model)
    }

    override fun onMissionImpressed(model: Mission4SquareUiModel, position: Int) {
        shouldTrackImpressWidgetWhenRendered(model)
    }

    private fun shouldTrackWhenProductOrCardClicked(model: Mission4SquareUiModel) {
        if (model.isProduct()) {
            AppLogRecommendation.sendProductClickAppLog(
                model.asProductModel(
                    isCache = model.isCache,
                    enterMethod = "${model.data.pageName}_${model.cardPosition+1}"
                )
            )
        } else {
            AppLogRecommendation.sendCardClickAppLog(
                model.asCardModel(model.isCache)
            )
        }
    }

    private fun shouldTrackImpressWidgetWhenRendered(model: Mission4SquareUiModel) {
        if (model.isProduct()) {
            AppLogRecommendation.sendProductShowAppLog(model.asProductModel(model.isCache))
        } else {
            AppLogRecommendation.sendCardShowAppLog(model.asCardModel(model.isCache))
        }
    }
}
