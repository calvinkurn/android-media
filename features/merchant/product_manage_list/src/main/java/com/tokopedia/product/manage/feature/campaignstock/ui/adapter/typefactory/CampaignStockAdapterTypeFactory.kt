package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder.ActiveProductSwitchViewHolder
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder.TotalStockEditorViewHolder
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.ActiveProductSwitchUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.TotalStockEditorUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.VariantStockEditorUiModel

class CampaignStockAdapterTypeFactory: BaseAdapterTypeFactory(), CampaignStockTypeFactory {

    override fun type(model: ActiveProductSwitchUiModel): Int = ActiveProductSwitchViewHolder.LAYOUT_RES

    override fun type(model: TotalStockEditorUiModel): Int = TotalStockEditorViewHolder.LAYOUT_RES

    override fun type(model: VariantStockEditorUiModel): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ActiveProductSwitchViewHolder.LAYOUT_RES -> ActiveProductSwitchViewHolder(parent)
            TotalStockEditorViewHolder.LAYOUT_RES -> TotalStockEditorViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}