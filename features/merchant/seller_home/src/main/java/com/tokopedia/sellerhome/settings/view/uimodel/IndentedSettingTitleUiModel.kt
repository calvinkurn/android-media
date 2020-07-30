package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class IndentedSettingTitleUiModel(val settingTitle: String) : SettingUiModel {

    override val onClickApplink: String?
        get() = null
    override val settingUiType: SettingUiType
        get() = SettingUiType.INDENTED_SETTING_TITLE

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

}