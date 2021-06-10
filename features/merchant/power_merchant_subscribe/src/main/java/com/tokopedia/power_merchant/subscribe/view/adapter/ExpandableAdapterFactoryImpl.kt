package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.ExpandableItemViewHolder
import com.tokopedia.power_merchant.subscribe.view.adapter.viewholder.ExpandableSectionViewHolder
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableItemUiModel
import com.tokopedia.power_merchant.subscribe.view.model.ExpandableSectionUiModel

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableAdapterFactoryImpl(
        private val powerMerchantTracking: PowerMerchantTracking
) : BaseAdapterTypeFactory(), ExpandableAdapterFactory {

    override fun type(model: ExpandableSectionUiModel): Int = ExpandableSectionViewHolder.RES_LAYOUT

    override fun type(model: ExpandableItemUiModel): Int = ExpandableItemViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ExpandableSectionViewHolder.RES_LAYOUT -> ExpandableSectionViewHolder(parent)
            ExpandableItemViewHolder.RES_LAYOUT -> ExpandableItemViewHolder(parent, powerMerchantTracking)
            else -> super.createViewHolder(parent, type)
        }
    }
}