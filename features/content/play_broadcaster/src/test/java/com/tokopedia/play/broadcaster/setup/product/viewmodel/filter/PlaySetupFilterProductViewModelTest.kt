package com.tokopedia.play.broadcaster.setup.product.viewmodel.filter

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.ProductSetupAction
import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
internal class PlaySetupFilterProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockCampaign = productSetupUiModelBuilder.buildCampaignModel()
    private val mockEtalase = productSetupUiModelBuilder.buildEtalaseModel()

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
}