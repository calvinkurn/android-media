package com.tokopedia.home_component.widget.shop_flash_sale

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselVisitable
import com.tokopedia.home_component.widget.shop_flash_sale.item.ProductCardGridShimmerDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component_header.model.ChannelHeader

data class ShopFlashSaleWidgetDataModel(
    val id: String = "",
    val channelModel: ChannelModel,
    val channelHeader: ChannelHeader = ChannelHeader(),
    val tabList: List<ShopFlashSaleTabDataModel> = listOf(),
    val itemList: List<HomeComponentCarouselVisitable> = listOf(),
    val timer: ShopFlashSaleTimerDataModel = ShopFlashSaleTimerDataModel(),
    val useShopHeader: Boolean = false,
    val cardInteraction: Boolean = false,
): HomeComponentVisitable {

    companion object {
        const val PAYLOAD_ITEM_LIST_CHANGED = "itemListChanged"
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

    fun isLoadingState(): Boolean {
        return itemList.all { it is ProductCardGridShimmerDataModel }
    }
}
