package com.tokopedia.sellerhome.settings.view.typefactory

import com.tokopedia.sellerhome.settings.view.uimodel.*

interface OtherSettingTypeFactory {

    fun type(balanceUiModel: BalanceUiModel): Int
    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int
    fun type(shopInfoUiModel: ShopInfoUiModel): Int
    fun type(shopStatusUiModel: ShopStatusUiModel): Int

}