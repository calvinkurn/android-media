package com.tokopedia.seller.menu.common.view.uimodel

import androidx.annotation.DimenRes
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

class SettingTitleUiModel(
    val title: String,
    @DimenRes val verticalSpacing: Int? = null
) : SettingUiModel {

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val onClickApplink: String?
        get() = null
}