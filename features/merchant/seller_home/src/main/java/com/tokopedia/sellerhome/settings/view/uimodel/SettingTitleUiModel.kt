package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class SettingTitleUiModel(val title: String) : SettingUiModel {

    override fun type(typeFactory: OtherSettingTypeFactory): Int =
            typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE

    override val onClickApplink: String?
        get() = null
}