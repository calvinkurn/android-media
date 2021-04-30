package com.tokopedia.tokomart.home.presentation.adapter

import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.home.presentation.uimodel.*
import com.tokopedia.tokomart.home.presentation.viewholder.*

class TokoMartHomeAdapterTypeFactory(private val fragment: Fragment): BaseAdapterTypeFactory(), TokoMartHomeTypeFactory {

    override fun type(uiModel: HomeSectionUiModel): Int = HomeSectionViewHolder.LAYOUT
    override fun type(uiModel: HomeAllCategoryUiModel): Int = HomeAllCategoryViewHolder.LAYOUT
    override fun type(uiModel: HomeSliderBannerUiModel): Int = HomeSliderBannerViewHolder.LAYOUT
    override fun type(uiModel: HomeSliderProductUiModel): Int = HomeSliderProductViewHolder.LAYOUT
    override fun type(uiModel: HomeChooseAddressWidgetUiModel): Int = HomeChooseAddressWidgetViewHolder.LAYOUT
//    override fun type(uiModel: HomeSpecialRecipeUiModel): Int = HomeSpecialRecipeViewHolder.LAYOUT NOT MVP

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            HomeSectionViewHolder.LAYOUT -> HomeSectionViewHolder(parent)
            HomeAllCategoryViewHolder.LAYOUT -> HomeAllCategoryViewHolder(parent)
            HomeSliderBannerViewHolder.LAYOUT -> HomeSliderBannerViewHolder(parent)
            HomeSliderProductViewHolder.LAYOUT -> HomeSliderProductViewHolder(parent)
            HomeChooseAddressWidgetViewHolder.LAYOUT -> HomeChooseAddressWidgetViewHolder(parent, fragment)
//            HomeSpecialRecipeViewHolder.LAYOUT -> HomeSpecialRecipeViewHolder(parent) NOT MVP
            else -> super.createViewHolder(parent, type)
        }
    }
}