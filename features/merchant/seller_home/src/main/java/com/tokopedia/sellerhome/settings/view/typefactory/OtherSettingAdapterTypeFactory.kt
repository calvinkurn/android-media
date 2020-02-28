package com.tokopedia.sellerhome.settings.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.uimodel.BalanceUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.BalanceViewHolder

class OtherSettingAdapterTypeFactory : BaseAdapterTypeFactory(), OtherSettingTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            BalanceViewHolder.LAYOUT -> BalanceViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(balanceUiModel: BalanceUiModel): Int {
        return BalanceViewHolder.LAYOUT
    }

    override fun type(dividerUiModel: DividerUiModel): Int {
        return BalanceViewHolder.LAYOUT
    }

    override fun type(settingTitleUiModel: SettingTitleUiModel): Int {
        return BalanceViewHolder.LAYOUT
    }

    override fun type(menuItemUiModel: MenuItemUiModel): Int {
        return BalanceViewHolder.LAYOUT
    }
}