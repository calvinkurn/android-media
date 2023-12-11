package com.tokopedia.tokopedianow.test.matcher

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.test.util.RecommendationDataUtil.getAssignedValueLabelGroup
import com.tokopedia.tokopedianow.test.util.RecommendationDataUtil.getPriceLabelGroup
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse.dynamicChannelChipCarouselResponse
import com.tokopedia.tokopedianow.home.domain.repository.ProductRecomGraphqlResponse.getProductRecommendationResponse
import com.tokopedia.unifycomponents.R as unifycomponentsR

class ChipCarouselMatcher {

    private val productCardMatcher = ProductCardMatcher(
        containerId = R.id.product_carousel_chip_view
    )

    fun assertTitleShown() {
        val getHomeChannelV2 = dynamicChannelChipCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
            .response.data.first()
        val title = getHomeChannelV2.header.name
        onView(withId(R.id.tp_title)).check(matches(withText(title)))
    }

    fun assertChipTabsShown() {
        val getHomeChannelV2 = dynamicChannelChipCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
        val grids = getHomeChannelV2.response.data.first().grids

        grids.forEachIndexed { index, grid ->
            scrollChipToPosition(index)
            assertChipText(index, grid)
        }
    }

    fun assertProductListShown(
        productRecommendationResponse: RecommendationEntity = getProductRecommendationResponse
            .getData(RecommendationEntity::class.java)
    ) {
        val products = productRecommendationResponse.productRecommendationWidget
            .data.first().recommendation

        products.forEachIndexed { index, product ->
            val productName = product.name
            val productPrice = product.price
            val slashPrice = product.slashedPrice
            val productRating = product.ratingAverage
            val discountPercentage = product.discountPercentage
            val discount = if(discountPercentage > 0) {
                "$discountPercentage%"
            } else {
                ""
            }

            val promoLabel = product.getPriceLabelGroup()
            val valueLabel = product.getAssignedValueLabelGroup()

            val promoText = promoLabel?.title
            val valueText = valueLabel?.title

            productCardMatcher.run {
                scrollProductToPosition(index)
                assertProductName(index, productName)
                assertProductPrice(index, productPrice)
                assertSlashPrice(index, slashPrice)
                assertPromoLabel(index, discount, promoText)
                assertValueLabel(index, valueText)
                assertProductRating(index, productRating)
            }
        }
    }

    fun assertProductAddToCartQuantityUpdated() {
        val getProductRecommendation = getProductRecommendationResponse
            .getData<RecommendationEntity>(RecommendationEntity::class.java)
        val products = getProductRecommendation.productRecommendationWidget
            .data.first().recommendation

        products.forEachIndexed { index, _ ->
            val quantityList = listOf(1, 2)
            val expectedQuantity = quantityList.getOrNull(index)
            productCardMatcher.run {
                scrollProductToPosition(index)
                assertAddToCartQuantity(index, expectedQuantity)
            }
        }
    }

    private fun scrollChipToPosition(index: Int) {
        onView(withId(R.id.chips_tab))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    private fun assertChipText(index: Int, grid: Grid) {
        onView(RecyclerViewMatcher(R.id.chips_tab)
            .atPositionOnView(index, unifycomponentsR.id.chip_text))
            .check(matches(withText(grid.name)))
    }
}
