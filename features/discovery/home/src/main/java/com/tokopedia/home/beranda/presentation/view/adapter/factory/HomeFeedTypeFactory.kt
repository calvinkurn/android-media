//package com.tokopedia.home.beranda.presentation.view.adapter.factory
//
//import android.view.View
//import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
//import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
//import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
//import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
//import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
//import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
//import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
//import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeBannerFeedViewHolder
//import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeFeedLoadingMoreViewHolder
//import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeFeedViewHolder
//
///**
// * Created by Lukas on 2019-07-16
// */
//class HomeFeedTypeFactory(private val view: HomeFeedContract.View) : BaseAdapterTypeFactory() {
//
//    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
//        return when (type) {
//            HomeFeedViewHolder.LAYOUT -> HomeFeedViewHolder(parent, view)
//            LoadingShimmeringGridViewHolder.LAYOUT -> LoadingShimmeringGridViewHolder(parent)
//            HomeFeedLoadingMoreViewHolder.LAYOUT -> HomeFeedLoadingMoreViewHolder(parent)
//            HomeBannerFeedViewHolder.LAYOUT -> HomeBannerFeedViewHolder(parent, view)
//            else -> super.createViewHolder(parent, type)
//        }
//    }
//
//    fun type(viewModel: HomeRecommendationItemDataModel): Int {
//        return HomeFeedViewHolder.LAYOUT
//    }
//
//    fun type(viewModel: BannerRecommendationDataModel): Int {
//        return HomeBannerFeedViewHolder.LAYOUT
//    }
//
//
//    override fun type(viewModel: LoadingModel): Int {
//        return LoadingShimmeringGridViewHolder.LAYOUT
//    }
//
//    override fun type(viewModel: LoadingMoreModel): Int {
//        return HomeFeedLoadingMoreViewHolder.LAYOUT
//    }
//}