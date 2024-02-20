package com.tokopedia.buy_more_get_more.minicart.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.GwpMiniCartEditorAdapterFactoryImpl
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BaseMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 05/12/23.
 */

class GwpMiniCartEditorAdapter(listener: Listener) : BaseListAdapter<BaseMiniCartVisitable, AdapterTypeFactory>(
    GwpMiniCartEditorAdapterFactoryImpl(listener)
) {

    interface Listener {
        fun onCartItemQuantityChanged(product: BmgmMiniCartVisitable.ProductUiModel, newQty: Int)
        fun onCartItemQuantityDeleted(product: BmgmMiniCartVisitable.ProductUiModel)
        fun onReloadClicked()
        fun onGiftCtaClicked()
    }
}