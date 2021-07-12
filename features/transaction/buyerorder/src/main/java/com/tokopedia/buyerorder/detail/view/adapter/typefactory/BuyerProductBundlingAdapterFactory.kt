package com.tokopedia.buyerorder.detail.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerBundlingProductUiModel
import com.tokopedia.buyerorder.detail.view.adapter.uimodel.BuyerNormalProductUiModel
import com.tokopedia.buyerorder.detail.view.adapter.viewholder.BuyerBundlingProductViewHolder
import com.tokopedia.buyerorder.detail.view.adapter.viewholder.BuyerNormalProductViewHolder

class BuyerProductBundlingAdapterFactory: BaseAdapterTypeFactory(), BuyerProductBundlingTypeFactory {

    override fun type(uiModel: BuyerNormalProductUiModel): Int = BuyerNormalProductViewHolder.LAYOUT

    override fun type(uiModel: BuyerBundlingProductUiModel): Int = BuyerBundlingProductViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BuyerNormalProductViewHolder.LAYOUT -> BuyerNormalProductViewHolder(parent)
            BuyerBundlingProductViewHolder.LAYOUT -> BuyerBundlingProductViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}