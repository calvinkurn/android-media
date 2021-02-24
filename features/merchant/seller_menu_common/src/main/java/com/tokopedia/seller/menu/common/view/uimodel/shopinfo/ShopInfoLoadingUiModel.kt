package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

object ShopInfoLoadingUiModel: SettingUiModel {
    override fun type(typeFactory: OtherMenuTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val onClickApplink: String?
        get() = null

}