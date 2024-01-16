package com.tokopedia.tokopedianow.home.presentation.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokopedianow.home.domain.repository.HomeGraphqlResponse.dynamicChannelProductCarouselResponse
import com.tokopedia.tokopedianow.test.matcher.ProductCarouselMatcher
import org.junit.Test

@UiTest
class TokoNowHomeActivityTestProductCarousel : TokoNowHomeActivityTestFixture() {

    private val productCarouselMatcher = ProductCarouselMatcher()

    @Test
    fun given_product_carousel_response_when_open_homepage_should_show_product_carousel_widget() {
        robot.run {
            mockGraphql()
            mockIsUserLoggedIn(
                loggedIn = false
            )

            openHomePage()

            assertProductCarouselShown()
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

    private fun assertProductCarouselShown() {
        productCarouselMatcher.run {
            assertTitleShown()
            assertSubtitleShown()
            assertProductListShown()
        }
    }

    private fun assertProductAddToCartQuantityUpdated() {
        productCarouselMatcher.assertProductAddToCartQuantityUpdated()
    }

    private fun mockGraphql() {
        mockGraphql {
            "getHomeChannelV2" to dynamicChannelProductCarouselResponse
        }
    }
}
