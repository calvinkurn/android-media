package com.tokopedia.dilayanitokopedia.home.domain.mapper.recommendationforyou

import androidx.annotation.VisibleForTesting
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeRecommendationProductV2
import com.tokopedia.dilayanitokopedia.home.domain.model.Product
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import java.util.*

object HomeRecommendationMapper {

    @VisibleForTesting
    const val TYPE_PRODUCT = "product"
    private const val TYPE_BANNER = "banner"
    private const val TYPE_BANNER_ADS = "banner_ads"

    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetHomeRecommendationProductV2,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val visitables = mutableListOf<HomeRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationItemDataModel>()
        // reverse stack because to get the first in
        Collections.reverse(productStack)
        productStack.addAll(
            convertToHomeProductFeedModel(
                graphqlResponse.products,
                graphqlResponse.pageName,
                tabName,
                pageNumber
            )
        )

        // reverse stack because to get the first in
        Collections.reverse(productStack)

        graphqlResponse.positions.forEachIndexed { index, layoutType ->
            when (layoutType.type) {
                TYPE_PRODUCT -> {
                    visitables.add(productStack.pop())
                }
            }
        }

        return HomeRecommendationDataModel(visitables, graphqlResponse.hasNextPage)
    }

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
