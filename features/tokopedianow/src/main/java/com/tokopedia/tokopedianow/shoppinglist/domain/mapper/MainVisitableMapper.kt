package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.LOADING
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.ATC_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.EMPTY_STOCK
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel

object MainVisitableMapper {

    /**
     * -- Header Section --
     */

    fun MutableList<Visitable<*>>.addShimmeringPage(
    ): MutableList<Visitable<*>> {
        val newList = arrayListOf(
            TokoNowThematicHeaderUiModel(
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = ATC_WISHLIST,
                state = LOADING
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                type = ATC_WISHLIST,
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

    fun MutableList<Visitable<*>>.addLoadMore() {
        add(LoadingMoreModel())
    }

    fun MutableList<Visitable<*>>.removeLoadMore() {
        removeFirst { it is LoadingMoreModel }
    }

    fun MutableList<Visitable<*>>.addRetry() {
        add(ShoppingListRetryUiModel())
    }

    fun MutableList<Visitable<*>>.removeRetry() {
        removeFirst { it is ShoppingListRetryUiModel }
    }

    fun MutableList<Visitable<*>>.addWishlistProducts() {
        val list = listOf(
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy Baby Pak ChoyBaby Pak ChoyBaby Pak ChoyBaby Pak ChoyBaby Pak ChoyBaby Pak ChoyBaby Pak ChoyBaby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000000000000000000000000000000000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = ATC_WISHLIST
            )
        )
        add(
            ShoppingListTopCheckAllUiModel(
                id = "2222",
                allPrice = "Semua (Rp.12.000)",
                selectedProductCounter = "1 produk terpilih"
            )
        )
        addAll(list)
    }

    fun MutableList<Visitable<*>>.addDivider() {
        add(TokoNowDividerUiModel())
    }

    fun MutableList<Visitable<*>>.addTitle(
        title: String
    ) {
        add(
            TokoNowTitleUiModel(
                title = title
            )
        )
    }

    fun MutableList<Visitable<*>>.addEmptyStockProducts() {
        val list = listOf(
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = EMPTY_STOCK
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = EMPTY_STOCK
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000000000000000000000000000000000",
                type = EMPTY_STOCK
            )
        )
        addAll(list)
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
}
