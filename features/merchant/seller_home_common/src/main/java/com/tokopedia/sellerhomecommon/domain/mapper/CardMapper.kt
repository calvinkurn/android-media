package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.CardDataModel
import com.tokopedia.sellerhomecommon.presentation.model.CardDataUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class CardMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(items: List<CardDataModel>): List<CardDataUiModel> {
        return items.map {
            CardDataUiModel(
                    dataKey = it.dataKey.orEmpty(),
                    description = it.description.orEmpty(),
                    error = it.errorMsg.orEmpty(),
                    state = it.state.orEmpty(),
                    value = if (it.value.isNullOrBlank()) "0" else it.value
            )
        }
    }
}