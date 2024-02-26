package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.flashsaletoko

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.home_component.widget.shop_tab.ShopTabDataModel

object FlashSaleTokoTabMapper {
    fun ArrayList<ComponentsItem>.mapToShopTabDataModel(isFestiveApplied: Boolean): MutableList<ShopTabDataModel> {
        val tabData = mutableListOf<ShopTabDataModel>()

        forEach { item ->
            item.data?.first()?.run {
                val tab = ShopTabDataModel(
                    id = filterValue.orEmpty(),
                    shopName = name.orEmpty(),
                    imageUrl = shopLogo.orEmpty(),
                    badgesUrl = shopBadgeImageUrl.orEmpty(),
                    isActivated = isSelected,
                    useGradientBackground = !isFestiveApplied
                )

                tabData.add(tab)
            }
        }

        return tabData
    }
}
