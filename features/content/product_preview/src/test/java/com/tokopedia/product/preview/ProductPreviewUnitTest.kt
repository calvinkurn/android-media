package com.tokopedia.product.preview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.product.preview.data.mock.ProductPreviewMockData
import com.tokopedia.content.product.preview.data.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreference
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewUiEvent
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.test.util.assertEqualTo
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
        val sourceModel = mockDataSource.mockSourceProductWithNoReview(productId)

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
        val sourceModel = mockDataSource.mockSourceProductWithReview(productId)

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
        val sourceModel = mockDataSource.mockSourceProductWithReview(productId)
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
        val sourceModel = mockDataSource.mockSourceProductWithReview(productId)
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

}
