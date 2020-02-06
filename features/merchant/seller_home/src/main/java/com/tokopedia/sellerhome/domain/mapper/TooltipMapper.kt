package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.TooltipListItemModel
import com.tokopedia.sellerhome.domain.model.TooltipModel
import com.tokopedia.sellerhome.view.model.TooltipListItemUiModel
import com.tokopedia.sellerhome.view.model.TooltipUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-02-05
 */

class TooltipMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(tooltip: TooltipModel): TooltipUiModel {
        return TooltipUiModel(
                title = tooltip.title.orEmpty(),
                content = tooltip.content.orEmpty(),
                list = mapTooltipItem(tooltip.list)
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