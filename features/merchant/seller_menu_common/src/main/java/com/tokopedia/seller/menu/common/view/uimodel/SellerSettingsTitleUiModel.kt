package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

class SellerSettingsTitleUiModel(val settingTitle: String,
                                 val settingDrawable: Int?) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE_MENU
}