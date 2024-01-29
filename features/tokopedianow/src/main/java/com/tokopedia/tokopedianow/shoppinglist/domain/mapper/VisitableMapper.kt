package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel

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
}
