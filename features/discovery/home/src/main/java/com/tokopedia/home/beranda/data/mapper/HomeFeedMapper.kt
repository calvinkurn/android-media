package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.Banner
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeFeedTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerFeedViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedListModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel
import rx.functions.Func1
import java.util.*

/**
 * Created by henrypriyono on 12/29/17.
 */

class HomeFeedMapper : Func1<GraphqlResponse, HomeFeedListModel> {

    val TYPE_PRODUCT = "product"
    val TYPE_BANNER = "banner"

    override fun call(graphqlResponse: GraphqlResponse): HomeFeedListModel {
        val gqlResponse = graphqlResponse.getData<HomeFeedContentGqlResponse>(HomeFeedContentGqlResponse::class.java)
        val recommendationProduct = gqlResponse!!.homeRecommendation.recommendationProduct

        val visitables = mutableListOf<Visitable<HomeFeedTypeFactory>>()

        val productStack = Stack<HomeFeedViewModel>()
        //reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(convertToHomeProductFeedModel(recommendationProduct.product))

        val bannerStack = Stack<BannerFeedViewModel>()
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
        return HomeFeedListModel(visitables, recommendationProduct.hasNextPage)
    }

    private fun convertToHomeBannerFeedModel(banners: List<Banner>): List<BannerFeedViewModel> {
        val bannerFeedViewModels = ArrayList<BannerFeedViewModel>()
        for (position in banners.indices) {
            val banner = banners[position]

            bannerFeedViewModels.add(
                    BannerFeedViewModel(
                            banner.id,
                            banner.name,
                            banner.imageUrl,
                            banner.url,
                            banner.applink,
                            banner.buAttribution,
                            banner.creativeName,
                            banner.target,
                            position
                    )
            )
        }
        return bannerFeedViewModels
    }

    private fun convertToHomeProductFeedModel(products: List<Product>): List<HomeFeedViewModel> {
        val homeFeedViewModels = ArrayList<HomeFeedViewModel>()
        for (position in products.indices) {
            val product = products[position]

            homeFeedViewModels.add(HomeFeedViewModel(
                    product.id,
                    product.name,
                    product.categoryBreadcrumbs,
                    product.recommendationType,
                    product.imageUrl,
                    product.price,
                    product.rating ?: 0,
                    product.countReview ?: 0,
                    product.clickUrl,
                    product.trackerImageUrl,
                    product.slashedPrice,
                    product.discountPercentage!!,
                    product.priceInt ?: 0,
                    product.isTopads ?: false,
                    position + 1,
                    product.labelGroup,
                    product.labels,
                    product.badges,
                    product.shop.city,
                    product.wishlistUrl,
                    product.isWishlist
            ))
        }
        return homeFeedViewModels
    }
}