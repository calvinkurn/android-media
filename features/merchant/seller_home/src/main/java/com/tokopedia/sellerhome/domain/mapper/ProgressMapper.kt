package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.sellerhome.domain.model.ProgressDataModel
import com.tokopedia.sellerhome.view.model.ProgressDataUiModel
import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget
import javax.inject.Inject

class ProgressMapper @Inject constructor() {

    companion object {
        private const val GOOD = "GOOD"
        private const val WARNING = "WARNING"
        private const val DANGER = "DANGER"
    }

    fun mapResponseToUi(progressDataResponse: List<ProgressDataModel>): List<ProgressDataUiModel> {
        return progressDataResponse.map{
            ProgressDataUiModel(
                    valueTxt = it.valueText.orEmpty(),
                    maxValueTxt = it.maxValueText.orEmpty(),
                    value = it.value.toZeroIfNull(),
                    maxValue = it.maxValue.toZeroIfNull(),
                    colorState = mapState(it.state.orEmpty()),
                    error = it.errorMessage.orEmpty(),
                    subtitle = it.subtitle.orEmpty(),
                    dataKey = it.dataKey.orEmpty()
            )
        }
    }

    private fun mapState(state: String?) : ShopScorePMWidget.State {
        return when(state) {
            GOOD -> ShopScorePMWidget.State.GOOD
            WARNING -> ShopScorePMWidget.State.WARNING
            DANGER -> ShopScorePMWidget.State.DANGER
            else -> ShopScorePMWidget.State.GOOD
        }
    }

}