package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.BmgmMiniCartAdapter
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.BmgmBundledProductViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.BmgmProductPlaceholderViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.BmgmSingleProductViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.MiniCartGiftDividerViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.MiniCartGwpGiftPlaceholderViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.MiniCartGwpGiftWidgetViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartAdapterFactoryImpl(
    private val listener: BmgmMiniCartAdapter.Listener
) : BaseAdapterTypeFactory(), BmgmMiniCartAdapterFactory {

    override fun type(model: BmgmMiniCartVisitable.TierUiModel): Int {
        return BmgmBundledProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.ProductUiModel): Int {
        return BmgmSingleProductViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.PlaceholderUiModel): Int {
        return BmgmProductPlaceholderViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.GwpGiftPlaceholder): Int {
        return MiniCartGwpGiftPlaceholderViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.GwpGiftWidgetUiModel): Int {
        return MiniCartGwpGiftWidgetViewHolder.RES_LAYOUT
    }

    override fun type(model: BmgmMiniCartVisitable.DividerUiModel): Int {
        return MiniCartGiftDividerViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BmgmBundledProductViewHolder.RES_LAYOUT -> BmgmBundledProductViewHolder(parent, listener)
            BmgmSingleProductViewHolder.RES_LAYOUT -> BmgmSingleProductViewHolder(parent, listener)
            BmgmProductPlaceholderViewHolder.RES_LAYOUT -> BmgmProductPlaceholderViewHolder(parent, listener)
            MiniCartGwpGiftPlaceholderViewHolder.RES_LAYOUT -> MiniCartGwpGiftPlaceholderViewHolder(parent, listener)
            MiniCartGwpGiftWidgetViewHolder.RES_LAYOUT -> MiniCartGwpGiftWidgetViewHolder(parent, listener)
            MiniCartGiftDividerViewHolder.RES_LAYOUT -> MiniCartGiftDividerViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}