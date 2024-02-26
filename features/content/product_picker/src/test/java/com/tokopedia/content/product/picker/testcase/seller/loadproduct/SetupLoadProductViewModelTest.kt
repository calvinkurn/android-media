package com.tokopedia.content.product.picker.testcase.seller.loadproduct

import com.tokopedia.content.product.picker.builder.seller.CommonUiModelBuilder
import com.tokopedia.content.product.picker.builder.seller.ProductSetupUiModelBuilder
import com.tokopedia.content.product.picker.robot.ContentProductPickerSellerViewModelRobot
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.PagingType
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.result.PageResultState
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.content.product.picker.util.assertType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class SetupLoadProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val commonUiModelBuilder = CommonUiModelBuilder()

    private val mockCampaign = productSetupUiModelBuilder.buildCampaignModel()

    private val mockProduct = productSetupUiModelBuilder.buildProductUiModel()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockException = commonUiModelBuilder.buildException()

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

        val robot = ContentProductPickerSellerViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
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

        val robot = ContentProductPickerSellerViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
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

        val robot = ContentProductPickerSellerViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo,
            commonRepo = mockCommonRepo,
        )

        robot.use {
            coEvery { mockCommonRepo.getProductsInCampaign(any(), any()) } returns mockCampaignPagedDataResponseWhenSelectCampaign

            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectCampaign(mockCampaign))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(mockCampaignPagedDataResponseWhenSelectCampaign.dataList)
                resultState.assertEqualTo(PageResultState.Success(true))
                assertPagingPage(pagingType, 1)
            }

            coEvery { mockCommonRepo.getProductsInCampaign(any(), any()) } returns mockCampaignPagedDataResponseWhenScrollDown

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

        val robot = ContentProductPickerSellerViewModelRobot(
            productSectionList = mockProductTagSectionList,
            dispatchers = testDispatcher,
            repo = mockRepo
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
