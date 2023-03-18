package com.tokopedia.dilayanitokopedia.domain.mapper.recommendationforyou

import androidx.annotation.VisibleForTesting
import com.tokopedia.dilayanitokopedia.domain.model.GetHomeRecommendationProductV2
import com.tokopedia.dilayanitokopedia.domain.model.Product
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationVisitable
import java.util.*

object HomeRecommendationMapper {

    @VisibleForTesting
    const val TYPE_PRODUCT = "product"

    fun mapToHomeRecommendationDataModel(
        graphqlResponse: GetHomeRecommendationProductV2,
        tabName: String,
        pageNumber: Int
    ): HomeRecommendationDataModel {
        val visitables = mutableListOf<HomeRecommendationVisitable>()
        val productStack = Stack<HomeRecommendationItemDataModel>()

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
