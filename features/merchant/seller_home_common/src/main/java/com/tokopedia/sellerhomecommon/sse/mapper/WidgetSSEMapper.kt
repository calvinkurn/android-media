package com.tokopedia.sellerhomecommon.sse.mapper

import com.tokopedia.sellerhomecommon.domain.mapper.CardMapper
import com.tokopedia.sellerhomecommon.presentation.model.BaseDataUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 11/10/22.
 */

class WidgetSSEMapper @Inject constructor(
    private val cardMapper: CardMapper
) {

    fun mappingWidget(event: String, data: String): BaseDataUiModel {

    }
}
