package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class SettingTitleMenuUiModel(val settingTitle: String,
                              val settingDrawable: Int?) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val onClickApplink: String?
        get() = null

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE_MENU
}