package com.tokopedia.play.broadcaster.setup.product.viewmodel

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
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
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

    /** Mock Response */
    private val mockCampaign = CampaignUiModel("1", "Campaign 1", "", "", "", CampaignStatusUiModel(CampaignStatus.Ongoing, "Berlangsung"), 1)
    private val mockEtalase = EtalaseUiModel("1", "", "Etalase 1", 1)
    private val mockCampaignList = List(5) {
        CampaignUiModel("$it", "Campaign $it", "", "", "", CampaignStatusUiModel(CampaignStatus.Ongoing, "Berlangsung"), it)
    }
    private val mockEtalaseList = List(5) {
        EtalaseUiModel("$it", "", "Etalase $it", it)
    }

    private val mockProductTagSection = List(5) {
        ProductTagSectionUiModel("Test 1", CampaignStatus.Ongoing, List(3) {
            ProductUiModel("$it", "Product 1", "", 10, OriginalPrice("Rp 12.000", 12000.0))
        })
    }
    private val mockProductCount = mockProductTagSection.sumOf { it.products.size }

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

    /** Summary Page */
    @Test
    fun `when user successfully load product section, it should emit success state`() {

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSection

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
            state.productTagSectionList.assertEqualTo(mockProductTagSection)
        }
    }

    @Test
    fun `when user failed load product section, it should emit error state`() {

        val exception = Exception("Network Error")
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

        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSection
        coEvery { mockRepo.setProductTags(any(), any()) } returns Unit

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSection[0].products[0]))
            }

            state.productCount.assertEqualTo(mockProductCount)
            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Success)
            state.productTagSectionList.assertEqualTo(mockProductTagSection)
            event[0].assertEqualTo(PlayBroProductChooserEvent.DeleteProductSuccess(1))
        }
    }

    @Test
    fun `when user failed delete product, it should emit fail state`() {

        val exception = Exception("Network Error")
        coEvery { mockRepo.setProductTags(any(), any()) } throws exception

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val (state, event) = robot.recordSummaryStateAndEvent{
                robot.submitAction(ProductSetupAction.DeleteSelectedProduct(mockProductTagSection[0].products[0]))
            }

            state.productTagSummary.assertEqualTo(ProductTagSummaryUiModel.Unknown)
            assertTrue(event[0] is PlayBroProductChooserEvent.DeleteProductError)
        }
    }
}