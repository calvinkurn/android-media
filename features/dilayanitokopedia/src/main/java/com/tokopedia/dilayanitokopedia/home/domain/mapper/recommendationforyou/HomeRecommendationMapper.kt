package com.tokopedia.dilayanitokopedia.home.domain.mapper.recommendationforyou

import com.tokopedia.dilayanitokopedia.home.domain.model.GetDtHomeRecommendationResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.Product
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import java.util.*

object HomeRecommendationMapper {

    private const val TYPE_PRODUCT = "product"
    private const val TYPE_BANNER = "banner"
    private const val TYPE_BANNER_ADS = "banner_ads"

    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetDtHomeRecommendationResponse,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val recommendationProduct = graphqlResponse.response
        val visitables = mutableListOf<HomeRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationItemDataModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(
            convertToHomeProductFeedModel(
                recommendationProduct.products,
                recommendationProduct.pageName,
                tabName,
                pageNumber
            )
        )

        val bannerStack = Stack<BannerRecommendationDataModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
//        bannerStack.addAll(convertToHomeBannerFeedModel(recommendationProduct.banners, tabName, pageNumber))

        visitables.addAll(productStack)

        recommendationProduct.positions.forEachIndexed { index, layoutType ->
            when (layoutType.type) {
                TYPE_PRODUCT -> {
                    visitables.add(productStack.pop())
                }
                TYPE_BANNER -> {
                    visitables.add(bannerStack.pop())
                }
                TYPE_BANNER_ADS -> {
                    visitables.add(HomeRecommendationBannerTopAdsDataModel(position = index))
                }
            }
        }

        return HomeRecommendationDataModel(visitables, recommendationProduct.hasNextPage)
    }

    //    private fun convertToHomeBannerFeedModel(
//        banners: List<Banner>,
//        tabName: String,
//        pageNumber: Int
//    ): List<BannerRecommendationDataModel> {
//        val bannerFeedViewModels = ArrayList<BannerRecommendationDataModel>()
//        for (position in banners.indices) {
//            val banner = banners[position]
//
//            bannerFeedViewModels.add(
//                BannerRecommendationDataModel(
//                    banner.id,
//                    banner.name,
//                    banner.imageUrl,
//                    banner.url,
//                    banner.applink,
//                    banner.buAttribution,
//                    banner.creativeName,
//                    banner.target,
//                    (((pageNumber - 1) * banners.size) + position + 1).toInt(),
//                    banner.galaxyAttribution,
//                    banner.persona,
//                    banner.brandId,
//                    banner.categoryPersona,
//                    tabName
//                )
//            )
//        }
//        return bannerFeedViewModels
//    }
//
    private fun convertToHomeProductFeedModel(
        products: List<Product>,
        pageName: String,
        tabName: String,
        pageNumber: Int
    ): List<HomeRecommendationItemDataModel> {
        val homeFeedViewModels = ArrayList<HomeRecommendationItemDataModel>()
        for (position in products.indices) {
            val product = products[position]

            homeFeedViewModels.add(
                HomeRecommendationItemDataModel(
                    product,
                    pageName,
                    (((pageNumber - 1) * products.size) + position + 1),
                    tabName
                )
            )
        }
        return homeFeedViewModels
    }
}
