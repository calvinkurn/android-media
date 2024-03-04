package com.tokopedia.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.product.preview.data.mock.ProductPreviewMockData
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreference
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewUiEvent
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertFalse
import com.tokopedia.content.test.util.assertNotEqualTo
import com.tokopedia.content.test.util.assertTrue
import com.tokopedia.content.test.util.assertType
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.product.preview.robot.ProductPreviewViewModelRobot
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ProductPreviewUnitTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()
    private val testDispatcher = rule.dispatchers

    private val mockDataSource = ProductPreviewMockData()

    private val productId = "productId_12345"
    private val reviewSourceId = "reviewSourceId_12345"
    private val attachmentId = "attachmentId_12345"
    private val mockRepository: ProductPreviewRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockSharedPref: ProductPreviewSharedPreference = mockk(relaxed = true)

    private fun getRobot(sourceModel: ProductPreviewSourceModel): ProductPreviewViewModelRobot {
        return ProductPreviewViewModelRobot(
            dispatchers = testDispatcher,
            source = sourceModel,
            repository = mockRepository,
            userSession = mockUserSession,
            sharedPref = mockSharedPref,
        )
    }

    @Test
    fun `when checking initial source and source is product with no review`() {
        val sourceModel = mockDataSource.mockSourceProduct(
            productId = productId,
            hasReview = false
        )

        getRobot(sourceModel).use { robot ->
            val state = robot.recordState {
                robot.checkInitialSourceTestCase()
            }

            state.tabsUiModel.tabs.size.assertEqualTo(1)
            state.tabsUiModel.tabs.first { it.key == ProductPreviewTabUiModel.TAB_PRODUCT_KEY }
        }
    }

    @Test
    fun `when checking initial source and source is product with review`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.checkInitialSourceTestCase()
            }.also { state ->
                state.tabsUiModel.tabs.size.assertEqualTo(2)
                state.tabsUiModel.tabs.first { it.key == ProductPreviewTabUiModel.TAB_PRODUCT_KEY }
                state.tabsUiModel.tabs.last { it.key == ProductPreviewTabUiModel.TAB_REVIEW_KEY }
            }
        }
    }

    @Test
    fun `when checking initial source and source is review`() {
        val sourceModel = mockDataSource.mockSourceReview(productId, reviewSourceId, attachmentId)

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.checkInitialSourceTestCase()
            }.also { state ->
                state.tabsUiModel.tabs.size.assertEqualTo(1)
                state.tabsUiModel.tabs.first { it.key == ProductPreviewTabUiModel.TAB_REVIEW_KEY }
            }
        }
    }

    @Test
    fun `when checking initial source and source is unknown`() {
        val sourceModel = mockDataSource.mockSourceUnknown(productId)

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.checkInitialSourceTestCase()
            }.also { state ->
                state.last().assertEqualTo(ProductPreviewUiEvent.UnknownSourceData)
            }
        }
    }

    @Test
    fun `when fetch mini info success`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo()

        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.fetchMiniInfoTestCase()
            }.also { state ->
                state.bottomNavUiModel.assertEqualTo(expectedData)
            }
        }
    }

    @Test
    fun `when fetch mini info fail`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedThrow = Throwable("fail fetching")

        coEvery { mockRepository.getProductMiniInfo(productId) } throws expectedThrow

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.fetchMiniInfoTestCase()
            }.also { state ->
                state.last().assertEqualTo(ProductPreviewUiEvent.FailFetchMiniInfo(expectedThrow))
            }
        }
    }

    @Test
    fun `when initialize product main data`() {
        val productMedia = mockDataSource.mockProductMediaList()
        val sourceModel = mockDataSource.mockSourceProduct(productId)

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.initializeProductMainDataTestCase()
            }.also { state ->
                state.productUiModel.productMedia.assertEqualTo(productMedia)
            }
        }
    }

    @Test
    fun `when initialize review main data source product and success fetch review data`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedDataReview = mockDataSource.mockReviewData()

        coEvery { mockRepository.getReview(productId, 1) } returns expectedDataReview

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.initializeReviewMainDataTestCase()
            }.also { state ->
                state.reviewUiModel.reviewContent.assertEqualTo(expectedDataReview.reviewContent)
                state.reviewUiModel.reviewContent.size.assertEqualTo(expectedDataReview.reviewContent.size)
                state.reviewUiModel.reviewPaging.assertEqualTo(expectedDataReview.reviewPaging)
            }
        }
    }

    @Test
    fun `when initialize review main data source product and fails fetch review data`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedThrow = Throwable("fail fetching")

        coEvery { mockRepository.getReview(productId, 1) } throws expectedThrow

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.initializeReviewMainDataTestCase()
            }.also { state ->
                state.reviewUiModel.reviewPaging.assertType<ReviewPaging.Error>()
                (state.reviewUiModel.reviewPaging as ReviewPaging.Error).onRetry()
            }
        }
    }

    @Test
    fun `when initialize review main data source review and success fetch review data by ids`() {
        val sourceModel = mockDataSource.mockSourceReview(productId, reviewSourceId, attachmentId)
        val expectedDataReviewByIds = mockDataSource.mockReviewDataByIds(reviewSourceId)
        val expectedDataReview = mockDataSource.mockReviewData()

        coEvery { mockRepository.getReviewByIds(listOf(reviewSourceId)) } returns expectedDataReviewByIds
        coEvery { mockRepository.getReview(productId, 1) } returns expectedDataReview

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.initializeReviewMainDataTestCase()
            }.also { state ->
                state.reviewUiModel.reviewContent.assertEqualTo(
                    expectedDataReviewByIds.reviewContent
                        .plus(expectedDataReview.reviewContent)
                )
                state.reviewUiModel.reviewContent.size.assertEqualTo(
                    expectedDataReviewByIds.reviewContent.size
                        .plus(expectedDataReview.reviewContent.size)
                )
                state.reviewUiModel.reviewPaging.assertEqualTo(expectedDataReview.reviewPaging)
            }
        }
    }

    @Test
    fun `when initialize review main data source review and fails fetch review data by ids`() {
        val sourceModel = mockDataSource.mockSourceReview(productId, reviewSourceId, attachmentId)
        val expectedThrow = Throwable("fail fetching")

        coEvery { mockRepository.getReviewByIds(listOf(reviewSourceId)) } throws expectedThrow

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.initializeReviewMainDataTestCase()
            }.also { state ->
                state.reviewUiModel.reviewPaging.assertType<ReviewPaging.Error>()
                (state.reviewUiModel.reviewPaging as ReviewPaging.Error).onRetry()
            }
        }
    }

    @Test
    fun `when initialize review main data source unknown and emit error`() {
        val sourceModel = mockDataSource.mockSourceUnknown(productId)

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.initializeReviewMainDataTestCase()
            }.also { event ->
                event.last().assertEqualTo(ProductPreviewUiEvent.UnknownSourceData)
            }
        }
    }

    @Test
    fun `when product media video ended and selected media should be plus 1`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val firstSelected = when (val source = sourceModel.source) {
            is ProductPreviewSourceModel.ProductSourceData -> {
                source.productSourceList.indexOfFirst { it.selected }
            }

            is ProductPreviewSourceModel.ReviewSourceData -> -1
            ProductPreviewSourceModel.UnknownSource -> -1
        }

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.productMediaVideoEndedTestCase()
            }.also { state ->
                val selected = state.productUiModel.productMedia.indexOfFirst { it.selected }
                selected.assertEqualTo(firstSelected.plus(1))
            }
        }
    }

    @Test
    fun `when product media selected media when preselected not first index then selected media should be changed`() {
        val sourceModel = mockDataSource.mockSourceProduct(
            productId = productId,
            productMedia = mockDataSource.mockProductMediaListSelectedNotFirstIndex()
        )
        val selectedMedia = 0

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.productMediaSelectedTestCase(selectedMedia)
            }.also { state ->
                val selected = state.productUiModel.productMedia.indexOfFirst { it.selected }
                selected.assertEqualTo(selectedMedia)
            }
        }
    }

    @Test
    fun `when review content selected and selected position should not same with previous`() {
        val sourceModel = mockDataSource.mockSourceReview(productId, reviewSourceId, attachmentId)
        var currentSelected: Int
        var lastSelected: Int
        val mockReviewData = mockDataSource.mockReviewDataByIds(reviewSourceId)

        coEvery { mockRepository.getReviewByIds(listOf(reviewSourceId)) } returns mockReviewData

        getRobot(sourceModel).use { robot ->
            currentSelected = robot._reviewPosition.value
            currentSelected.assertEqualTo(0)

            robot.reviewContentSelectedTestCase(2)

            lastSelected = robot._reviewPosition.value
            lastSelected.assertEqualTo(2)

            currentSelected.assertNotEqualTo(lastSelected)
        }
    }

    @Test
    fun `when review content scrolling and scrolling state should be changed`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val scrolledPosition = 2
        val isScrolling = true
        val mockReviewData = mockDataSource.mockReviewData()

        coEvery { mockRepository.getReview(productId, 1) } returns mockReviewData

        getRobot(sourceModel).use { robot ->
            robot.recordState {
                robot.reviewContentScrollingState(scrolledPosition, true)
            }.also { state ->
                state.reviewUiModel.reviewContent.mapIndexed { index, reviewContentUiModel ->
                    if (index == scrolledPosition) reviewContentUiModel.isScrolling.assertEqualTo(
                        isScrolling
                    )
                    else reviewContentUiModel.isScrolling.assertFalse()
                }
            }
        }
    }

    @Test
    fun `when review media selected and selected media should changed`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        var reviewPosition: Int
        val mediaSelectedPosition = 1
        val mockReviewData = mockDataSource.mockReviewData()

        coEvery { mockRepository.getReview(productId, 1) } returns mockReviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordState {
                robot.reviewMediaSelectedTestCase(mediaSelectedPosition)
            }.also { state ->
                val selectedMedia = state.reviewUiModel.reviewContent[reviewPosition]
                    .medias.indexOfFirst { it.selected }
                selectedMedia.assertEqualTo(mediaSelectedPosition)
                state.reviewUiModel.reviewContent.mapIndexed { index, reviewContentUiModel ->
                    if (index == reviewPosition) reviewContentUiModel.mediaSelectedPosition.assertEqualTo(
                        mediaSelectedPosition
                    )
                    else reviewContentUiModel.mediaSelectedPosition.assertNotEqualTo(
                        mediaSelectedPosition
                    )
                }
            }
        }
    }

    @Test
    fun `when handling tab selected and selected is product then update tab current position`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val selectedTab = 0

        getRobot(sourceModel).use { robot ->
            robot._currentTabPosition.value.assertNotEqualTo(selectedTab)
            robot.initializeProductMainDataTestCase()
            robot.tabSelectedTestCase(selectedTab)
            robot._currentTabPosition.value.assertEqualTo(selectedTab)
        }
    }

    @Test
    fun `when handling tab selected and selected is review then update tab current position`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val selectedTab = 1

        getRobot(sourceModel).use { robot ->
            robot._currentTabPosition.value.assertNotEqualTo(selectedTab)
            robot.tabSelectedTestCase(selectedTab)
            robot._currentTabPosition.value.assertEqualTo(selectedTab)
        }
    }

    @Test
    fun `when product action add to chart and user not login should emit login event`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = true,
            buttonState = BottomNavUiModel.ButtonState.Active,
        )

        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertEqualTo(ProductPreviewUiEvent.LoginUiEvent(expectedData))
            }
        }
    }

    @Test
    fun `when product action add to chart and success then should emit success toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = true,
            buttonState = BottomNavUiModel.ButtonState.Active,
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData
        coEvery {
            mockRepository.addToCart(
                productId,
                expectedData.title,
                expectedData.shop.id,
                expectedData.price.finalPrice.toDoubleOrZero()
            )
        } returns true

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertEqualTo(
                    ProductPreviewUiEvent.ShowSuccessToaster(
                        type = ProductPreviewUiEvent.ShowSuccessToaster.Type.ATC,
                        message = ProductPreviewUiEvent.ShowSuccessToaster.Type.ATC.textRes
                    )
                )
            }
        }
    }

    @Test
    fun `when product action add to chart and fail should emit error toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = true,
            buttonState = BottomNavUiModel.ButtonState.Active,
        )

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData
        coEvery {
            mockRepository.addToCart(
                productId,
                expectedData.title,
                expectedData.shop.id,
                expectedData.price.finalPrice.toDoubleOrZero()
            )
        } returns false

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertType<ProductPreviewUiEvent.ShowErrorToaster>()
                (event.last() as ProductPreviewUiEvent.ShowErrorToaster).onClick()
            }
        }
    }

    @Test
    fun `when product action remind me and user not login should emit login event`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = false,
            buttonState = BottomNavUiModel.ButtonState.OOS,
        )

        coEvery { mockUserSession.isLoggedIn } returns false
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertEqualTo(ProductPreviewUiEvent.LoginUiEvent(expectedData))
            }
        }
    }

    @Test
    fun `when product action remind me and success then should emit success toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = false,
            buttonState = BottomNavUiModel.ButtonState.OOS,
        )
        val expectedResult = BottomNavUiModel.RemindMeUiModel(true, "success")

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData
        coEvery { mockRepository.remindMe(productId) } returns expectedResult

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertEqualTo(
                    ProductPreviewUiEvent.ShowSuccessToaster(
                        type = ProductPreviewUiEvent.ShowSuccessToaster.Type.Remind,
                        message = ProductPreviewUiEvent.ShowSuccessToaster.Type.Remind.textRes
                    )
                )
            }
        }
    }

    @Test
    fun `when product action remind me and fail should emit error toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val expectedData = mockDataSource.mockProductMiniInfo(
            hasVariant = false,
            buttonState = BottomNavUiModel.ButtonState.OOS,
        )
        val expectedResult = BottomNavUiModel.RemindMeUiModel(false, "not success")

        coEvery { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepository.getProductMiniInfo(productId) } returns expectedData
        coEvery { mockRepository.remindMe(productId) } returns expectedResult

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.productActionAddToChartTestCase(expectedData)
            }.also { event ->
                event.last().assertType<ProductPreviewUiEvent.ShowErrorToaster>()
                (event.last() as ProductPreviewUiEvent.ShowErrorToaster).onClick()
            }
        }
    }

    @Test
    fun `when navigate to app link and should emit event with corresponding app link`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val appLink = "tokopedia://product/123567"

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.navigateAppTestCase(appLink)
            }.also { event ->
                event.last().assertEqualTo(ProductPreviewUiEvent.NavigateUiEvent(appLink))
            }
        }
    }

    @Test
    fun `when submit report and fail should emit event error toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val model = mockDataSource.mockReviewReport()
        val expectedThrow = Throwable("fail fetch")

        coEvery { mockRepository.submitReport(model, reviewSourceId) } throws expectedThrow

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.submitReportTestCase(model)
            }.also { event ->
                event.last().assertType<ProductPreviewUiEvent.ShowErrorToaster>()
                (event.last() as ProductPreviewUiEvent.ShowErrorToaster).onClick()
            }
        }
    }

    @Test
    fun `when submit report and success should emit event success toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val model = mockDataSource.mockReviewReport()

        coEvery { mockRepository.submitReport(model, any()) } returns true

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.submitReportTestCase(model)
            }.also { event ->
                event.last().assertEqualTo(
                    ProductPreviewUiEvent.ShowSuccessToaster(
                        type = ProductPreviewUiEvent.ShowSuccessToaster.Type.Report
                    )
                )
            }
        }
    }

    @Test
    fun `when submit report and fail request should emit event success toaster`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val model = mockDataSource.mockReviewReport()

        coEvery { mockRepository.submitReport(model, reviewSourceId) } returns false

        getRobot(sourceModel).use { robot ->
            robot.recordEvent {
                robot.submitReportTestCase(model)
            }.also { event ->
                event.last().assertType<ProductPreviewUiEvent.ShowErrorToaster>()
                (event.last() as ProductPreviewUiEvent.ShowErrorToaster).onClick()
            }
        }
    }

    @Test
    fun `when click menu from login and user is login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isFromLogin = true
        val isLogin = true
        val userId = "123"
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockUserSession.userId } returns userId
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordStateAndEvents {
                robot.clickMenuTestCase(isFromLogin)
            }.also { value ->
                value.first.reviewUiModel.reviewContent[reviewPosition].menus.isReportable.assertTrue()
                value.second.last()
                    .assertEqualTo(ProductPreviewUiEvent.ShowMenuSheet(reviewData.reviewContent[reviewPosition].menus))
            }
        }
    }

    @Test
    fun `when click menu from login and user is not login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isFromLogin = true
        val isLogin = false
        val userId = "123"
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockUserSession.userId } returns userId
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordStateAndEvents {
                robot.clickMenuTestCase(isFromLogin)
            }.also { value ->
                value.first.reviewUiModel.reviewContent[reviewPosition].menus.isReportable.assertFalse()
                value.second.last().assertEqualTo(
                    ProductPreviewUiEvent.LoginUiEvent(
                        reviewData.reviewContent[reviewPosition].menus.copy(isReportable = false)
                    )
                )
            }
        }
    }

    @Test
    fun `when click menu from not login and user is login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isFromLogin = true
        val isLogin = true
        val userId = "123"
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockUserSession.userId } returns userId
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordStateAndEvents {
                robot.clickMenuTestCase(isFromLogin)
            }.also { value ->
                value.first.reviewUiModel.reviewContent[reviewPosition].menus.isReportable.assertTrue()
                value.second.last()
                    .assertEqualTo(ProductPreviewUiEvent.ShowMenuSheet(reviewData.reviewContent[reviewPosition].menus))
            }
        }
    }

    @Test
    fun `when click menu from not login and user is not login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isFromLogin = true
        val isLogin = false
        val userId = "123"
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockUserSession.userId } returns userId
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordStateAndEvents {
                robot.clickMenuTestCase(isFromLogin)
            }.also { value ->
                value.first.reviewUiModel.reviewContent[reviewPosition].menus.isReportable.assertFalse()
                value.second.last().assertEqualTo(
                    ProductPreviewUiEvent.LoginUiEvent(
                        reviewData.reviewContent[reviewPosition].menus.copy(isReportable = false)
                    )
                )
            }
        }
    }

    @Test
    fun `when like from result and is from double tap and user not login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isDoubleTap = true
        val isLogin = false
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordEvent {
                robot.likeFromResultTestCase(isDoubleTap)
            }.also { event ->
                event.last()
                    .assertEqualTo(ProductPreviewUiEvent.LoginUiEvent(reviewData.reviewContent[reviewPosition].likeState))
            }
        }
    }

    @Test
    fun `when like from result and is from double tap and user is login`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isDoubleTap = true
        val isLogin = true
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            coEvery {
                mockRepository.likeReview(
                    reviewData.reviewContent[reviewPosition].likeState,
                    any()
                )
            } returns reviewData.reviewContent[reviewPosition].likeState.copy(withAnimation = true)

            robot.recordState {
                robot.likeFromResultTestCase(isDoubleTap)
            }.also { state ->
                state.reviewUiModel.reviewContent.mapIndexed { index, reviewContentUiModel ->
                    if (index == reviewPosition) reviewContentUiModel.likeState.withAnimation.assertEqualTo(
                        isDoubleTap
                    )
                }
            }
        }
    }

    @Test
    fun `when like from result and fail`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        val isDoubleTap = true
        val isLogin = true
        var reviewPosition: Int

        coEvery { mockUserSession.isLoggedIn } returns isLogin
        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            coEvery {
                mockRepository.likeReview(
                    reviewData.reviewContent[reviewPosition].likeState,
                    any()
                )
            } throws Throwable("fail fetch")

            robot.recordEvent {
                robot.likeFromResultTestCase(isDoubleTap)
            }.also { event ->
                event.last().assertType<ProductPreviewUiEvent.ShowErrorToaster>()
                (event.last() as ProductPreviewUiEvent.ShowErrorToaster).onClick()
            }
        }
    }

    @Test
    fun `when review watch mode updated`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)
        val reviewData = mockDataSource.mockReviewData()
        var reviewPosition: Int

        coEvery { mockRepository.getReview(productId, 1) } returns reviewData

        getRobot(sourceModel).use { robot ->
            reviewPosition = robot._reviewPosition.value
            robot.recordState {
                robot.reviewWatchModeTestCase()
            }.also { state ->
                state.reviewUiModel.reviewContent.mapIndexed { index, reviewContentUiModel ->
                    reviewContentUiModel.isWatchMode.assertEqualTo(index == reviewPosition)
                }
            }
        }
    }

    @Test
    fun `when has visit coach mark updated`() {
        val sourceModel = mockDataSource.mockSourceProduct(productId)

        coEvery { mockSharedPref.hasVisited() } returns true

        getRobot(sourceModel).use { robot ->
            robot.hasVisitCoachMarkTestCase()
            robot.hasVisit.assertTrue()
        }
    }

}
