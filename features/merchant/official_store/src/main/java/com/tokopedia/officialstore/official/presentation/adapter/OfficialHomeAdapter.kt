package com.tokopedia.officialstore.official.presentation.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.ProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBenefitViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel

class OfficialHomeAdapter(adapterTypeFactory: OfficialHomeAdapterTypeFactory):
        BaseAdapter<OfficialHomeAdapterTypeFactory>(adapterTypeFactory) {

    /**
     * preparing space for banner, benefit, and featuredshop
     */
    fun resetState() {
        visitables.add(OfficialHomeMapper.BANNER_POSITION, OfficialBannerViewModel(arrayListOf()))
        visitables.add(OfficialHomeMapper.BENEFIT_POSITION, OfficialBenefitViewModel(arrayListOf()))
        visitables.add(OfficialHomeMapper.FEATURE_SHOP_POSITION, OfficialFeaturedShopViewModel(arrayListOf(), null))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in twoSpanLayout
        super.onBindViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int, payloads: MutableList<Any>) {
        val layoutParams = holder.itemView.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = getItemViewType(position) !in twoSpanLayout
        super.onBindViewHolder(holder, position, payloads)
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }

    var twoSpanLayout = listOf(
            ProductRecommendationViewHolder.LAYOUT
    )

    fun getDataByPosition(position: Int): Visitable<*> {
        return this.visitables[position]
    }
}