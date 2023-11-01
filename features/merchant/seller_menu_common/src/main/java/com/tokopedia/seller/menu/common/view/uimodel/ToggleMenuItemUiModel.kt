package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel

class ToggleMenuItemUiModel(
    val title: String = "",
    val tag: String = "",
    var isChecked: Boolean = false,
    val onCheckedChanged: ((isChecked: Boolean) -> Unit)? = null
) : SettingUiModel {

    override val onClickApplink: String
        get() = String.EMPTY

    override fun type(typeFactory: OtherMenuTypeFactory): Int = typeFactory.type(this)
}
