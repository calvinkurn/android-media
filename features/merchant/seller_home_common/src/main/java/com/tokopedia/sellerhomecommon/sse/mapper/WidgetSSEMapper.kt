package com.tokopedia.sellerhomecommon.sse.mapper

import com.google.gson.Gson
import com.tokopedia.sellerhomecommon.common.WidgetType
import com.tokopedia.sellerhomecommon.domain.mapper.CardMapper
import com.tokopedia.sellerhomecommon.domain.mapper.MilestoneMapper
import com.tokopedia.sellerhomecommon.domain.model.CardDataModel
import com.tokopedia.sellerhomecommon.domain.model.MilestoneData
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 11/10/22.
 */

class WidgetSSEMapper @Inject constructor(
    private val cardMapper: CardMapper,
    private val milestoneMapper: MilestoneMapper
) {

    companion object {
        private const val DELIMITER = "-"
        private const val PATTERN = "\\S+-\\S+"
    }

    fun mappingWidget(event: String, data: String): BaseDataUiModel? {
        if (!getStatusIsValidDataKey(event)) {
            return null
        }
        return when (getWidgetType(event)) {
            WidgetType.CARD -> getCardData(data)
            WidgetType.MILESTONE -> getMilestoneData(data)
            else -> null
        }
    }

    fun getStatusIsValidDataKey(event: String): Boolean {
        return event.matches(Regex(PATTERN))
    }

    private fun getMilestoneData(data: String): MilestoneDataUiModel {
        val model = Gson().fromJson(data, MilestoneData::class.java)
        return milestoneMapper.mapToUiModel(model, false)
    }

    private fun getCardData(data: String): CardDataUiModel {
        val model = Gson().fromJson(data, CardDataModel::class.java)
        return cardMapper.mapToCardUiModel(model, false)
    }

    private fun getWidgetType(event: String): String {
        return event.substringAfter(DELIMITER)
    }
}
