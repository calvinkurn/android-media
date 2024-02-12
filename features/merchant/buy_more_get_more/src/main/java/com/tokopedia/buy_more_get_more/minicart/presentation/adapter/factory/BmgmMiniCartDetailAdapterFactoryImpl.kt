package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.BmgmMiniCartDetailProductViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.viewholder.BmgmMiniCartDetailSectionViewHolder
import com.tokopedia.buy_more_get_more.minicart.presentation.model.MiniCartDetailUiModel

/**
 * Created by @ilhamsuaib on 31/07/23.
 */

class BmgmMiniCartDetailAdapterFactoryImpl : BaseAdapterTypeFactory(),
    BmgmMiniCartDetailAdapterFactory {

    override fun type(model: MiniCartDetailUiModel.Section): Int {
        return BmgmMiniCartDetailSectionViewHolder.RES_LAYOUT
    }

    override fun type(model: MiniCartDetailUiModel.Product): Int {
        return BmgmMiniCartDetailProductViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BmgmMiniCartDetailSectionViewHolder.RES_LAYOUT -> BmgmMiniCartDetailSectionViewHolder(
                parent
            )

            BmgmMiniCartDetailProductViewHolder.RES_LAYOUT -> BmgmMiniCartDetailProductViewHolder(
                parent
            )

            else -> super.createViewHolder(parent, type)
        }
    }
}