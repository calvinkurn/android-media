package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home.beranda.domain.gql.feed.Banner
import com.tokopedia.home.beranda.domain.gql.feed.GetHomeRecommendationContent
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.LAYOUT_NAME_LIST
import java.util.*

class HomeRecommendationMapper {
    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetHomeRecommendationContent,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val recommendationProduct = graphqlResponse.recommendationProduct
        val visitables = mutableListOf<HomeRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationItemDataModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(convertToHomeProductFeedModel(recommendationProduct.product, recommendationProduct.pageName, recommendationProduct.layoutName, tabName, pageNumber))

        val bannerStack = Stack<BannerRecommendationDataModel>()
        // ignore banner when getting list type
        if(recommendationProduct.layoutName != LAYOUT_NAME_LIST) {
            // reverse stack because to get the first in
            Collections.reverse(productStack)
            bannerStack.addAll(convertToHomeBannerFeedModel(recommendationProduct.banners, tabName, pageNumber))
        }

        recommendationProduct.layoutTypes.forEachIndexed { index, layoutType ->
            when (layoutType.type) {
                TYPE_PRODUCT -> {
                    visitables.add(productStack.pop())
                }
                TYPE_BANNER -> {
                    // ignore banner when getting list type
                    if(recommendationProduct.layoutName != LAYOUT_NAME_LIST) {
                        visitables.add(bannerStack.pop())
                    }
                }
                TYPE_BANNER_ADS -> {
                    visitables.add(HomeRecommendationBannerTopAdsDataModel(position = index, bannerType = TYPE_BANNER_ADS))
                }
                TYPE_VERTICAL_BANNER_ADS -> {
                    visitables.add(HomeRecommendationBannerTopAdsDataModel(position = index, bannerType = TYPE_VERTICAL_BANNER_ADS))
                }


            }
        }
        return HomeRecommendationDataModel(visitables, recommendationProduct.hasNextPage)
    }

    private fun convertToHomeBannerFeedModel(banners: List<Banner>, tabName: String, pageNumber: Int): List<BannerRecommendationDataModel> {
        val bannerFeedViewModels = ArrayList<BannerRecommendationDataModel>()
        for (position in banners.indices) {
            val banner = banners[position]

            bannerFeedViewModels.add(
                BannerRecommendationDataModel(
                    banner.id,
                    banner.name,
                    banner.imageUrl,
                    banner.url,
                    banner.applink,
                    banner.buAttribution,
                    banner.creativeName,
                    banner.target,
                    (((pageNumber - 1) * banners.size) + position + 1).toInt(),
                    banner.galaxyAttribution,
                    banner.persona,
                    banner.brandId,
                    banner.categoryPersona,
                    tabName
                )
            )
        }
        return bannerFeedViewModels
    }

    private fun convertToHomeProductFeedModel(products: List<Product>, pageName: String, layoutName: String, tabName: String, pageNumber: Int): List<HomeRecommendationItemDataModel> {
        val homeFeedViewModels = ArrayList<HomeRecommendationItemDataModel>()
        for (position in products.indices) {
            val product = products[position]

            homeFeedViewModels.add(HomeRecommendationItemDataModel(product, pageName, layoutName, (((pageNumber - 1) * products.size) + position + 1), tabName))
        }
        return homeFeedViewModels
    }

    companion object {
        private const val TYPE_PRODUCT = "product"
        private const val TYPE_BANNER = "banner"
        const val TYPE_BANNER_ADS = "banner_ads"
        const val TYPE_VERTICAL_BANNER_ADS = "banner_ads_vertical"
    }
}
