package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel

/**
 * Created by meyta.taliti on 31/08/23.
 */
data class FeedBrowseChipUiModel(
    val id: String,
    val label: String,
    val extraParam: WidgetRequestModel = WidgetRequestModel.Empty,
    val isSelected: Boolean
) : ImpressionableModel {

    override val impressHolder: ImpressHolder = ImpressHolder()
}
