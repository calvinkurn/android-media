package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder.*
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

class CampaignStockAdapterTypeFactory(private val onVariantReservedEventInfoClicked: (String, MutableList<ReservedEventInfoUiModel>) -> Unit = { _,_ -> },
                                      private val onTotalStockChanged: (String, Int, Int?) -> Unit = { _,_,_ -> },
                                      private val onActiveStockChanged: (Boolean) -> Unit = {},
                                      private val onVariantStockChanged: (productId: String, stock: Int, maxStock: Int?) -> Unit = { _,_,_ -> },
                                      private val onVariantStatusChanged: (productId: String, status: ProductStatus) -> Unit = { _,_ -> },
                                      private val onOngoingPromotionClicked: (campaignTypeList: List<ProductCampaignType>) -> Unit = {},
                                      private val source: String = "",
                                      private val shopId: String = ""
): BaseAdapterTypeFactory(), CampaignStockTypeFactory {

    override fun type(model: ActiveProductSwitchUiModel): Int = ActiveProductSwitchViewHolder.LAYOUT_RES

    override fun type(model: TotalStockEditorUiModel): Int = TotalStockEditorViewHolder.LAYOUT_RES

    override fun type(model: ReservedEventInfoUiModel): Int = ReservedEventInfoViewHolder.LAYOUT_RES

    override fun type(model: VariantReservedEventInfoUiModel): Int = VariantReservedEventInfoViewHolder.LAYOUT_RES

    override fun type(model: SellableStockProductUIModel): Int = SellableStockProductViewHolder.LAYOUT_RES

    override fun type(model: ReservedStockRedirectionUiModel): Int = ReservedStockRedirectionViewHolder.LAYOUT_RES

    override fun type(model: CampaignStockTickerUiModel): Int = CampaignStockTickerViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ActiveProductSwitchViewHolder.LAYOUT_RES -> ActiveProductSwitchViewHolder(
                parent, onActiveStockChanged, source, shopId)
            TotalStockEditorViewHolder.LAYOUT_RES -> TotalStockEditorViewHolder(
                parent, onTotalStockChanged, onOngoingPromotionClicked, source, shopId)
            SellableStockProductViewHolder.LAYOUT_RES -> SellableStockProductViewHolder(
                    parent, onVariantStockChanged, onVariantStatusChanged, onOngoingPromotionClicked, source, shopId
            )
            ReservedEventInfoViewHolder.LAYOUT_RES -> ReservedEventInfoViewHolder(parent)
            VariantReservedEventInfoViewHolder.LAYOUT_RES -> VariantReservedEventInfoViewHolder(parent, onVariantReservedEventInfoClicked)
            ReservedStockRedirectionViewHolder.LAYOUT_RES -> ReservedStockRedirectionViewHolder(parent)
            CampaignStockTickerViewHolder.LAYOUT -> CampaignStockTickerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}