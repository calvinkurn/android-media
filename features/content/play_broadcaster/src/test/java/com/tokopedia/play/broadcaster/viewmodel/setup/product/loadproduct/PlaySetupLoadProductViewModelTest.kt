package com.tokopedia.play.broadcaster.viewmodel.setup.product.loadproduct

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.ui.model.PagingType
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class PlaySetupLoadProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockCampaign = productSetupUiModelBuilder.buildCampaignModel()

    private val mockProduct = productSetupUiModelBuilder.buildProductUiModel()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockException = uiModelBuilder.buildException()

    private val mockCursor = "asdfasdf"

    @Test
    fun `when user change keyword, it should trigger load product and emit new product list`() {
        val keyword = mockProduct.name

        val mockEtalasePagedDataResponse = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = false,
            cursor = "",
        )

        coEvery { mockRepo.getProductsInEtalase(any(), any(), keyword, any()) } returns mockEtalasePagedDataResponse

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SearchProduct(keyword))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(mockEtalasePagedDataResponse.dataList)
                resultState.assertEqualTo(PageResultState.Success(false))
                assertPagingCursor(pagingType, "")
            }
        }
    }

    @Test
    fun `when user search product and wants load more product, it should trigger load product and emit more product list`() {
        val keyword = mockProduct.name

        val mockEtalasePagedDataResponseWhenSearch = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = true,
            cursor = mockCursor,
        )

        val mockEtalasePagedDataResponseWhenScrollDown = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = false,
            cursor = "",
        )

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            coEvery { mockRepo.getProductsInEtalase(any(), any(), keyword, any()) } returns mockEtalasePagedDataResponseWhenSearch

            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SearchProduct(keyword))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(mockEtalasePagedDataResponseWhenSearch.dataList)
                resultState.assertEqualTo(PageResultState.Success(true))
                assertPagingCursor(pagingType, mockCursor)
            }

            coEvery { mockRepo.getProductsInEtalase(any(), any(), keyword, any()) } returns mockEtalasePagedDataResponseWhenScrollDown

            val stateAfterScrollDown = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(keyword))
            }

            stateAfterScrollDown.focusedProductList.apply {
                productList.assertEqualTo(mockEtalasePagedDataResponseWhenSearch.dataList + mockEtalasePagedDataResponseWhenScrollDown.dataList)
                resultState.assertEqualTo(PageResultState.Success(false))
                assertPagingCursor(pagingType, "")
            }
        }
    }

    @Test
    fun `when user select campaign and wants load more product, it should trigger load product and emit more product list`() {
        val mockCampaignPagedDataResponseWhenSelectCampaign = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = true,
        )

        val mockCampaignPagedDataResponseWhenScrollDown = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = false,
        )

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            coEvery { mockRepo.getProductsInCampaign(any(), any()) } returns mockCampaignPagedDataResponseWhenSelectCampaign

            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectCampaign(mockCampaign))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(mockCampaignPagedDataResponseWhenSelectCampaign.dataList)
                resultState.assertEqualTo(PageResultState.Success(true))
                assertPagingPage(pagingType, 1)
            }

            coEvery { mockRepo.getProductsInCampaign(any(), any()) } returns mockCampaignPagedDataResponseWhenScrollDown

            val stateAfterScrollDown = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(""))
            }

            stateAfterScrollDown.focusedProductList.apply {
                productList.assertEqualTo(mockCampaignPagedDataResponseWhenSelectCampaign.dataList + mockCampaignPagedDataResponseWhenScrollDown.dataList)
                resultState.assertEqualTo(PageResultState.Success(false))
                assertPagingPage(pagingType, 2)
            }
        }
    }

    @Test
    fun `when user wants load more etalase product and error happens, it should trigger error state`() {

        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } throws mockException

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(""))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(emptyList())
                resultState.assertEqualTo(PageResultState.Fail(mockException))
                pagingType.assertType<PagingType.Unknown>()
            }
        }
    }

    private fun assertPagingPage(pagingType: PagingType, page: Int) {
        pagingType.assertType<PagingType.Page>()
        when(pagingType) {
            is PagingType.Page -> {
                pagingType.page.assertEqualTo(page)
            }
            else -> fail("cursor should be PagingType.Page")
        }
    }

    private fun assertPagingCursor(pagingType: PagingType, cursor: String) {
        pagingType.assertType<PagingType.Cursor>()
        when(pagingType) {
            is PagingType.Cursor -> {
                pagingType.cursor.assertEqualTo(cursor)
            }
            else -> fail("cursor should be PagingType.Cursor")
        }
    }
}
