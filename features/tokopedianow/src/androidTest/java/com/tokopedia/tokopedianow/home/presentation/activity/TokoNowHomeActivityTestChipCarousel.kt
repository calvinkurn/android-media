package com.tokopedia.tokopedianow.home.presentation.activity

import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokopedianow.test.matcher.ChipCarouselMatcher
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse.dynamicChannelChipCarouselResponse
import com.tokopedia.tokopedianow.home.domain.repository.ProductRecomGraphqlResponse.getProductRecommendationResponse
import com.tokopedia.tokopedianow.home.domain.repository.ProductRecomGraphqlResponse.getProductRecommendationTelurResponse
import org.junit.Test

@UiTest
class TokoNowHomeActivityTestChipCarousel : TokoNowHomeActivityTestFixture() {

    private val chipCarouselMatcher = ChipCarouselMatcher()

    @Test
    fun given_chip_carousel_response_when_open_homepage_should_show_chip_carousel_widget() {
        robot.run {
            mockGraphql()
            mockIsUserLoggedIn(
                loggedIn = false
            )

            openHomePage()

            assertChipCarouselShown()
        }
    }

    @Test
    fun given_chip_carousel_response_when_click_chip_tab_should_show_product_list_based_on_active_tab() {
        robot.run {
            mockGraphql()
            mockIsUserLoggedIn(
                loggedIn = false
            )

            openHomePage()

            mockGetProductRecommendationTelur()

            clickCarouselChipTab(tab = "Telur")

            assertTelurChipCarouselShown()
        }
    }

    @Test
    fun given_mini_cart_data_when_resume_activity_should_update_product_add_to_cart_quantity() {
        robot.run {
            mockGraphql()
            mockIsUserLoggedIn(
                loggedIn = true
            )

            openHomePage()

            mockGetMiniCartSuccess()

            onResumeActivity()

            assertProductAddToCartQuantityUpdated()
        }
    }

    @Test
    fun given_mini_cart_data_when_add_to_cart_success_should_update_product_add_to_cart_quantity() {
        robot.run {
            mockGraphql()
            mockIsUserLoggedIn(
                loggedIn = true
            )

            openHomePage()

            mockAddToCartSuccess()
            mockGetMiniCartSuccess()

            addToCartOnView(position = 0)

            assertProductAddToCartQuantityUpdated()
        }
    }

    private fun assertChipCarouselShown() {
        chipCarouselMatcher.run {
            assertTitleShown()
            assertChipTabsShown()
            assertProductListShown()
        }
    }

    private fun assertTelurChipCarouselShown() {
        chipCarouselMatcher.assertProductListShown(
            productRecommendationResponse = getProductRecommendationTelurResponse
                .getData(RecommendationEntity::class.java)
        )
    }

    private fun assertProductAddToCartQuantityUpdated() {
        chipCarouselMatcher.assertProductAddToCartQuantityUpdated()
    }

    private fun mockGraphql() {
        mockGraphql {
            "getHomeChannelV2" to dynamicChannelChipCarouselResponse
        }
        mockGraphql {
            "productRecommendation" to getProductRecommendationResponse
        }
    }

    private fun mockGetProductRecommendationTelur() {
        mockGraphql {
            "productRecommendation" to getProductRecommendationTelurResponse
        }
    }
}
