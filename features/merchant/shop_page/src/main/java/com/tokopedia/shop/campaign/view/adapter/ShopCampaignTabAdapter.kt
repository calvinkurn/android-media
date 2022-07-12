package com.tokopedia.shop.campaign.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

class ShopCampaignTabAdapter(
    shopCampaignTabAdapterTypeFactory: ShopCampaignTabAdapterTypeFactory
) : ShopHomeAdapter(shopCampaignTabAdapterTypeFactory) {

    fun setCampaignLayoutData(data: List<Visitable<*>>) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.addAll(data.onEach {
            if (it is BaseShopHomeWidgetUiModel) {
                it.widgetState = WidgetState.PLACEHOLDER
            } else if (it is ThematicWidgetUiModel) {
                it.widgetState = WidgetState.PLACEHOLDER
            }
        })
        submitList(newList)
    }

    fun isAllWidgetLoading(): Boolean {
        return visitables.filterIsInstance<Visitable<*>>().all {
            when(it) {
                is BaseShopHomeWidgetUiModel -> it.widgetState == WidgetState.PLACEHOLDER || it.widgetState == WidgetState.LOADING
                is ThematicWidgetUiModel -> it.widgetState == WidgetState.PLACEHOLDER || it.widgetState == WidgetState.LOADING
                else -> false
            }
        }
    }

}