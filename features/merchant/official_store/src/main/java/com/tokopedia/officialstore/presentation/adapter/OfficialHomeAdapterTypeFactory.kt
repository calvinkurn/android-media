package com.tokopedia.officialstore.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.presentation.adapter.viewholder.*
import com.tokopedia.officialstore.presentation.adapter.viewmodel.*

class OfficialHomeAdapterTypeFactory : BaseAdapterTypeFactory(), OfficialHomeTypeFactory {

    override fun type(brandPopulerViewModel: BrandPopulerViewModel): Int {
        return BrandPopulerViewHolder.LAYOUT
    }

    override fun type(categoryViewModel: CategoryViewModel): Int {
        return CategoryViewHolder.LAYOUT
    }

    override fun type(exclusiveBrandViewModel: ExclusiveBrandViewModel): Int {
        return ExclusiveBrandViewHolder.LAYOUT
    }

    override fun type(officialBannerViewModel: OfficialBannerViewModel): Int {
        return OfficialBannerViewHolder.LAYOUT
    }

    override fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if (type == BrandPopulerViewHolder.LAYOUT) {
            return BrandPopulerViewHolder(parent)
        } else if (type == CategoryViewHolder.LAYOUT) {
            return CategoryViewHolder(parent)
        } else if (type == ExclusiveBrandViewHolder.LAYOUT) {
            return ExclusiveBrandViewHolder(parent)
        } else if (type == OfficialBannerViewHolder.LAYOUT) {
            return OfficialBannerViewHolder(parent)
        } else if (type == ProductRecommendationViewHolder.LAYOUT) {
            return ProductRecommendationViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}