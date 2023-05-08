package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

interface RichListFactory {
    fun type(model: BaseRichListItemUiModel.RankItemUiModel): Int
    fun type(model: BaseRichListItemUiModel.TickerItemUiModel): Int
    fun type(model: BaseRichListItemUiModel.CaptionItemUiModel): Int
}