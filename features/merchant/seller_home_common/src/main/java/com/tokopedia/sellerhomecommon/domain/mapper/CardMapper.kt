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
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetCardDataResponse, List<CardDataUiModel>> {

    companion object {
        private const val ZERO = "0"
        private const val STATE_DANGER = "DANGER"
        private const val STATE_WARNING = "WARNING"
        private const val STATE_GOOD = "GOOD"
        private const val STATE_GOOD_PLUS = "GOOD_PLUS"
        private const val STATE_WARNING_PLUS = "WARNING_PLUS"
        private const val STATE_DANGER_PLUS = "DANGER_PLUS"
    }

    override fun mapRemoteDataToUiData(
        response: GetCardDataResponse,
        isFromCache: Boolean
    ): List<CardDataUiModel> {
        return response.getCardData?.cardData.orEmpty().map {
            CardDataUiModel(
                dataKey = it.dataKey.orEmpty(),
                description = it.description.orEmpty(),
                secondaryDescription = it.secondaryDescription.orEmpty(),
                error = it.errorMsg.orEmpty(),
                state = when (it.state) {
                    STATE_GOOD -> CardDataUiModel.State.GOOD
                    STATE_WARNING -> CardDataUiModel.State.WARNING
                    STATE_DANGER -> CardDataUiModel.State.DANGER
                    STATE_GOOD_PLUS -> CardDataUiModel.State.GOOD_PLUS
                    STATE_WARNING_PLUS -> CardDataUiModel.State.WARNING_PLUS
                    STATE_DANGER_PLUS -> CardDataUiModel.State.DANGER_PLUS
                    else -> CardDataUiModel.State.NORMAL
                },
                value = if (it.value.isNullOrBlank()) ZERO else it.value,
                isFromCache = isFromCache,
                showWidget = it.showWidget.orFalse(),
                lastUpdated = getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache),
                badgeImageUrl = it.badgeImageUrl.orEmpty()
            )
        }
    }
}