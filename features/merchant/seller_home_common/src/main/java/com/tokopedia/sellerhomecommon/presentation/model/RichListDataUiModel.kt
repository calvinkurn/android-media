package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 08/07/22.
 */

data class RichListDataUiModel(
    override var dataKey: String = String.EMPTY,
    override var error: String = String.EMPTY,
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    val title: String = String.EMPTY,
    val subtitle: String = String.EMPTY,
    val lastUpdated: String = String.EMPTY,
    val richListData: List<BaseRichListItem> = emptyList()
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return richListData.isEmpty()
    }
}