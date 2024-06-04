package com.tokopedia.product.preview

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.product.preview.data.ProductPreviewMockData
import com.tokopedia.product.preview.robot.ProductPreviewViewModelUiTestRobot
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class ProductPreviewUiTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

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
            .validateEvent("openScreen")
            .validateEventAction("view - add to cart media fullscreen")
            .onSwipeProductContent()
            .onClickTab()
            .onSwipeTab()
            .validateEventAction("view - image content")
            .validateEventAction("scroll - swipe left right content")
            .onClickProductThumbnail()
            .validateEventAction("click - content thumbnail")
            .showProductAndReviewTab()
            .onClickBackButton()
            .validateEventAction("click - back button to pdp")
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
            .validateEventAction("view - ingatkan saya")
            .onClickAtcButton()
            .validateEventAction("click - ingatkan saya")
            .onClickNavigationButton()
            .validateEventAction("click - bottom nav")
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
            .validateEventAction("scroll - swipe up down ulasan tab")
            .onClickReviewAccountName()
            .validateEventAction("click - account name")
            .onClickReviewThreeDots()
            .validateEventAction("click - three dots")
            .onClickReviewReport()
            .validateEventAction("click - laporkan ulasan")
            .onClickSubmitReport()
            .validateEventAction("click - submit report ulasan")
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
            .validateEventAction("click - mode nonton")
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
            .validateEventAction("click - pause play button")
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
            .validateEventAction("click - like button")
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
            .validateEventAction("click - add to cart media fullscreen")
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
            .validateEventAction("click - variant bottomsheet atc entry point")
    }
}
