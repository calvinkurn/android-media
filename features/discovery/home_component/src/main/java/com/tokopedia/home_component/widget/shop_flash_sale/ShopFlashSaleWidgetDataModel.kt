package com.tokopedia.home_component.widget.shop_flash_sale

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.home_component.widget.common.DataStatus
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleErrorDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleProductGridShimmerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component_header.model.ChannelHeader

/**
 * Shop Flash Sale Widget model
 * @author Frenzel
 * @param[id] id of the widget
 * @param[channelModel] channel model for divider, query param, tracking, etc.
 * @param[channelHeader] header model
 * @param[tabList] list of tab models
 * @param[itemList] visitable list of carousel component.
 * @param[timer] timer model (optional). Won't be rendered if null.
 */
data class ShopFlashSaleWidgetDataModel(
    val id: String = "",
    val channelModel: ChannelModel,
    val channelHeader: ChannelHeader = ChannelHeader(),
    val tabList: List<ShopFlashSaleTabDataModel> = listOf(),
    val itemList: List<Visitable<out CommonCarouselProductCardTypeFactory>> = listOf(),
    val timer: ShopFlashSaleTimerDataModel? = null,
): HomeComponentVisitable {

    companion object {
        const val PAYLOAD_ITEM_LIST_CHANGED = "payloadItemListChanged"
    }

    override fun visitableId(): String = id

    override fun equalsWith(b: Any?): Boolean {
        return if(b is ShopFlashSaleWidgetDataModel) {
            b == this
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        val bundle = Bundle()
        if(b is ShopFlashSaleWidgetDataModel && b.itemList != this.itemList) {
            bundle.putBoolean(PAYLOAD_ITEM_LIST_CHANGED, true)
        }
        return bundle
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getStatus(): DataStatus {
        return if(itemList.all { it is ShopFlashSaleProductGridShimmerDataModel })
            DataStatus.LOADING
        else if(itemList.all { it is ShopFlashSaleErrorDataModel })
            DataStatus.ERROR
        else DataStatus.SUCCESS
    }

    fun getShopTab(id: String): ShopFlashSaleTabDataModel? {
        return tabList.find { it.channelGrid.id == id }
    }

    fun getActivatedTab(): ShopFlashSaleTabDataModel? {
        return tabList.find { it.isActivated }
    }

    fun activateShopTab(id: String): ShopFlashSaleWidgetDataModel {
        val newList = tabList.map {
            it.copy(isActivated = it.channelGrid.id == id)
        }
        return copy(tabList = newList)
    }
}
