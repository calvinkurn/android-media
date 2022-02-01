package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetCardDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class CardMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) : BaseWidgetMapper(lastUpdatedSharedPref),
    BaseResponseMapper<GetCardDataResponse, List<CardDataUiModel>> {

    companion object {
        private const val ZERO = "0"
        private const val STATE_DANGER = "DANGER"
        private const val STATE_WARNING = "WARNING"
    }

    override fun mapRemoteDataToUiData(
        response: GetCardDataResponse,
        isFromCache: Boolean
    ): List<CardDataUiModel> {
        return response.getCardData?.cardData.orEmpty().map {
            CardDataUiModel(
                dataKey = it.dataKey.orEmpty(),
                description = it.description.orEmpty(),
                error = it.errorMsg.orEmpty(),
                state = when (it.state) {
                    STATE_WARNING -> CardDataUiModel.State.WARNING
                    STATE_DANGER -> CardDataUiModel.State.DANGER
                    else -> CardDataUiModel.State.NORMAL
                },
                value = if (it.value.isNullOrBlank()) ZERO else it.value,
                isFromCache = isFromCache,
                showWidget = it.showWidget.orFalse(),
                lastUpdated = getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache)
            )
        }
    }
}