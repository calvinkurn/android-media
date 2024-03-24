package com.tokopedia.tokopedianow.data

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType

object ShoppingListDataFactory {
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

        fun createProductRecommendation(): ShoppingListHorizontalProductCardItemUiModel {
            return ShoppingListHorizontalProductCardItemUiModel(
                id = "1111111",
                name = "product 1",
                image = "https://www.tokopedia.com/image",
                price = "Rp.25.000",
                priceInt = 25000.toDouble(),
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
                productLayoutType = ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION
            )
        }
    }
}
