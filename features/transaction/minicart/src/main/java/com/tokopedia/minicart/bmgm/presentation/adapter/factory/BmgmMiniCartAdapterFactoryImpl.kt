package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmProductPlaceholderViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmSingleProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmBundledProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.model.BmgmBundledProductUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmProductPlaceholderUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmSingleProductUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartAdapterFactoryImpl : BaseAdapterTypeFactory(), BmgmMiniCartAdapterFactory {

    override fun type(model: BmgmSingleProductUiModel): Int {
        return BmgmSingleProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmBundledProductUiModel): Int {
        return BmgmBundledProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmProductPlaceholderUiModel): Int {
        return BmgmProductPlaceholderViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BmgmBundledProductViewHolder.RES_LAYOUT -> BmgmBundledProductViewHolder(parent)
            BmgmSingleProductViewHolder.RES_LAYOUT -> BmgmSingleProductViewHolder(parent)
            BmgmProductPlaceholderViewHolder.RES_LAYOUT -> BmgmProductPlaceholderViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}