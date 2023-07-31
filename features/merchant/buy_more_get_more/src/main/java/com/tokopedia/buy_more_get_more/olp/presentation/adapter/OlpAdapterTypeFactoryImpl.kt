package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel

class OlpAdapterTypeFactoryImpl: OlpAdapterTypeFactory, BaseAdapterTypeFactory() {
    override fun type(type: OfferInfoForBuyerUiModel) = OfferingInfoViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        val viewHolder = when(type) {
            OfferingInfoViewHolder.LAYOUT -> OfferingInfoViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
        return viewHolder
    }
}
