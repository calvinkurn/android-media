package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.domain.model.GetCardDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class CardMapper @Inject constructor(): BaseResponseMapper<GetCardDataResponse, List<CardDataUiModel>> {

    override fun mapRemoteDataToUiData(response: GetCardDataResponse, isFromCache: Boolean): List<CardDataUiModel> {
        return response.getCardData?.cardData.orEmpty().map {
            CardDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    description = it.description.orEmpty(),
                    error = it.errorMsg.orEmpty(),
                    state = it.state.orEmpty(),
                    value = if (it.value.isNullOrBlank()) "0" else it.value,
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }
}