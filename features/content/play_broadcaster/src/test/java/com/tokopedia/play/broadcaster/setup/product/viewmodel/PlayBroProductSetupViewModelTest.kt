package com.tokopedia.play.broadcaster.setup.product.viewmodel

import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.setup.product.model.ProductTagSummaryUiModel
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class PlayBroProductSetupViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockHydraConfigStore: HydraConfigStore = mockk(relaxed = true)

    /** Mock Response */
    private val mockCampaign = CampaignUiModel("1", "Campaign 1", "", "", "", CampaignStatusUiModel(CampaignStatus.Ongoing, "Berlangsung"), 1)
    private val mockEtalase = EtalaseUiModel("1", "", "Etalase 1", 1)
    private val mockCampaignList = List(5) {
        CampaignUiModel("$it", "Campaign $it", "", "", "", CampaignStatusUiModel(CampaignStatus.Ongoing, "Berlangsung"), it)
    }
    private val mockEtalaseList = List(5) {
        EtalaseUiModel("$it", "", "Etalase $it", it)
    }

    private val mockProduct = ProductUiModel("1", "Product 1", "", 10, OriginalPrice("Rp 12.000", 12000.0))
    private val sectionSize = 5
    private val productSizePerSection = 3
    private val mockProductTagSectionList = List(sectionSize) { sectionIdx ->
        ProductTagSectionUiModel("Test 1", CampaignStatus.Ongoing, List(productSizePerSection) { productIdx ->
            val idx = productSizePerSection * sectionIdx + productIdx
            ProductUiModel("$idx", "Product $idx", "", 10, OriginalPrice("Rp 12.000", 12000.0))
        })
    }
    private val mockProductCount = mockProductTagSectionList.sumOf { it.products.size }

    private val exception = Exception("Network Error")

    /** Campaign & Etalase */
    @Test
    fun `when user firstly create viewmodel, it will emit uiState with campaign and etalase list`() {

        coEvery { mockRepo.getEtalaseList() } returns mockEtalaseList
        coEvery { mockRepo.getCampaignList() } returns mockCampaignList

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = it.recordState {  }
            val campaignAndEtalase = state.campaignAndEtalase
            campaignAndEtalase.etalaseList.assertEqualTo(mockEtalaseList)
            campaignAndEtalase.campaignList.assertEqualTo(mockCampaignList)
        }
    }

    @Test
    fun `when user set sorting, it will emit uiState with new loadParam`() {
        val mockSort = SortUiModel(1, "Terbaru")

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetSort(mockSort))
            }

            assertEquals(state.loadParam.sort, mockSort)
        }
    }

    @Test
    fun `when user search product with keyword, it should emit uiState with newest keyword`() {
        val keyword = "piring cantik"

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SearchProduct(keyword))
            }

            assertEquals(state.loadParam.keyword, keyword)
        }
    }

    @Test
    fun `when user select etalase, it should emit uiState with new selected etalase`() {
        val mockSelectedEtalase = SelectedEtalaseModel.Etalase(mockEtalase)

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectEtalase(mockEtalase))
            }

            assertEquals(state.loadParam.etalase, mockSelectedEtalase)
        }
    }

    @Test
    fun `when user select campaign, it should emit uiState with new selected campaign`() {
        val mockSelectedCampaign = SelectedEtalaseModel.Campaign(mockCampaign)

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectCampaign(mockCampaign))
            }

            assertEquals(state.loadParam.etalase, mockSelectedCampaign)
        }
    }

    @Test
    fun `when user select product, it should emit uiState with new selected products`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", "", 10, OriginalPrice("Rp 12.000", 12000.0))
        val mockNewProductTagSectionList = mockProductTagSectionList.toMutableList()
        val mockNewProductList = mockNewProductTagSectionList.last().products.toMutableList()
        val mockSection = mockNewProductTagSectionList.last()

        mockNewProductTagSectionList.remove(mockSection)
        mockNewProductTagSectionList.add(mockSection.copy(products = mockNewProductList + mockAddedProduct))

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectProduct(mockAddedProduct))
            }

            assertEquals(state.selectedProductSectionList, mockNewProductTagSectionList)
        }
    }

    @Test
    fun `when user select product but exceed the max product allowed, it shouldnt change the selected products`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", "", 10, OriginalPrice("Rp 12.000", 12000.0))

        coEvery { mockHydraConfigStore.getMaxProduct() } returns sectionSize * productSizePerSection

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectProduct(mockAddedProduct))
            }

            assertEquals(state.selectedProductSectionList, mockProductTagSectionList)
        }
    }

    @Test
    fun `when user unselect product, it should emit uiState with fewer selected product`() {
        val mockUnselectedProduct = mockProductTagSectionList.last().products.last()

        val mockNewProductTagSectionList = mockProductTagSectionList.toMutableList()
        val mockSection = mockProductTagSectionList.last()
        val mockProductList = mockSection.products.toMutableList()
        mockProductList.removeLast()

        mockNewProductTagSectionList.remove(mockSection)
        mockNewProductTagSectionList.add(mockSection.copy(products = mockProductList))

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectProduct(mockUnselectedProduct))
            }

            assertEquals(state.selectedProductSectionList, mockNewProductTagSectionList)
        }
    }

    @Test
    fun `when user select product, it should emit saveState with canSave is true`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", "", 10, OriginalPrice("Rp 12.000", 12000.0))
        val mockNewProductTagSectionList = mockProductTagSectionList.toMutableList()
        val mockNewProductList = mockNewProductTagSectionList.last().products.toMutableList()
        val mockSection = mockNewProductTagSectionList.last()

        mockNewProductTagSectionList.remove(mockSection)
        mockNewProductTagSectionList.add(mockSection.copy(products = mockNewProductList + mockAddedProduct))

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectProduct(mockAddedProduct))
            }

            assertEquals(state.saveState.canSave, true)
        }
    }

    @Test
    fun `when user unselect all product, it should emit saveState with canSave is false`() {
        val mockProduct = ProductUiModel("1", "", "", 1, OriginalPrice("Rp 12.000", 12_000.0))
        val mockInitialProductTagSectionList = listOf(
            ProductTagSectionUiModel(
                name = "Test 1",
                campaignStatus = CampaignStatus.Ongoing,
                products = listOf(mockProduct)
            )
        )

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
            }

            assertEquals(state.saveState.canSave, false)
        }
    }

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
            hydraConfigStore = mockHydraConfigStore,
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
            hydraConfigStore = mockHydraConfigStore,
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
            hydraConfigStore = mockHydraConfigStore,
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

        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.LoadProductList(""))
            }

            state.focusedProductList.apply {
                productList.assertEqualTo(emptyList())
                resultState.assertEqualTo(PageResultState.Fail(exception))
                page.assertEqualTo(0)
            }
        }
    }

    @Test
    fun `when user wants to save products and success, it should trigger trigger event success`() {
        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
            }

            val (state, event) = robot.recordStateAsListAndEvent {
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state[1].saveState.isLoading.assertEqualTo(true)
            state[2].saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(PlayBroProductChooserEvent.SaveProductSuccess)
        }
    }

    @Test
    fun `when user wants to save products and failed, it should trigger trigger event error`() {

        coEvery { mockRepo.setProductTags(any(), any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            runBlockingTest {
                robot.submitAction(ProductSetupAction.SelectProduct(mockProduct))
            }

            val (state, event) = robot.recordStateAsListAndEvent {
                robot.submitAction(ProductSetupAction.SaveProducts)
            }

            state[1].saveState.isLoading.assertEqualTo(true)
            state[2].saveState.isLoading.assertEqualTo(false)
            event.last().assertEqualTo(PlayBroProductChooserEvent.ShowError(exception))
        }
    }

    /** Summary Page */
    @Test
    fun `when user successfully load product section, it should emit success state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordSummaryState{
                robot.submitAction(ProductSetupAction.LoadProductSummary)
            }

            state.productCount.assertEqualTo(mockProductCount)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSectionList)
        }
    }

    @Test
    fun `when user failed load product section, it should emit error state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.LoadProductSummary)
            }

            state.productCount.assertEqualTo(0)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            state.productTagSectionList.assertEqualTo(emptyList())
            assertTrue(event[0] is PlayBroProductChooserEvent.GetDataError)
        }
    }

    @Test
    fun `when user successfully delete product, it should emit success state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList
        coEvery { mockRepo.setProductTags(any(), any()) } returns Unit

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productCount.assertEqualTo(mockProductCount)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSectionList)
            event[0].assertEqualTo(PlayBroProductChooserEvent.DeleteProductSuccess(1))
        }
    }

    @Test
    fun `when user failed delete product, it should emit fail state`() {

        coEvery { mockRepo.setProductTags(any(), any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSectionList[0].products[0]))
            }

            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            assertTrue(event[0] is PlayBroProductChooserEvent.DeleteProductError)
        }
    }
}