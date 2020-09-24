package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

object ProductSectionTitleUiModel: SectionTitleUiModel(
    R.string.seller_menu_product_section,
    R.string.seller_menu_product_cta,
    SectionTitleType.PRODUCT_SECTION_TITLE
) {

    override val onClickApplink: String?
        get() = ApplinkConst.PRODUCT_ADD
}