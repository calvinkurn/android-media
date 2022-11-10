package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

data class UnificationDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val tabs: List<UnificationTabUiModel> = listOf()
) : BaseDataUiModel, LastUpdatedDataInterface  {

    override fun isWidgetEmpty(): Boolean {
        return tabs.isEmpty()
    }
}