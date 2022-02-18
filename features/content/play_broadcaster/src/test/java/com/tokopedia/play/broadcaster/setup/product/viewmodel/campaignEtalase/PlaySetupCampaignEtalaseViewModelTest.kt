package com.tokopedia.play.broadcaster.setup.product.viewmodel.campaignEtalase

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class PlaySetupCampaignEtalaseViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockCampaignList = productSetupUiModelBuilder.buildCampaignList()
    private val mockEtalaseList = productSetupUiModelBuilder.buildEtalaseList()

    private val mockException = uiModelBuilder.buildException()

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
    fun `when user firstly create viewmodel and theres an error when loading campaign and etalase list, it will emit uiState with empty campaign and etalase`() {

        coEvery { mockRepo.getEtalaseList() } throws mockException
        coEvery { mockRepo.getCampaignList() } throws mockException

        val robot = PlayBroProductSetupViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo
        )

        robot.use {
            val state = it.recordState {  }
            val campaignAndEtalase = state.campaignAndEtalase
            campaignAndEtalase.etalaseList.assertEqualTo(emptyList())
            campaignAndEtalase.campaignList.assertEqualTo(emptyList())
        }
    }
}