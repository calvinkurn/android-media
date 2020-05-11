package com.tokopedia.sellerhome.settings.view.typefactory

import com.tokopedia.sellerhome.settings.view.uimodel.*

interface OtherMenuTypeFactory {

    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int
    fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int

}