package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.sellerhomecommon.domain.model.ProgressDataModel
import com.tokopedia.sellerhomecommon.presentation.model.ProgressDataUiModel
import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class ProgressMapper @Inject constructor() {

    companion object {
        private const val GOOD = "GOOD"
        private const val WARNING = "WARNING"
        private const val DANGER = "DANGER"
    }

    fun mapResponseToUi(progressDataResponse: List<ProgressDataModel>, isFromCache: Boolean): List<ProgressDataUiModel> {
        return progressDataResponse.map{
            ProgressDataUiModel(
                    valueTxt = it.valueText.orEmpty(),
                    maxValueTxt = it.maxValueText.orEmpty(),
                    value = it.value.toZeroIfNull(),
                    maxValue = it.maxValue.toZeroIfNull(),
                    colorState = mapState(it.state.orEmpty()),
                    error = it.errorMessage.orEmpty(),
                    subtitle = it.subtitle.orEmpty(),
                    dataKey = it.dataKey.orEmpty(),
                    isFromCache = isFromCache,
                    showWidget = it.showWidget.orFalse()
            )
        }
    }

    private fun mapState(state: String?) : ShopScorePMWidget.State {
        return when(state) {
            GOOD -> ShopScorePMWidget.State.Good
            WARNING -> ShopScorePMWidget.State.Warning
            DANGER -> ShopScorePMWidget.State.Danger
            else -> ShopScorePMWidget.State.Good
        }
    }

}