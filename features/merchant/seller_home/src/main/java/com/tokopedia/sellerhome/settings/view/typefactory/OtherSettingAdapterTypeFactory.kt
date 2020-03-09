package com.tokopedia.sellerhome.settings.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.uimodel.*
import com.tokopedia.sellerhome.settings.view.viewholder.*

class OtherSettingAdapterTypeFactory : BaseAdapterTypeFactory(), OtherSettingTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            BalanceViewHolder.LAYOUT -> BalanceViewHolder(parent)
            DividerViewHolder.LAYOUT -> DividerViewHolder(parent)
            SettingTitleViewHolder.LAYOUT -> SettingTitleViewHolder(parent)
            MenuItemsViewHolder.LAYOUT -> MenuItemsViewHolder(parent)
            ShopInfoViewHolder.LAYOUT -> ShopInfoViewHolder(parent)
            ShopStatusViewHolder.LAYOUT -> ShopStatusViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(balanceUiModel: BalanceUiModel): Int {
        return BalanceViewHolder.LAYOUT
    }

    override fun type(dividerUiModel: DividerUiModel): Int {
        return DividerViewHolder.LAYOUT
    }

    override fun type(settingTitleUiModel: SettingTitleUiModel): Int {
        return SettingTitleViewHolder.LAYOUT
    }

    override fun type(menuItemUiModel: MenuItemUiModel): Int {
        return MenuItemsViewHolder.LAYOUT
    }

    override fun type(shopInfoUiModel: ShopInfoUiModel): Int {
        return ShopInfoViewHolder.LAYOUT
    }

    override fun type(shopStatusUiModel: ShopStatusUiModel): Int {
        return ShopStatusViewHolder.LAYOUT
    }
}