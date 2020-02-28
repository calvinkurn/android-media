package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory

class SettingTitleUiModel : Visitable<OtherSettingTypeFactory> {

    override fun type(typeFactory: OtherSettingTypeFactory): Int =
            typeFactory.type(this)
}