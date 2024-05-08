package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

object ProductShoppingListMapper {
    fun mapAvailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.AvailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listAvailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            priceInt = it.priceInt,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            isSelected = it.isSelected,
            appLink = it.applink,
            minOrder = it.minOrder,
            shopId = it.shop.id,
            warehouseId = it.warehouseID,
            productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
        )
    }

    fun mapUnavailableShoppingList(
        listUnavailableItem: List<GetShoppingListDataResponse.UnavailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listUnavailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            isSelected = it.isSelected,
            appLink = it.applink,
            minOrder = it.minOrder,
            shopId = it.shop.id,
            warehouseId = it.warehouseID,
            productLayoutType = ShoppingListProductLayoutType.UNAVAILABLE_SHOPPING_LIST
        )
    }
}
