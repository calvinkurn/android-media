package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.viewholder.BuyerNormalProductViewHolder
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel

class BuyerProductBundlingAdapterFactory: BaseAdapterTypeFactory(),
    BuyerProductBundlingTypeFactory {

    override fun type(uiModel: BuyerNormalProductUiModel): Int = BuyerNormalProductViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BuyerNormalProductViewHolder.LAYOUT -> BuyerNormalProductViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}