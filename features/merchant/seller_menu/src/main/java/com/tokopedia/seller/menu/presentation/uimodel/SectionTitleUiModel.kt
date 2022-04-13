package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

open class SectionTitleUiModel(
    val title: String? = null,
    val ctaText: String? = null,
    val type: SectionTitleType
): Visitable<SellerMenuTypeFactory> {

    open val onClickApplink: String? = null

    override fun type(typeFactory: SellerMenuTypeFactory): Int =
        typeFactory.type(this)

    enum class SectionTitleType {
        ORDER_SECTION_TITLE,
        PRODUCT_SECTION_TITLE,
        OTHER_SECTION_TITLE
    }
}