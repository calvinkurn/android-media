package com.tokopedia.sellerhome.settings.view.uimodel.base

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory

interface SettingUiModel: Visitable<OtherMenuTypeFactory> {
    val onClickApplink: String?
    val settingUiType: SettingUiType
}