package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

data class Mission4SquareUiModel(
    val data: MissionWidgetDataModel,
    val channelName: String,
    val channelId: String,
    val header: ChannelHeader,
    val verticalPosition: Int,
    val cardPosition: Int,
    val isCache: Boolean,
    val card: SmallProductModel,
    val appLog: RecommendationAppLog,
    val appLogImpressHolder: ImpressHolder = ImpressHolder(),
) : ImpressHolder() {

    fun isProduct(): Boolean {
        return this.data.productID.isNotBlank() && this.data.productID != "0"
    }
}
