package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

class ProductSectionTitleUiModel(
        title: String? = null,
        ctaText: String? = null,
        private val isShopOwner: Boolean)
    : SectionTitleUiModel(
        title,
        ctaText,
        SectionTitleType.PRODUCT_SECTION_TITLE
) {

    override val onClickApplink: String?
        get() =
            if (isShopOwner) {
                ApplinkConst.PRODUCT_ADD
            } else {
                UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, AdminFeature.ADD_PRODUCT)
            }
}