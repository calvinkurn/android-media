package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

interface BmgmMiniCartAdapterFactory : AdapterTypeFactory {

    fun type(model: BmgmMiniCartVisitable.TierUiModel): Int = -1
    fun type(model: BmgmMiniCartVisitable.ProductUiModel): Int = -1
    fun type(model: BmgmMiniCartVisitable.PlaceholderUiModel): Int = -1
    fun type(model: BmgmMiniCartVisitable.GwpGiftWidgetUiModel): Int = -1
    fun type(model: BmgmMiniCartVisitable.GwpGiftPlaceholder): Int = -1
    fun type(model: GwpMiniCartEditorVisitable.GiftMessageUiModel): Int = -1
    fun type(model: GwpMiniCartEditorVisitable.MiniCartEditorLoadingState): Int = -1
    fun type(model: GwpMiniCartEditorVisitable.MiniCartEditorErrorState): Int = -1
}