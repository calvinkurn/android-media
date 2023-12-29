package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationPlayWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.RecomEntityCardViewHolder

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
                    item.bannerType != TYPE_VERTICAL_BANNER_ADS

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

interface HomeRecommendationListener : RecomEntityCardViewHolder.Listener, HomeRecommendationPlayWidgetViewHolder.Listener {
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
    fun onBannerTopAdsOldClick(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        position: Int
    )

    fun onBannerTopAdsOldImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        position: Int
    )

    fun onBannerTopAdsClick(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsUiModel,
        position: Int
    )

    fun onBannerTopAdsImpress(
        homeTopAdsRecommendationBannerDataModelDataModel: HomeRecommendationBannerTopAdsUiModel,
        position: Int
    )

    fun onRetryGetProductRecommendationData()

    fun onRetryGetNextProductRecommendationData()
}
