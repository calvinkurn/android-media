package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.Type.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.Type.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.Type.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel

internal object MainVisitableMapper {

    const val MAX_ITEM_DISPLAYED = 10
    const val INVALID_INDEX = -1

    fun mapAvailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.AvailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listAvailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            type = AVAILABLE_SHOPPING_LIST
        )
    }

    fun mapUnavailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.UnavailableItem>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = listAvailableItem.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.id,
            image = it.imageUrl,
            price = it.price,
            name = it.name,
            weight = it.getWeight(),
            percentage = it.discountPercentage.toString(),
            slashPrice = it.originalPrice,
            type = UNAVAILABLE_SHOPPING_LIST
        )
    }

    fun MutableList<Visitable<*>>.addShoppingListProducts(
        products: List<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<Visitable<*>> {
        addAll(products)
        return this
    }

    fun MutableList<Visitable<*>>.addLoadingState(): MutableList<Visitable<*>> {
        val newList = arrayListOf(
            TokoNowThematicHeaderUiModel(
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = AVAILABLE_SHOPPING_LIST,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = AVAILABLE_SHOPPING_LIST,
                state = LOADING
            ),
            TokoNowDividerUiModel(),
            TokoNowTitleUiModel(
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = PRODUCT_RECOMMENDATION,
                state = LOADING
            )
        )
        addAll(newList)
        return this
    }

    fun MutableList<Visitable<*>>.addHeader(
        headerModel: HeaderModel,
        @TokoNowLayoutState state: Int
    ) {
        add(
            TokoNowThematicHeaderUiModel(
                pageTitle = headerModel.pageTitle,
                pageTitleColor = headerModel.pageTitleColor,
                ctaText = headerModel.ctaText,
                ctaTextColor = headerModel.ctaTextColor,
                ctaChevronIsShown = headerModel.ctaChevronIsShown,
                ctaChevronColor = headerModel.ctaChevronColor,
                backgroundGradientColor = headerModel.backgroundGradientColor,
                state = state
            )
        )
    }

    fun MutableList<Visitable<*>>.addEmptyShoppingList(): MutableList<Visitable<*>> {
        add(ShoppingListEmptyUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addTopCheckAllShoppingList(): MutableList<Visitable<*>> {
        add(ShoppingListTopCheckAllUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addProductInCartWidget() {
        add(
            ShoppingListProductInCartUiModel(
                productList = listOf(
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    ),
                    ShoppingListProductInCartItemUiModel(
                        imageUrl = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp"
                    )
                )
            )
        )
    }

    fun MutableList<Visitable<*>>.addExpandCollapse(
        state: ShoppingListExpandCollapseUiModel.State,
        productLayoutType: ShoppingListHorizontalProductCardItemUiModel.Type,
        remainingTotalProduct: Int
    ): MutableList<Visitable<*>> {
        add(
            ShoppingListExpandCollapseUiModel(
                state = state,
                remainingTotalProduct = remainingTotalProduct,
                productLayoutType = productLayoutType
            )
        )
        return this
    }

    fun MutableList<Visitable<*>>.addTitle(
        title: String
    ): MutableList<Visitable<*>> {
        add(TokoNowTitleUiModel(title))
        return this
    }

    fun MutableList<Visitable<*>>.addLoadMore(): MutableList<Visitable<*>> {
        add(LoadingMoreModel())
        return this
    }

    fun MutableList<Visitable<*>>.addRetry(): MutableList<Visitable<*>> {
        add(ShoppingListRetryUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addDivider(): MutableList<Visitable<*>> {
        add(TokoNowDividerUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.removeLoadMore(): MutableList<Visitable<*>> {
        removeFirst { it is LoadingMoreModel }
        return this
    }

    fun MutableList<Visitable<*>>.removeRetry(): MutableList<Visitable<*>> {
        removeFirst { it is ShoppingListRetryUiModel }
        return this
    }

    fun MutableList<Visitable<*>>.modifyExpandCollapseProducts(
        state: ShoppingListExpandCollapseUiModel.State,
        productLayoutType: ShoppingListHorizontalProductCardItemUiModel.Type,
        availableItems: List<ShoppingListHorizontalProductCardItemUiModel>,
        unavailableItems: List<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<Visitable<*>> {
        val firstProductIndex = indexOfFirst { it is ShoppingListHorizontalProductCardItemUiModel && it.type == productLayoutType }
        val lastProductIndex = indexOfLast { it is ShoppingListHorizontalProductCardItemUiModel && it.type == productLayoutType }

        if (firstProductIndex != INVALID_INDEX && lastProductIndex != INVALID_INDEX) {
            subList(
                firstProductIndex,
                lastProductIndex.inc()
            ).clear()

            val items = if (productLayoutType == AVAILABLE_SHOPPING_LIST) {
                if (state == ShoppingListExpandCollapseUiModel.State.EXPAND) availableItems else availableItems.take(MAX_ITEM_DISPLAYED)
            } else {
                if (state == ShoppingListExpandCollapseUiModel.State.EXPAND) unavailableItems else unavailableItems.take(MAX_ITEM_DISPLAYED)
            }

            addAll(firstProductIndex, items)
        }
        return this
    }

    fun MutableList<Visitable<*>>.modifyExpandCollapseState(
        state: ShoppingListExpandCollapseUiModel.State,
        productLayoutType: ShoppingListHorizontalProductCardItemUiModel.Type
    ): MutableList<Visitable<*>> {
        val index = indexOfFirst { it is ShoppingListExpandCollapseUiModel && it.productLayoutType == productLayoutType }

        if (index != INVALID_INDEX) {
            val item = this[index] as ShoppingListExpandCollapseUiModel
            removeAt(index)
            add(index, item.copy(state = state))
        }
        return this
    }

    fun MutableList<Visitable<*>>.addIf(
        isTrue: Boolean,
        layout: () -> MutableList<Visitable<*>>
    ): MutableList<Visitable<*>> {
        return if (isTrue) {
            layout.invoke()
        } else {
            this
        }
    }
}
