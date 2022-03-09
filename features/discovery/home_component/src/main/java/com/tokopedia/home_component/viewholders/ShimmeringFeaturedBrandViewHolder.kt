package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.visitable.ShimmeringFeaturedBrandDataModel

/**
 * @author by frenzelts on 08/03/22
 */
class ShimmeringFeaturedBrandViewHolder (itemView: View): AbstractViewHolder<ShimmeringFeaturedBrandDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shimmering_featured_brand_tablet
        @LayoutRes
        val LAYOUT_TABLET = R.layout.layout_shimmering_featured_brand_mobile
    }

    override fun bind(element: ShimmeringFeaturedBrandDataModel) {
    }

    override fun bind(element: ShimmeringFeaturedBrandDataModel, payloads: MutableList<Any>) {
    }
}