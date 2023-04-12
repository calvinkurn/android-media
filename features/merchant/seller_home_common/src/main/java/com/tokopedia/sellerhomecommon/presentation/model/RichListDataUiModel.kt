package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

data class RichListDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel()
) : BaseDataUiModel, LastUpdatedDataInterface  {

    override fun isWidgetEmpty(): Boolean {
        return false
    }
}