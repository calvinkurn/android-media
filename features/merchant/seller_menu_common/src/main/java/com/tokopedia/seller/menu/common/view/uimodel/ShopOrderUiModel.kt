package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

class ShopOrderUiModel(val newOrderCount: Int = 0, val readyToShip: Int = 0): SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE

    override val onClickApplink: String?
        get() = null
}