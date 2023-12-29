package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.remoteconfig.RollenceKey
import io.mockk.every
import org.junit.Test


internal class SimilarSearchCoachmarkViewModelTest : ProductCardOptionsViewModelTestFixtures() {

    private val productCardOptionsModel = ProductCardOptionsModel(hasSimilarSearch = true)

    private val similarProductOption = ProductCardOptionsItemModel(
        title = SEE_SIMILAR_PRODUCTS,
    )

    private fun `Given similar search ab test active`() {
        every {
            abTestRemoteConfig.getString(RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK, any())
        } returns RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK_VARIANT
    }

    private fun `Given similar search ab test inactive`() {
        every {
            abTestRemoteConfig.getString(RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK, any())
        } returns ""
    }

    private fun `Given similar search coach mark local cache return true`() {
        every { similarSearchCoachMarkLocalCache.shouldShowSimilarSearchProductOptionCoachmark() } returns true
    }

    private fun `Given similar search coach mark local cache return false`() {
        every { similarSearchCoachMarkLocalCache.shouldShowSimilarSearchProductOptionCoachmark() } returns false
    }


    private fun `Given Product Card Options View Model`() {
        createProductCardOptionsViewModel(productCardOptionsModel)
    }

    private fun `When check should display similar search coachmark`(
        adapterPosition: Int,
        option: ProductCardOptionsItemModel = similarProductOption,
    ) {
        productCardOptionsViewModel.checkShouldDisplaySimilarSearchCoachmark(option, adapterPosition)
    }

    private fun `Then verify CoachmarkEvent`(expected: CoachmarkEvent?) {
            val coachmarkEventLiveData = productCardOptionsViewModel.getCoachmarkEventLiveData().value

            coachmarkEventLiveData?.getContentIfNotHandled() shouldBe expected
    }

    @Test
    fun `Similar search coach mark should be displayed on ab test active and local cache return true`() {
        val adapterPosition = 1
        `Given Product Card Options View Model`()
        `Given similar search ab test active`()
        `Given similar search coach mark local cache return true`()

        `When check should display similar search coachmark`(adapterPosition)

        `Then verify CoachmarkEvent`(CoachmarkEvent(adapterPosition))
    }

    @Test
    fun `Similar search coach mark should not be displayed on non similar search option when ab test active and local cache return true`() {
        val adapterPosition = 1
        `Given Product Card Options View Model`()
        `Given similar search ab test active`()
        `Given similar search coach mark local cache return true`()

        `When check should display similar search coachmark`(adapterPosition, ProductCardOptionsItemModel())

        `Then verify CoachmarkEvent`(null)
    }

    @Test
    fun `Similar search coach mark should not be displayed on ab test active and local cache return false`() {
        val adapterPosition = 1
        `Given Product Card Options View Model`()
        `Given similar search ab test active`()
        `Given similar search coach mark local cache return false`()

        `When check should display similar search coachmark`(adapterPosition)

        `Then verify CoachmarkEvent`(null)
    }

    @Test
    fun `Similar search coach mark should not be displayed on ab test inactive and local cache return true`() {
        val adapterPosition = 1
        `Given Product Card Options View Model`()
        `Given similar search ab test inactive`()
        `Given similar search coach mark local cache return true`()

        `When check should display similar search coachmark`(adapterPosition)

        `Then verify CoachmarkEvent`(null)
    }

    @Test
    fun `Similar search coach mark should not be displayed on ab test inactive and local cache return false`() {
        val adapterPosition = 1
        `Given Product Card Options View Model`()
        `Given similar search ab test inactive`()
        `Given similar search coach mark local cache return false`()

        `When check should display similar search coachmark`(adapterPosition)

        `Then verify CoachmarkEvent`(null)
    }
}
