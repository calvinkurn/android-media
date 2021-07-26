package com.tokopedia.shop.info.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.info.view.adapter.viewholder.ShopInfoLogisticViewHolder
import com.tokopedia.shop.info.view.adapter.viewholder.ShopInfoLogisticViewHolder.Companion.LAYOUT
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel

/**
 * @author by alvarisi on 12/21/17.
 */
class ShopInfoLogisticAdapterTypeFactory : BaseAdapterTypeFactory(), ShopInfoLogisticTypeFactory {
    override fun type(viewModel: ShopInfoLogisticUiModel?): Int {
        return LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == LAYOUT) {
            ShopInfoLogisticViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}