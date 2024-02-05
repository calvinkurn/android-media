package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowDividerUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTitleUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.ATC_WISHLIST
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.EMPTY_STOCK
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel.LayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListTopCheckAllUiModel

object VisitableMapper {
    /**
     * -- Header Section --
     */

    fun MutableList<Visitable<*>>.addHeaderSpace(
        space: Int,
        headerModel: HeaderModel
    ) {
        add(
            TokoNowHeaderSpaceUiModel(
                space = space,
                backgroundColor = headerModel.backgroundGradientColor?.startColor
            )
        )
    }

    fun MutableList<Visitable<*>>.addHeader(
        headerModel: HeaderModel
    ) {
        add(
            TokoNowHeaderUiModel(
                pageTitle = headerModel.pageTitle,
                pageTitleColor = headerModel.pageTitleColor,
                ctaText = headerModel.ctaText,
                ctaTextColor = headerModel.ctaTextColor,
                ctaChevronIsShown = headerModel.ctaChevronIsShown,
                ctaChevronColor = headerModel.ctaChevronColor,
                backgroundGradientColor = headerModel.backgroundGradientColor
            )
        )
    }

    fun MutableList<Visitable<*>>.addWishlistProducts() {
        val list = listOf(
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                eta = "12 Desember",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000000000000000000000000000000000",
                type = ATC_WISHLIST
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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
                eta = "12 Desember",
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

    fun MutableList<Visitable<*>>.addRecommendationProducts() {
        val list = listOf(
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                eta = "12 Desember",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = PRODUCT_RECOMMENDATION
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                eta = "12 Desember",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000",
                type = PRODUCT_RECOMMENDATION
            ),
            ShoppingListHorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                eta = "12 Desember",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000000000000000000000000000000000",
                type = PRODUCT_RECOMMENDATION
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
