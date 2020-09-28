package com.tokopedia.seller.menu.common.view.uimodel

import androidx.annotation.DimenRes
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

class SettingTitleUiModel(
    val title: String,
    @DimenRes val verticalSpacing: Int? = null
) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SETTING_TITLE

    override val onClickApplink: String?
        get() = null
}