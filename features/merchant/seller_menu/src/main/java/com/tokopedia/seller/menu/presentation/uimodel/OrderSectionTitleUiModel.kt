package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

object OrderSectionTitleUiModel: SectionTitleUiModel(
    R.string.seller_menu_order_section,
    R.string.seller_menu_order_cta,
    SectionTitleType.ORDER_SECTION_TITLE
) {

    override val onClickApplink: String?
        get() = UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.ORDER_HISTORY)
}