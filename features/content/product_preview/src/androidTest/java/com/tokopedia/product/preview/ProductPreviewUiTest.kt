package com.tokopedia.product.preview

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.test.cassava.containsTrackerId
import com.tokopedia.product.preview.data.ProductPreviewMockData
import com.tokopedia.product.preview.robot.ProductPreviewViewModelUiTestRobot
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
    val composeTestRule = createEmptyComposeRule()

    @get:Rule
    var cassavaTest = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val analyticProductPreviewTracker = "tracker/content/productpreview/product_preview.json"

    private val mockData = ProductPreviewMockData()
    private val repository: ProductPreviewRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val productPreviewPref: ProductPreviewSharedPreferences = mockk(relaxed = true)
    private val router: Router = mockk(relaxed = true)

    private fun getProductPreviewRobot(
        sourceModel: ProductPreviewSourceModel,
        userSession: UserSessionInterface = mockk(relaxed = true),
        router: Router = mockk(relaxed = true)
    ) = ProductPreviewViewModelUiTestRobot(
        context = targetContext,
        composeTestRule = composeTestRule,
        productPreviewSourceModel = sourceModel,
        repo = repository,
        userSession = userSession,
        productPreviewPref = productPreviewPref,
        router = router
    )

    private val productId = "123"

    @Test
    fun test_shouldSwipeContentAndTabAndClosePage() {
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()

        getProductPreviewRobot(mockData.mockSourceProduct(productId))
            .onSwipeProductContent()
            .onClickTab()
            .onSwipeTab()
            .onClickProductThumbnail()
            .showProductAndReviewTab()
            .onClickBackButton()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49606")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49598")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49594")
        )
        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49589")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49588")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49587")
        )
    }

    @Test
    fun test_shouldShowAndClickRemindMeButton() {
        coEvery {
            userSession.isLoggedIn
        } returns true
        coEvery {
            repository.getProductMiniInfo(productId)
        } returns mockData.mockProductMiniInfo(buttonState = BottomNavUiModel.ButtonState.OOS)
        coEvery {
            repository.remindMe(productId)
        } returns BottomNavUiModel.RemindMeUiModel(isSuccess = true, message = "success")

        getProductPreviewRobot(
            userSession = userSession,
            sourceModel = mockData.mockSourceProduct(productId)
        )
            .showProductAndReviewTab()
            .showATCRemindMe()
            .onClickAtcButton()
            .onClickNavigationButton()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("50664")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49601")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49600")
        )
    }

    @Test
    fun test_shouldSwipeReviewNextContent() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()
        coEvery { repository.getReview(productId, 1) } returns mockData.mockReviewData()

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession
        )
            .onSwipeReviewContentFromProductPage()
            .onClickReviewAccountName()
            .onClickReviewThreeDots()
            .onClickReviewReport()
            .onClickSubmitReport()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49850")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49650")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49605")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49603")
        )

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49602")
        )
    }

    @Test
    fun test_shouldClickWatchModeReview() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()
        coEvery { repository.getReview(productId, 1) } returns mockData.mockReviewData()

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession
        )
            .onClickReviewWatchMode()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49651")
        )
    }

    @Test
    fun test_shouldClickPauseOrPlayVideo() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession
        )
            .onClickPauseOrPlayVideo()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49845")
        )
    }

    @Test
    fun test_shouldClickLikeUnLike() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()
        coEvery { repository.getReview(productId, 1) } returns mockData.mockReviewData()

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession
        )
            .onClickLikeUnLike()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("49851")
        )
    }

    @Test
    fun test_shouldClickAtcWithoutVariant() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo()

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession
        )
            .onClickAtcButton()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("50427")
        )
    }

    @Test
    fun test_shouldClickAtcWithVariant() {
        coEvery { userSession.isLoggedIn } returns true
        coEvery { repository.getProductMiniInfo(productId) } returns mockData.mockProductMiniInfo(hasVariant = true)

        getProductPreviewRobot(
            sourceModel = mockData.mockSourceProduct(productId),
            userSession = userSession,
            router = router
        )
            .onClickAtcButton()

        assertThat(
            cassavaTest.validate(analyticProductPreviewTracker),
            containsTrackerId("50428")
        )
    }
}
