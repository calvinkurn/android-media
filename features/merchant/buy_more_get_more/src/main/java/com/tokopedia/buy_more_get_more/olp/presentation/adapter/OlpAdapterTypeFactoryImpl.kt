package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.olp.domain.entity.EmptyStateUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.EmptyStateViewHolder
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.OfferingInfoViewHolder
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.OfferingProductListViewHolder
import com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder.OfferingProductSortingViewHolder
import com.tokopedia.buy_more_get_more.olp.presentation.listener.AtcProductListener
import com.tokopedia.buy_more_get_more.olp.presentation.listener.OfferingInfoListener
import com.tokopedia.buy_more_get_more.sort.listener.ProductSortListener

class OlpAdapterTypeFactoryImpl(
    private val sortListener: ProductSortListener,
    private val atcProductListener: AtcProductListener,
    private val offeringInfoListener: OfferingInfoListener,
) : OlpAdapterTypeFactory, BaseAdapterTypeFactory() {
    override fun type(type: OfferInfoForBuyerUiModel) = OfferingInfoViewHolder.LAYOUT
    override fun type(type: OfferProductSortingUiModel) = OfferingProductSortingViewHolder.LAYOUT
    override fun type(type: OfferProductListUiModel.Product) = OfferingProductListViewHolder.LAYOUT
    override fun type(type: EmptyStateUiModel): Int = EmptyStateViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        val viewHolder = when (type) {
            OfferingInfoViewHolder.LAYOUT -> OfferingInfoViewHolder(parent, offeringInfoListener)
            OfferingProductSortingViewHolder.LAYOUT -> OfferingProductSortingViewHolder(parent, sortListener)
            OfferingProductListViewHolder.LAYOUT -> OfferingProductListViewHolder(parent, atcProductListener)
            EmptyStateViewHolder.LAYOUT -> EmptyStateViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}
