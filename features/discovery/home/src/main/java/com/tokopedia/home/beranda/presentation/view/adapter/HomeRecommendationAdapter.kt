package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BaseHomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationButtonRetryUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationEmpty
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationError
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoadMore
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.isFullSpan
import com.tokopedia.home.beranda.presentation.view.adapter.diffutil.HomeRecommendationDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationAdapter(
    private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl
) : ListAdapter<BaseHomeRecommendationVisitable, AbstractViewHolder<Visitable<*>>>(
    AsyncDifferConfig.Builder(HomeRecommendationDiffUtil())
        .build()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int
    ) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (val item = getItem(position)) {
            is HomeRecommendationLoading,
            is HomeRecommendationError,
            is HomeRecommendationEmpty,
            is HomeRecommendationLoadMore -> layout.isFullSpan = true

            is HomeRecommendationBannerTopAdsOldDataModel ->
                layout.isFullSpan =
                    item.bannerType != HomeRecommendationMapper.TYPE_VERTICAL_BANNER_ADS

            is HomeRecommendationButtonRetryUiModel -> layout.isFullSpan = true

            is HomeRecommendationHeadlineTopAdsDataModel -> layout.isFullSpan = true
            is HomeRecommendationItemDataModel -> layout.isFullSpan = item.isFullSpan()
        }
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(holder.bindingAdapterPosition), payloads)
        } else {
            super.onBindViewHolder(holder, holder.bindingAdapterPosition, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}

