package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetProgressDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.LastUpdatedUiModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class ProgressMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    private val lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetProgressDataResponse, List<ProgressDataUiModel>> {

    companion object {
        private const val GOOD = "GOOD"
        private const val WARNING = "WARNING"
    }

    override fun mapRemoteDataToUiData(
        response: GetProgressDataResponse,
        isFromCache: Boolean
    ): List<ProgressDataUiModel> {
        return response.getProgressBarData?.progressData.orEmpty().map {
            ProgressDataUiModel(
                valueTxt = it.valueText.orEmpty(),
                maxValueTxt = it.maxValueText.orEmpty(),
                value = it.value.orZero(),
                maxValue = it.maxValue.orZero(),
                colorState = mapState(it.state),
                error = it.errorMessage.orEmpty(),
                subtitle = it.subtitle.orEmpty(),
                dataKey = it.dataKey.orEmpty(),
                isFromCache = isFromCache,
                showWidget = it.showWidget.orFalse(),
                lastUpdated = if (it.updateInfo.isNullOrBlank()) {
                    getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache)
                } else {
                    LastUpdatedUiModel(
                        lastUpdatedInMillis = convertToMilliseconds(it.updateInfo.orEmpty()),
                        needToUpdated = isFromCache,
                        isEnabled = lastUpdatedEnabled
                    )
                }
            )
        }
    }

    private fun convertToMilliseconds(updateDate: String): Long {
        return DateTimeUtil.getTimeInMillis(
            updateDate,
            DateTimeUtil.FORMAT_DD_MM_YYYY
        )
    }

    private fun mapState(state: String?): ShopScorePMWidget.State {
        return when (state) {
            GOOD -> ShopScorePMWidget.State.Good
            WARNING -> ShopScorePMWidget.State.Warning
            else -> ShopScorePMWidget.State.Danger
        }
    }
}