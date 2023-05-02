package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

interface RichListFactory {
    fun type(model: BaseRichListItemUiModel): Int
}