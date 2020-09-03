package com.tokopedia.seller.menu.common.view.uimodel

import androidx.annotation.StringRes
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiType

open class SectionTitleUiModel(
    @StringRes val title: Int,
    @StringRes val ctaText: Int
): SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
        typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.SHOP_ORDER

    override val onClickApplink: String?
        get() = null
}