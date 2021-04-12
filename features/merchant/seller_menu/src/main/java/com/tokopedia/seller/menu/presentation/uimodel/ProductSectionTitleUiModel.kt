package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.SectionTitleUiModel

class ProductSectionTitleUiModel(private val isShopOwner: Boolean): SectionTitleUiModel(
        R.string.seller_menu_product_section,
        R.string.seller_menu_product_cta,
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