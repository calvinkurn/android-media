package com.tokopedia.recommendation_widget_common.byteio

import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam

class RecommendationByteIoUseCase {
    private val requestedRecommendation: HashMap<String, String> = hashMapOf()

    fun getParameter(inputParameter: GetRecommendationRequestParam): GetRecommendationRequestParam {
        val (refreshType, sessionId) = if(inputParameter.pageNumber == 1) {
            if(requestedRecommendation.contains(inputParameter.pageName)) {
                RefreshType.REFRESH
            } else {
                RefreshType.OPEN
            }.also {
                requestedRecommendation[inputParameter.pageName] = ""
            }
        } else {
            RefreshType.LOAD_MORE
        } to requestedRecommendation[inputParameter.pageName].orEmpty()
        return inputParameter.copy(
            refreshType = refreshType,
            bytedanceSessionId = sessionId
        )
    }

    fun updateSessionId(
        pageName: String,
        sessionId: String
    ) {
        requestedRecommendation[pageName] = sessionId
    }
}
