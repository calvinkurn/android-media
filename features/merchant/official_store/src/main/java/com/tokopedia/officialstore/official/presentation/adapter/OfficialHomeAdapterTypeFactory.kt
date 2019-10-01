package com.tokopedia.officialstore.official.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialBannerViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.ProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel

class OfficialHomeAdapterTypeFactory : BaseAdapterTypeFactory(), OfficialHomeTypeFactory {

    override fun type(officialBannerViewModel: OfficialBannerViewModel): Int {
        return OfficialBannerViewHolder.LAYOUT
    }

    override fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int {
        return ProductRecommendationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        if (type == OfficialBannerViewHolder.LAYOUT) {
            return OfficialBannerViewHolder(parent)
        } else if (type == ProductRecommendationViewHolder.LAYOUT) {
            return ProductRecommendationViewHolder(parent)
        }
        return super.createViewHolder(parent, type)
    }
}