package com.tokopedia.seller.menu.common.view.typefactory

import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.*

interface OtherMenuTypeFactory {

    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int
    fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int

}