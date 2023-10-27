package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.isFullSpan
import com.tokopedia.home.beranda.presentation.view.adapter.diffutil.HomeRecommendationDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.viewholder.RecomEntryPointCardViewHolder
import com.tokopedia.smart_recycler_helper.*

class HomeRecommendationAdapter(
    smartExecutors: SmartExecutors,
    private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl
) : ListAdapter<HomeRecommendationVisitable, AbstractViewHolder<Visitable<*>>>(
    AsyncDifferConfig.Builder(HomeRecommendationDiffUtil).build()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
        return adapterTypeFactory.createViewHolder(parent, viewType) as AbstractViewHolder<Visitable<*>>
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

            is HomeRecommendationBannerTopAdsDataModel -> layout.isFullSpan =
                item.bannerType != TYPE_VERTICAL_BANNER_ADS

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
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.bind(getItem(position), payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}

interface HomeRecommendationListener: RecomEntryPointCardViewHolder.Listener {
    fun onProductImpression(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    )

    fun onProductClick(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    )

    fun onProductThreeDotsClick(
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel,
        position: Int
    )

    fun onBannerImpression(bannerRecommendationDataModel: BannerRecommendationDataModel)
    fun onBannerTopAdsClick(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        position: Int
    )

    fun onBannerTopAdsImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        position: Int
    )

    fun onRetryGetProductRecommendationData()
}
