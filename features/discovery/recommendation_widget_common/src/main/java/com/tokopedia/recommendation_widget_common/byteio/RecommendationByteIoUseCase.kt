package com.tokopedia.recommendation_widget_common.byteio

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam

class RecommendationByteIoUseCase {
    private val requestedRecommendation: HashMap<String, RecommendationByteIoParam> = hashMapOf()

    fun getParameter(inputParameter: GetRecommendationRequestParam): GetRecommendationRequestParam {
        val refreshType = if(inputParameter.pageNumber == 1) {
            if(requestedRecommendation.contains(inputParameter.pageName)) {
                RefreshType.REFRESH
            } else {
                RefreshType.OPEN
            }.also {
                requestedRecommendation.remove(inputParameter.pageName)
            }
        } else {
            RefreshType.LOAD_MORE
        }
        val sessionId = requestedRecommendation[inputParameter.pageName]?.sessionId.orEmpty()
        return inputParameter.copy(
            refreshType = refreshType,
            bytedanceSessionId = sessionId
        )
    }

    fun updateMap(
        pageName: String,
        sessionId: String,
        totalData: Int,
    ) {
        val currentData = requestedRecommendation[pageName]
        requestedRecommendation[pageName] = currentData?.copy(
            sessionId = sessionId,
            totalData = totalData + currentData.totalData
        ) ?: RecommendationByteIoParam(
            sessionId = sessionId,
            totalData = totalData
        )
    }

    fun getTotalData(pageName: String): Int {
        return requestedRecommendation[pageName]?.totalData.orZero()
    }

    private data class RecommendationByteIoParam (
        val sessionId: String,
        val totalData: Int,
    )
}
