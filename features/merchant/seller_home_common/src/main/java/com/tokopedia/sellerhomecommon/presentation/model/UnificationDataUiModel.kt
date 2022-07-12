package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

data class UnificationDataUiModel(
    override var dataKey: String,
    override var error: String,
    override var isFromCache: Boolean,
    override val showWidget: Boolean
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return false
    }
}