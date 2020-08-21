package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.domain.model.TooltipListItemModel
import com.tokopedia.sellerhomecommon.domain.model.TooltipModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class TooltipMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(tooltip: TooltipModel): TooltipUiModel {
        return TooltipUiModel(
                title = tooltip.title.orEmpty(),
                content = tooltip.content.orEmpty(),
                list = mapTooltipItem(tooltip.list),
                shouldShow = tooltip.shouldShow
        )
    }

    private fun mapTooltipItem(tooltipItems: List<TooltipListItemModel>?): List<TooltipListItemUiModel> {
        return tooltipItems.orEmpty().map {
            TooltipListItemUiModel(
                    title = it.title.orEmpty(),
                    description = it.description.orEmpty()
            )
        }
    }
}