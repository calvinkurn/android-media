package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel

class BalanceUiModel: SettingUiModel {

    override fun type(typeFactory: OtherSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val onClickAction: () -> Unit
        get() = {}

}