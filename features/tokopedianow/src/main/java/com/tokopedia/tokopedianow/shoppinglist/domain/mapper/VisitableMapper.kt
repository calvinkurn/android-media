package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.HorizontalProductCardItemUiModel

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
            HorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000"
            ),
            HorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000"
            ),
            HorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000"
            ),
            HorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000"
            ),
            HorizontalProductCardItemUiModel(
                id = "123121",
                image = "https://images.tokopedia.net/img/cache/1200/ynqObV/2024/1/23/3b40956f-18a9-4881-b462-54a28bbf75f9.jpg.webp",
                price = "Rp5.000",
                name = "Baby Pak Choy",
                weight = "350gr",
                percentage = "50%",
                slashPrice = "Rp100.000"
            )
        )
        addAll(list)
    }
}
