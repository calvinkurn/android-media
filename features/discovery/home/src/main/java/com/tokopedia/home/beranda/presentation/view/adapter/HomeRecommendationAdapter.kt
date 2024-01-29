package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.diffutil.HomeRecommendationDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationPlayWidgetViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.widget.foryou.entity.RecomEntityCardViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.foryou.recom.HomeRecommendationUtil.isFullSpan
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.HeadlineTopAdsModel

class HomeRecommendationAdapter(
    private val adapterTypeFactory: ForYouRecommendationTypeFactoryImpl
) : ListAdapter<ForYouRecommendationVisitable, AbstractViewHolder<Visitable<*>>>(
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
            is ShimmeringStateModel,
            is ErrorStateModel,
            is EmptyStateModel,
            is LoadMoreStateModel -> layout.isFullSpan = true

            is BannerOldTopAdsModel ->
                layout.isFullSpan =
                    item.bannerType != TYPE_VERTICAL_BANNER_ADS

            is RetryButtonStateModel -> layout.isFullSpan = true

            is HeadlineTopAdsModel -> layout.isFullSpan = true
            is HomeRecommendationModel -> layout.isFullSpan = item.isFullSpan()
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

interface HomeRecommendationListener :
    RecomEntityCardViewHolder.Listener,
    HomeRecommendationPlayWidgetViewHolder.Listener {

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
