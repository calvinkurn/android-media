package com.tokopedia.tokopedianow.data

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartItemKey
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

object ShoppingListDataFactory {
    object Main {
        fun createMiniCart(
            miniCartItems: Map<MiniCartItemKey, MiniCartItem> = mapOf(
                MiniCartItemKey("1111111") to MiniCartItem.MiniCartItemProduct(
                    productId = "1111111",
                    productParentId = "0",
                    quantity = 1,
                    cartId = "12345",
                )
            )
        ) = MiniCartSimplifiedData(
            miniCartItems = miniCartItems
        )

        fun createShoppingList(
            listAvailableItem: List<GetShoppingListDataResponse.AvailableItem> = listOf(
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111111",
                    name = "product 1",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.25.000",
                    priceInt = 25000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "4 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.50.000",
                    applink = "tokopedia://product/1111111",
                    minOrder = 1,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111112",
                    name = "product 2",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111115",
                    name = "product 5",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111116",
                    name = "product 6",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111117",
                    name = "product 7",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111118",
                    name = "product 8",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.AvailableItem(
                    id = "1111119",
                    name = "product 9",
                    imageUrl = "https://www.tokopedia.com/image",
                    isSelected = false,
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111112",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                )
            ),
            listUnavailableItem: List<GetShoppingListDataResponse.UnavailableItem> = listOf(
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111122",
                    name = "product 22",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111123",
                    name = "product 23",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111124",
                    name = "product 24",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111125",
                    name = "product 25",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111126",
                    name = "product 26",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                ),
                GetShoppingListDataResponse.UnavailableItem(
                    id = "1111127",
                    name = "product 27",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000.toDouble(),
                    labelGroup = listOf(
                        GetShoppingListDataResponse.LabelGroup(
                            title = "22 gr",
                            position = "weight"
                        )
                    ),
                    originalPrice = "Rp.20.000",
                    applink = "tokopedia://product/1111122",
                    minOrder = 2,
                    shop = GetShoppingListDataResponse.Shop(id = "3333333"),
                    warehouseID = "12312121",
                    discountPercentage = 50
                )
            )
        ) = GetShoppingListDataResponse.Data(
            listAvailableItem = listAvailableItem,
            listUnavailableItem = listUnavailableItem
        )

        fun createRecommendationWidget(
            recommendationItemList: List<RecommendationItem> = listOf(
                RecommendationItem(
                    productId = 1111111,
                    name = "product 1",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.25.000",
                    priceInt = 25000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "4 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.50.000",
                    appUrl = "tokopedia://product/1111111",
                    minOrder = 1,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                ),
                RecommendationItem(
                    productId = 1111112,
                    name = "product 2",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.20.000",
                    appUrl = "tokopedia://product/1111112",
                    minOrder = 2,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                ),
                RecommendationItem(
                    productId = 1111113,
                    name = "product 3",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.20.000",
                    priceInt = 30000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "1 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.40.000",
                    appUrl = "tokopedia://product/1111112",
                    minOrder = 3,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                ),
                RecommendationItem(
                    productId = 1111114,
                    name = "product 4",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.50.000",
                    priceInt = 50000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "1 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.100.000",
                    appUrl = "tokopedia://product/1111112",
                    minOrder = 2,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                )
            ),
            hasNext: Boolean,
            title: String
        ) = RecommendationWidget(
            hasNext = hasNext,
            title = title,
            recommendationItemList = recommendationItemList
        )
    }

    object AnotherOptionBottomSheet {
        fun createRecommendationWidget(
            recommendationItemList: List<RecommendationItem> = listOf(
                RecommendationItem(
                    productId = 1111111,
                    name = "product 1",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.25.000",
                    priceInt = 25000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "4 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.50.000",
                    appUrl = "tokopedia://product/1111111",
                    minOrder = 2,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                ),
                RecommendationItem(
                    productId = 1111112,
                    name = "product 2",
                    imageUrl = "https://www.tokopedia.com/image",
                    price = "Rp.10.000",
                    priceInt = 10000,
                    labelGroupList = listOf(
                        RecommendationLabel(
                            title = "2 gr",
                            position = "weight"
                        )
                    ),
                    slashedPrice = "Rp.20.000",
                    appUrl = "tokopedia://product/1111112",
                    minOrder = 2,
                    shopId = 3333333,
                    warehouseId = 12312121,
                    discountPercentage = "50%"
                )
            )
        ) = RecommendationWidget(
            recommendationItemList = recommendationItemList
        )

        fun createAvailableProducts() = listOf(
            ShoppingListHorizontalProductCardItemUiModel(
                id = "122231",
                name = "product 5",
                image = "https://www.tokopedia.com/image",
                price = "Rp.10.000",
                priceInt = 10000.toDouble(),
                weight = "4 gr",
                percentage = "50%",
                slashPrice = "Rp.50.000",
                isSelected = true,
                appLink = "tokopedia://product/122231",
                minOrder = 2,
                shopId = "122122",
                warehouseId = "111111",
                index = 0,
                state = TokoNowLayoutState.SHOW,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "1111112",
                name = "product 2",
                image = "https://www.tokopedia.com/image",
                price = "Rp.10.000",
                priceInt = 10000.toDouble(),
                weight = "2 gr",
                percentage = "50%",
                slashPrice = "Rp.20.000",
                isSelected = true,
                appLink = "tokopedia://product/1111112",
                minOrder = 2,
                shopId = "3333333",
                warehouseId = "12312121",
                index = 1,
                state = TokoNowLayoutState.SHOW,
                productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
            )
        )
    }
}
