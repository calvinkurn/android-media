package com.tokopedia.tokopedianow.test.matcher

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.domain.model.GetHomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse
import com.tokopedia.tokopedianow.test.util.ProductCarouselDataUtil.getAssignedValueLabelGroup
import com.tokopedia.tokopedianow.test.util.ProductCarouselDataUtil.getPriceLabelGroup

class ProductCarouselMatcher {

    private val productCardMatcher = ProductCardMatcher()

    fun assertTitleShown() {
        val getHomeChannelV2 = HomeGraphqlResponse.dynamicChannelProductCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
            .response.data.first()
        val title = getHomeChannelV2.header.name
        Espresso.onView(ViewMatchers.withId(R.id.tp_title))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
    }

    fun assertSubtitleShown() {
        val getHomeChannelV2 = HomeGraphqlResponse.dynamicChannelProductCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
            .response.data.first()
        val title = getHomeChannelV2.header.subtitle
        Espresso.onView(ViewMatchers.withId(R.id.tp_subtitle))
            .check(ViewAssertions.matches(ViewMatchers.withText(title)))
    }

    fun assertProductListShown() {
        val getHomeChannelV2 = HomeGraphqlResponse.dynamicChannelProductCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
            .response.data.first()
        val products = getHomeChannelV2.grids

        products.forEachIndexed { index, product ->
            val productName = product.name
            val productPrice = product.price
            val slashPrice = product.slashedPrice
            val productRating = product.ratingFloat
            val discount = product.discount

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
        val getHomeChannelV2 = HomeGraphqlResponse.dynamicChannelProductCarouselResponse
            .getData<GetHomeLayoutResponse>(GetHomeLayoutResponse::class.java)
            .response.data.first()
        val products = getHomeChannelV2.grids

        products.forEachIndexed { index, _ ->
            val quantityList = listOf(1, 1)
            val expectedQuantity = quantityList.getOrNull(index)
            productCardMatcher.run {
                scrollProductToPosition(index)
                assertAddToCartQuantity(index, expectedQuantity)
            }
        }
    }
}
