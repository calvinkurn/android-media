package com.tokopedia.seller.menu.common.view.typefactory

import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SellerSettingsTitleUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.seller.menu.common.view.uimodel.SettingTitleUiModel

interface OtherMenuTypeFactory {
    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(sellerSettingsTitleUiModel: SellerSettingsTitleUiModel): Int
    fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int
    fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int
    fun type(settingLoadingUiModel: SettingLoadingUiModel): Int
}