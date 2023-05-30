package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

interface RichListFactory {
    fun type(model: BaseRichListItem.RankItemUiModel): Int
    fun type(model: BaseRichListItem.CaptionItemUiModel): Int
    fun type(model: BaseRichListItem.TickerItemUiModel): Int
    fun type(model: BaseRichListItem.LoadingStateUiModel): Int
    fun type(model: BaseRichListItem.ErrorStateUiModel): Int
}