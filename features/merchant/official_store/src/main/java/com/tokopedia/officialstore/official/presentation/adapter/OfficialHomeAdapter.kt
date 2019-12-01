package com.tokopedia.officialstore.official.presentation.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBenefitViewModel
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialFeaturedShopViewModel

class OfficialHomeAdapter(adapterTypeFactory: OfficialHomeAdapterTypeFactory):
        BaseAdapter<OfficialHomeAdapterTypeFactory>(adapterTypeFactory) {

    /**
     * preparing space for banner, benefit, and featuredshop
     */
    fun resetState() {
        visitables.add(OfficialHomeMapper.BANNER_POSITION, loadingModel)
        visitables.add(OfficialHomeMapper.BANNER_POSITION, OfficialBannerViewModel(mutableListOf(), ""))
        visitables.add(OfficialHomeMapper.BENEFIT_POSITION, OfficialBenefitViewModel(arrayListOf()))
        visitables.add(OfficialHomeMapper.FEATURE_SHOP_POSITION, OfficialFeaturedShopViewModel(arrayListOf(), null, ""))
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

//    fun showLoadingBanner() {
//        visitables.add(OfficialHomeMapper.BANNER_POSITION, loadingModel)
//    }

    fun removeLoading() {
        visitables.remove(loadingModel)
    }

    fun getVisitables(): MutableList<Visitable<*>> {
        return visitables
    }

    var twoSpanLayout = listOf(
            OfficialProductRecommendationViewHolder.LAYOUT
    )
}