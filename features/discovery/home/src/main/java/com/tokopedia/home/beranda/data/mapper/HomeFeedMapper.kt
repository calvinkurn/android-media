package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.Banner
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import rx.functions.Func1
import java.util.*

/**
 * Created by henrypriyono on 12/29/17.
 */

class HomeFeedMapper : Func1<GraphqlResponse, HomeRecommendationDataModel> {

    companion object{
        private const val TYPE_PRODUCT = "product"
        private const val TYPE_BANNER = "banner"
    }

    override fun call(graphqlResponse: GraphqlResponse): HomeRecommendationDataModel {
        val gqlResponse = graphqlResponse.getData<HomeFeedContentGqlResponse>(HomeFeedContentGqlResponse::class.java)
        val recommendationProduct = gqlResponse!!.homeRecommendation!!.recommendationProduct

        val visitables = mutableListOf<HomeRecommendationVisitable>()

        val productStack = Stack<HomeRecommendationItemDataModel>()
        //reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(convertToHomeProductFeedModel(recommendationProduct!!.product))

        val bannerStack = Stack<BannerRecommendationDataModel>()
        //reverse stack because to get the first in
        Collections.reverse(productStack)
        bannerStack.addAll(convertToHomeBannerFeedModel(recommendationProduct.banners))

        recommendationProduct.layoutTypes.forEachIndexed { index, layoutType ->
            if (layoutType.type.equals(TYPE_PRODUCT)) {
                visitables.add(productStack.pop())
            } else if(layoutType.type.equals(TYPE_BANNER)) {
                visitables.add(bannerStack.pop())
            }
        }
        return HomeRecommendationDataModel(visitables, recommendationProduct.hasNextPage)
    }

    private fun convertToHomeBannerFeedModel(banners: List<Banner>): List<BannerRecommendationDataModel> {
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
                            position,
                            banner.galaxyAttribution,
                            banner.persona,
                            banner.brandId,
                            banner.categoryPersona,
                            ""
                    )
            )
        }
        return bannerFeedViewModels
    }

    private fun convertToHomeProductFeedModel(products: List<Product>): List<HomeRecommendationItemDataModel> {
        val homeFeedViewModels = ArrayList<HomeRecommendationItemDataModel>()
        for (position in products.indices) {
            val product = products[position]

            homeFeedViewModels.add(HomeRecommendationItemDataModel(
                    product,
                    position + 1
            ))
        }
        return homeFeedViewModels
    }

    private fun isLabelDiscountVisible(productItem: Product): Boolean {
        return productItem.discountPercentage > 0
    }
}