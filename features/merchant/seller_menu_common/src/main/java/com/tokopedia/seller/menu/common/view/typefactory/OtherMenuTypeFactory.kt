package com.tokopedia.seller.menu.common.view.typefactory

import com.tokopedia.seller.menu.common.view.uimodel.MenuItemUiModel
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel

interface OtherMenuTypeFactory {

    fun type(shopInfoUiModel: ShopInfoUiModel): Int
    fun type(shopInfoLoadingUiModel: ShopInfoLoadingUiModel): Int
    fun type(shopInfoErrorUiModel: ShopInfoErrorUiModel): Int
    fun type(shopOrderUiModel: ShopOrderUiModel): Int
    fun type(dividerUiModel: DividerUiModel): Int
    fun type(settingTitleUiModel: SettingTitleUiModel): Int
    fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int
    fun type(menuItemUiModel: MenuItemUiModel): Int
    fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int

}