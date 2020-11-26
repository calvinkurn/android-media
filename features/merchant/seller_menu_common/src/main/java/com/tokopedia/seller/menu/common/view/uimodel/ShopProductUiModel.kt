package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

class ShopProductUiModel(val count: Int = 0): SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SHOP_PRODUCT

    override val onClickApplink: String?
        get() = null
}