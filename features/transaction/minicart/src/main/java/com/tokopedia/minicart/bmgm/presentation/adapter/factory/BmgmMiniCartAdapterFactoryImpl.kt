package com.tokopedia.minicart.bmgm.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmProductPlaceholderViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmSingleProductViewHolder
import com.tokopedia.minicart.bmgm.presentation.adapter.viewholder.BmgmBundledProductViewHolder
import com.tokopedia.purchase_platform.common.feature.bmgm.adapter.BmgmMiniCartAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.uimodel.BmgmCommonDataUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartAdapterFactoryImpl(
    private val listener: BmgmMiniCartAdapter.Listener
) : BaseAdapterTypeFactory(), BmgmMiniCartAdapterFactory {

    override fun type(model: BmgmCommonDataUiModel.SingleProductUiModel): Int {
        return BmgmSingleProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmCommonDataUiModel.BundledProductUiModel): Int {
        return BmgmBundledProductViewHolder.RES_LAYOUT
    }

    override fun type(model: Visitable<*>): Int {
        return BmgmProductPlaceholderViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BmgmBundledProductViewHolder.RES_LAYOUT -> BmgmBundledProductViewHolder(parent, listener)
            BmgmSingleProductViewHolder.RES_LAYOUT -> BmgmSingleProductViewHolder(parent, listener)
            BmgmProductPlaceholderViewHolder.RES_LAYOUT -> BmgmProductPlaceholderViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}