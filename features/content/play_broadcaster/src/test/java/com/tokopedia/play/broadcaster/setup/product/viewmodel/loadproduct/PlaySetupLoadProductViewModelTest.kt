package com.tokopedia.play.broadcaster.setup.product.viewmodel.loadproduct

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun `when user change keyword, it should trigger load product and emit new product list`() {
        val keyword = mockProduct.name

        val mockEtalasePagedDataResponse = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = false,
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
                page.assertEqualTo(1)
            }
        }
    }

    @Test
    fun `when user search product and wants load more product, it should trigger load product and emit more product list`() {
        val keyword = mockProduct.name

        val mockEtalasePagedDataResponseWhenSearch = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = true,
        )

        val mockEtalasePagedDataResponseWhenScrollDown = PagedDataUiModel(
            dataList = listOf(mockProduct),
            hasNextPage = false,
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
                page.assertEqualTo(1)
            }

            coEvery { mockRepo.getProductsInEtalase(any(), any(), keyword, any()) } returns mockEtalasePagedDataResponseWhenScrollDown

            val stateAfterScrollDown = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(keyword))
            }

            stateAfterScrollDown.focusedProductList.apply {
                productList.assertEqualTo(mockEtalasePagedDataResponseWhenSearch.dataList + mockEtalasePagedDataResponseWhenScrollDown.dataList)
                resultState.assertEqualTo(PageResultState.Success(false))
                page.assertEqualTo(2)
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
                page.assertEqualTo(1)
            }

            coEvery { mockRepo.getProductsInCampaign(any(), any()) } returns mockCampaignPagedDataResponseWhenScrollDown

            val stateAfterScrollDown = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(""))
            }

            stateAfterScrollDown.focusedProductList.apply {
                productList.assertEqualTo(mockCampaignPagedDataResponseWhenSelectCampaign.dataList + mockCampaignPagedDataResponseWhenScrollDown.dataList)
                resultState.assertEqualTo(PageResultState.Success(false))
                page.assertEqualTo(2)
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
                page.assertEqualTo(0)
            }
        }
    }
}