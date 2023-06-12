package com.tokopedia.play.broadcaster.viewmodel.setup.product.selectproduct

import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
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
    private val mockMaxProduct = 30

    @Test
    fun `when user select product, it should emit uiState with new selected products`() {
        val mockAddedProduct = ProductUiModel("100", "Product 100", false, "", 0, false, "", 10, OriginalPrice("Rp 12.000", 12000.0), PinProductUiModel.Empty, "")

        val expectedSelectedProducts = mockSelectedProducts.toMutableList()
        expectedSelectedProducts.add(mockAddedProduct)

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            maxProduct = mockMaxProduct,
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
        val mockAddedProduct = ProductUiModel("100", "Product 100", false, "", 0, false,"", 10, OriginalPrice("Rp 12.000", 12000.0), PinProductUiModel.Empty, "")

        val expectedSelectedProducts = mockSelectedProducts.toMutableList()

        coEvery { mockHydraConfigStore.getMaxProduct() } returns mockSelectedProducts.size

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            maxProduct = mockSelectedProducts.size,
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

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            maxProduct = mockMaxProduct,
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
        val mockAddedProduct = ProductUiModel("100", "Product 100", false, "", 0, false,"", 10, OriginalPrice("Rp 12.000", 12000.0), PinProductUiModel.Empty, "")
        val mockNewProductTagSectionList = mockProductTagSectionList.toMutableList()
        val mockNewProductList = mockNewProductTagSectionList.last().products.toMutableList()
        val mockSection = mockNewProductTagSectionList.last()

        mockNewProductTagSectionList.remove(mockSection)
        mockNewProductTagSectionList.add(mockSection.copy(products = mockNewProductList + mockAddedProduct))

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockProductTagSectionList,
            maxProduct = mockMaxProduct,
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
        val mockProduct = ProductUiModel("1", "", false, "", 0, false,"", 1, OriginalPrice("Rp 12.000", 12_000.0), PinProductUiModel.Empty, "")
        val mockInitialProductTagSectionList = listOf(
            ProductTagSectionUiModel(
                name = "Test 1",
                campaignStatus = CampaignStatus.Ongoing,
                products = listOf(mockProduct)
            )
        )

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            maxProduct = mockMaxProduct,
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
                false,
                "",
                0,
                false,
                "",
                1,
                OriginalPrice("Rp 12.000", 12_000.0),
                PinProductUiModel.Empty,
                ""
            )
        }
        val mockInitialProductTagSectionList = emptyList<ProductTagSectionUiModel>()

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            maxProduct = mockMaxProduct,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetProducts(mockProducts))
            }

            state.selectedProductList
                .assertEqualTo(mockProducts)
        }
    }

    @Test
    fun `when user set several products at once, old products should be overridden and products state should be those new set products`() {
        val mockProducts = List(5) {
            ProductUiModel(
                it.toString(),
                "",
                false,
                "",
                0,
                false,
                "",
                1,
                OriginalPrice("Rp 12.000", 12_000.0),
                PinProductUiModel.Empty,
                "",
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
                        false,
                        "",
                        0,
                        false,
                        "",
                        1,
                        OriginalPrice("Rp 12.000", 12_000.0),
                        PinProductUiModel.Empty,
                        ""
                    )
                }
            )
        )

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            maxProduct = mockMaxProduct,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetProducts(mockProducts))
            }

            state.selectedProductList
                .assertEqualTo(mockProducts)
        }
    }

    @Test
    fun `when user set several products at once more than the maximum, products count should be at most the maximum products allowed`() {
        val mockProducts = List(40) {
            ProductUiModel(
                it.toString(),
                "",
                false,
                "",
                0,
                false,
                "",
                1,
                OriginalPrice("Rp 12.000", 12_000.0),
                PinProductUiModel.Empty,
                ""
            )
        }
        val mockInitialProductTagSectionList = emptyList<ProductTagSectionUiModel>()

        val robot = PlayBroProductSetupViewModelRobot(
            productSectionList = mockInitialProductTagSectionList,
            maxProduct = mockMaxProduct,
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetProducts(mockProducts))
            }

            state.selectedProductList
                .assertEqualTo(mockProducts.subList(0, mockMaxProduct))
        }
    }
}
