package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.CardDataModel
import com.tokopedia.sellerhome.view.model.CardDataUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-01-30
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