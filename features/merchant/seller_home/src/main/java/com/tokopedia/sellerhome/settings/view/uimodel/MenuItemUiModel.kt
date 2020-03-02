package com.tokopedia.sellerhome.settings.view.uimodel

import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiType

class MenuItemUiModel(private val menuItemList: List<MenuItem>) : SettingUiModel {

    init {
        val listItemUnifyList = mapMenuItemList(menuItemList)
    }

    override fun type(typeFactory: OtherSettingTypeFactory): Int =
            typeFactory.type(this)

    override val settingUiType: SettingUiType
        get() = SettingUiType.MENU_ITEM

    override val onClickApplink: String?
        get() = null
}