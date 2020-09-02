package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

object OrderSectionTitleUiModel: SectionTitleUiModel(
    R.string.seller_menu_order_section,
    R.string.seller_menu_order_cta
) {

    override val onClickApplink: String?
        get() = ApplinkConst.MARKETPLACE_ORDER
}