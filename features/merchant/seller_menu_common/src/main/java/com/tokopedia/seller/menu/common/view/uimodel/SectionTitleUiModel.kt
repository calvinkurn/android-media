package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

open class SectionTitleUiModel(
    val title: String? = null,
    val ctaText: String? = null,
    val type: SectionTitleType
): SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

    override val onClickApplink: String?
        get() = null

    enum class SectionTitleType {
        ORDER_SECTION_TITLE,
        PRODUCT_SECTION_TITLE,
        OTHER_SECTION_TITLE
    }
}