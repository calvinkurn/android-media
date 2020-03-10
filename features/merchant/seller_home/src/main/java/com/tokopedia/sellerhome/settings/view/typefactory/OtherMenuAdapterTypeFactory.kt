package com.tokopedia.sellerhome.settings.view.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.settings.view.uimodel.*
import com.tokopedia.sellerhome.settings.view.viewholder.*

class OtherMenuAdapterTypeFactory : BaseAdapterTypeFactory(), OtherMenuTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            DividerViewHolder.LAYOUT -> DividerViewHolder(parent)
            SettingTitleViewHolder.LAYOUT -> SettingTitleViewHolder(parent)
            IndentedSettingTitleViewHolder.LAYOUT -> IndentedSettingTitleViewHolder(parent)
            MenuItemsViewHolder.LAYOUT -> MenuItemsViewHolder(parent)
            SettingTitleMenuViewHolder.LAYOUT -> SettingTitleMenuViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
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

    override fun type(settingTitleMenuUiModel: SettingTitleMenuUiModel): Int {
        return SettingTitleMenuViewHolder.LAYOUT
    }

    override fun type(indentedSettingTitleUiModel: IndentedSettingTitleUiModel): Int {
        return IndentedSettingTitleViewHolder.LAYOUT
    }
}