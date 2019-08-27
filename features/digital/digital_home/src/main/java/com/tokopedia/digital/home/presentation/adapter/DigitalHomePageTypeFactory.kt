package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePagePromoModel

class DigitalHomePageTypeFactory : BaseAdapterTypeFactory() {
    fun type(digitalHomePageBannerModel: DigitalHomePageBannerModel) : Int{
        return DigitalHomePageBannerViewHolder.LAYOUT
    }

    fun type(digitalHomePageCategoryModel: DigitalHomePageCategoryModel): Int {
        return DigitalHomePageCategoryViewHolder.LAYOUT
    }

    fun type(digitalHomePagePromoModel: DigitalHomePagePromoModel): Int {
        return DigitalHomePagePromoViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageBannerViewHolder.LAYOUT -> return DigitalHomePageBannerViewHolder(parent)
            DigitalHomePageCategoryViewHolder.LAYOUT -> return DigitalHomePageCategoryViewHolder(parent)
            DigitalHomePagePromoViewHolder.LAYOUT -> return DigitalHomePagePromoViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }

}
