package com.tokopedia.tokomart.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeSectionUiModel
import com.tokopedia.tokomart.home.presentation.viewholder.HomeCategoryItemViewHolder
import com.tokopedia.tokomart.home.presentation.viewholder.HomeCategoryGridViewHolder
import com.tokopedia.tokomart.home.presentation.viewholder.HomeSectionViewHolder

class TokoMartHomeAdapterTypeFactory: BaseAdapterTypeFactory(), TokoMartHomeTypeFactory {

    override fun type(uiModel: HomeSectionUiModel): Int = HomeSectionViewHolder.LAYOUT
    override fun type(uiModel: HomeCategoryGridUiModel): Int = HomeCategoryGridViewHolder.LAYOUT
    override fun type(uiModel: HomeCategoryItemUiModel): Int = HomeCategoryItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            HomeSectionViewHolder.LAYOUT -> HomeSectionViewHolder(parent)
            HomeCategoryGridViewHolder.LAYOUT -> HomeCategoryGridViewHolder(parent)
            HomeCategoryItemViewHolder.LAYOUT -> HomeCategoryItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}