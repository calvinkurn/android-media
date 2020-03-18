package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class MenuItemUiModel(val title: String = "",
                      val drawableReference: Int? = null,
                      private val clickApplink: String? = null,
                      val clickAction: () -> Unit = {}) : SettingUiModel {

    val isNoIcon: Boolean
        get() = drawableReference == null

    override fun type(typeFactory: OtherMenuTypeFactory): Int =
            typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.MENU_ITEM

    override val onClickApplink: String?
        get() = clickApplink
}