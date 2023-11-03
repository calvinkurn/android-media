package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationDiffUtil(
    private val typeFactory: HomeRecommendationTypeFactoryImpl
) : DiffUtil.ItemCallback<Visitable<HomeRecommendationTypeFactoryImpl>>() {

    override fun getChangePayload(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Any? {
        return Pair(oldItem, newItem)
    }

    override fun areItemsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
        return oldItem.type(typeFactory) == newItem.type(typeFactory)
    }

    override fun areContentsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
//        return if (oldItem is BannerRecommendationDataModel && newItem is BannerRecommendationDataModel) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationBannerTopAdsDataModel && newItem is HomeRecommendationBannerTopAdsDataModel) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationEmpty && newItem is HomeRecommendationEmpty) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationError && newItem is HomeRecommendationError) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationHeadlineTopAdsDataModel && newItem is HomeRecommendationHeadlineTopAdsDataModel) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationItemDataModel && newItem is HomeRecommendationItemDataModel) {
//            oldItem == newItem
//        } else if (oldItem is RecomEntryPointCardUiModel && newItem is RecomEntryPointCardUiModel) {
//            oldItem == newItem
//        }
//        else if (oldItem is HomeRecommendationLoading && newItem is HomeRecommendationLoading) {
//            oldItem == newItem
//        } else if (oldItem is HomeRecommendationLoadMore && newItem is HomeRecommendationLoadMore) {
//            oldItem == newItem
//        } else {
//            oldItem == newItem
//        }
        return oldItem.equals(newItem)
    }
}
