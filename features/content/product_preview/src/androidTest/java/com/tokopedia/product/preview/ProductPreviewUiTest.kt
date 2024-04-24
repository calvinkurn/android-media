package com.tokopedia.product.preview

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.product.preview.data.ProductPreviewMockData
import com.tokopedia.product.preview.robot.ProductPreviewViewModelUiTestRobot
import com.tokopedia.product.preview.utils.containsTrackerId
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductPreviewUiTest {

    @get:Rule
    var cassavaTest = CassavaTestRule(sendValidationResult = false)

    private val analyticProductPreviewTracker = "tracker/content/productpreview/product_preview.json"

    private val mockData = ProductPreviewMockData()
    private val repository: ProductPreviewRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val productPreviewPref: ProductPreviewSharedPreferences = mockk(relaxed = true)

    private fun getProductPreviewRobot(
        sourceModel: ProductPreviewSourceModel
    ) = ProductPreviewViewModelUiTestRobot(
        productPreviewSourceModel = sourceModel,
        repo = repository,
        userSession = userSession,
        productPreviewPref = productPreviewPref
    )

    private val productId = "123"

    init {
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()
    }

    @Test
    fun test_shouldSwipeContentAndTab() {
        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .onSwipeProductContent()
            .onClickTab()
            .onSwipeTab()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49587")
        )
    }

    @Test
    fun test_shouldImpressVideo() {
        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .showProductAndReviewTab()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49588")
        )
    }

    @Test
    fun test_shouldImpressATC() {
        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .showProductAndReviewTab()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49589")
        )
    }

    @Test
    fun test_shouldClickProductThumbnail() {
        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .showProductAndReviewTab()
            .onClickProductThumbnail()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49594")
        )
    }

    @Test
    fun test_shouldShowImage() {
        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .showProductAndReviewTab()
            .onSwipeProductContent()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49598")
        )
    }
}
