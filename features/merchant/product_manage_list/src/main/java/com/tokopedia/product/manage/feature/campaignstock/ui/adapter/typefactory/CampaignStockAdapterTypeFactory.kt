package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder.*
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.*

class CampaignStockAdapterTypeFactory(private val onAccordionStateChange: (Int) -> Unit = {}): BaseAdapterTypeFactory(), CampaignStockTypeFactory {

    override fun type(model: ActiveProductSwitchUiModel): Int = ActiveProductSwitchViewHolder.LAYOUT_RES

    override fun type(model: TotalStockEditorUiModel): Int = TotalStockEditorViewHolder.LAYOUT_RES

    override fun type(model: StockTickerInfoUiModel): Int = StockTickerInfoViewHolder.LAYOUT_RES

    override fun type(model: ReservedEventInfoUiModel): Int = ReservedEventInfoViewHolder.LAYOUT_RES

    override fun type(model: SellableStockProductUIModel): Int = SellableStockProductViewHolder.LAYOUT_RES

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ActiveProductSwitchViewHolder.LAYOUT_RES -> ActiveProductSwitchViewHolder(parent)
            TotalStockEditorViewHolder.LAYOUT_RES -> TotalStockEditorViewHolder(parent)
            SellableStockProductViewHolder.LAYOUT_RES -> SellableStockProductViewHolder(parent)
            StockTickerInfoViewHolder.LAYOUT_RES -> StockTickerInfoViewHolder(parent)
            ReservedEventInfoViewHolder.LAYOUT_RES -> ReservedEventInfoViewHolder(parent, onAccordionStateChange)
            else -> super.createViewHolder(parent, type)
        }
    }
}