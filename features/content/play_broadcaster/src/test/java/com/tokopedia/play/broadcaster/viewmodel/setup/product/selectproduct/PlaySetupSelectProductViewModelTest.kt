package com.tokopedia.play.broadcaster.viewmodel.setup.product.selectproduct

import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
class PlaySetupSelectProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockHydraConfigStore: HydraConfigStore = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    private val mockSelectedProducts = mockProductTagSectionList.flatMap { it.products }

    @Test
    fun `when user select product, it should emit uiState with new selected products`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", "", 10, OriginalPrice("Rp 12.000", 12000.0))

        val expectedSelectedProducts = mockSelectedProducts.toMutableList()
        expectedSelectedProducts.add(mockAddedProduct)

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockAddedProduct))
            }

            state.selectedProductList.assertEqualTo(expectedSelectedProducts)
        }
    }

    @Test
    fun `when user select product but exceed the max product allowed, it shouldnt change the selected products`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", "", 10, OriginalPrice("Rp 12.000", 12000.0))

        val expectedSelectedProducts = mockSelectedProducts.toMutableList()

        coEvery { mockHydraConfigStore.getMaxProduct() } returns mockSelectedProducts.size

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockAddedProduct))
            }

            state.selectedProductList
                .assertEqualTo(expectedSelectedProducts)
        }
    }

    @Test
    fun `when user unselect product, it should emit uiState with fewer selected product`() {
        val mockUnselectedProduct = mockSelectedProducts.last()

        val expectedSelectedProducts = mockSelectedProducts.toMutableList()
        expectedSelectedProducts.removeLast()

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockUnselectedProduct))
            }

            state.selectedProductList
                .assertEqualTo(expectedSelectedProducts)
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
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockAddedProduct))
            }

            Assertions.assertEquals(state.saveState.canSave, true)
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
                robot.submitAction(ProductSetupAction.ToggleSelectProduct(mockProduct))
            }

            Assertions.assertEquals(state.saveState.canSave, false)
        }
    }

    @Test
    fun `when user set several products at once, products state should be those same products`() {
        val mockProducts = List(5) {
            ProductUiModel(
                it.toString(),
                "",
                "",
                1,
                OriginalPrice("Rp 12.000", 12_000.0)
            )
        }
        val mockInitialProductTagSectionList = emptyList<ProductTagSectionUiModel>()

        coEvery { mockHydraConfigStore.getMaxProduct() } returns 30

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            hydraConfigStore = mockHydraConfigStore,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetProducts(mockProducts))
            }

            Assertions.assertEquals(state.selectedProductList, mockProducts)
        }
    }

    @Test
    fun `when user set several products at once, old products should be overridden and products state should be those new set products`() {
        val mockProducts = List(5) {
            ProductUiModel(
                it.toString(),
                "",
                "",
                1,
                OriginalPrice("Rp 12.000", 12_000.0)
            )
        }
        val mockInitialProductTagSectionList = listOf(
            ProductTagSectionUiModel(
                name = "Test 1",
                campaignStatus = CampaignStatus.Ongoing,
                products = List(10) {
                    ProductUiModel(
                        (it*5001).toString(),
                        "",
                        "",
                        1,
                        OriginalPrice("Rp 12.000", 12_000.0)
                    )
                }
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
                robot.submitAction(ProductSetupAction.SetProducts(mockProducts))
            }

            Assertions.assertEquals(state.selectedProductList, mockProducts)
        }
    }
}