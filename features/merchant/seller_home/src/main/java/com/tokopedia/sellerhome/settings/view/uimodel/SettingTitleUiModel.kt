package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class SettingTitleUiModel(val title: String) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE

    override val onClickApplink: String?
        get() = null
}