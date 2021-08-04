package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

class OrderSectionTitleUiModel(
        title: String? = null,
        ctaText: String? = null,
        private val isShopOwner: Boolean)
    : SectionTitleUiModel(
        title,
        ctaText,
        SectionTitleType.ORDER_SECTION_TITLE
) {

    override val onClickApplink: String?
        get() =
            if (isShopOwner) {
                ApplinkConst.SELLER_HISTORY
            } else {
                UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.ORDER_HISTORY)
            }
}