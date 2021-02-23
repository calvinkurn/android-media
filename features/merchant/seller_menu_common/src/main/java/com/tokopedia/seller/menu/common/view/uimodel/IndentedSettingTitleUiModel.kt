package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

class IndentedSettingTitleUiModel(val settingTitle: String) : SettingUiModel {

    override val onClickApplink: String?
        get() = null

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

}