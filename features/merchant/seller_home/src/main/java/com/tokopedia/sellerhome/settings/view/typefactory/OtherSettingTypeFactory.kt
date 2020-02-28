package com.tokopedia.sellerhome.settings.view.typefactory

import com.tokopedia.sellerhome.settings.view.uimodel.BalanceUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel

interface OtherSettingTypeFactory {

    fun type(balanceUiModel: BalanceUiModel): Int
    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int

}