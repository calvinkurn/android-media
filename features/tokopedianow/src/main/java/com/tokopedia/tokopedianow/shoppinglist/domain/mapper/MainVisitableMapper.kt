package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowErrorUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.UNAVAILABLE_SHOPPING_LIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel

internal object MainVisitableMapper {

    /**
     * -- Page Level Section
     */
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

    fun MutableList<Visitable<*>>.addErrorState(throwable: Throwable) {
        add(
            TokoNowErrorUiModel(
                isFullPage = true,
                throwable = throwable
            )
        )
    }

    /**
     * -- Header Section
     */

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

    /**
     * -- Shopping List Section
     */

    fun MutableList<Visitable<*>>.addEmptyShoppingList(): MutableList<Visitable<*>> {
        add(ShoppingListEmptyUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addTopCheckAllShoppingList(
    ): MutableList<Visitable<*>> {
        add(ShoppingListTopCheckAllUiModel())
        return this
    }

    fun MutableList<Visitable<*>>.addAvailableShoppingList(
        listAvailableItem: List<GetShoppingListDataResponse.AvailableItem>
    ): MutableList<Visitable<*>> {
        val list = listAvailableItem.map {
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
        addAll(list)
        return this
    }

    fun MutableList<Visitable<*>>.addUnavailableShoppingList(
        listUnavailableItem: List<GetShoppingListDataResponse.UnavailableItem>
    ): MutableList<Visitable<*>> {
        val list = listUnavailableItem.map {
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
        addAll(list)
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

    fun MutableList<Visitable<*>>.removeLoadMore(): MutableList<Visitable<*>> {
        removeFirst { it is LoadingMoreModel }
        return this
    }

    fun MutableList<Visitable<*>>.removeRetry(): MutableList<Visitable<*>> {
        removeFirst { it is ShoppingListRetryUiModel }
        return this
    }
}
