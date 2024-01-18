package com.tokopedia.play.broadcaster.shorts.viewmodel.summary

import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUploadUiState
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on January 16, 2024
 */
class PlayShortsInterspersingViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockCreationUploader: CreationUploader = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val productModelBuilder = ProductSetupUiModelBuilder()

    private val mockConfigInterspersingAllowed = uiModelBuilder.buildShortsConfig(shortsAllowed = true)
    private val mockConfigInterspersingNotAllowed = uiModelBuilder.buildShortsConfig(shortsAllowed = true, eligibleInterspersing = false)
    private val mockTagsSize5 = uiModelBuilder.buildTags(size = 5)
    private val mockAccountShop = uiModelBuilder.buildAccountListModel(onlyShop = true).first()
    private val mockHasPdpVideo = uiModelBuilder.buildHasPdpVideo()
    private val mockHasNoPdpVideo = uiModelBuilder.buildHasNoPdpVideo()
    private val mockProducts = productModelBuilder.buildProductTagSectionList(sectionSize = 1, productSizePerSection = 1)
    private val mockException = Exception("Network Error")

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    @Test
    fun playShorts_summary_toggleInterspersing() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordState {
                submitAction(PlayShortsAction.SwitchInterspersing)
            }

            state.interspersingConfig.isInterspersing.assertFalse()

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SwitchInterspersing)
            }

            state2.interspersingConfig.isInterspersing.assertTrue()
        }
    }

    @Test
    fun playShorts_summary_clickVideoPreview() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val events = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordEvent {
                submitAction(PlayShortsAction.ClickVideoPreview)
            }

            events.last().assertType<PlayShortsUiEvent.GoToVideoPreview>()
        }
    }

    @Test
    fun playShorts_summary_checkPdpVideo_success() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.checkProductCustomVideo(any()) } returns mockHasPdpVideo

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
            }

            events.last().assertType<PlayShortsUiEvent.ShowInterspersingConfirmation>()
            state.uploadState.assertEqualTo(PlayShortsUploadUiState.Unknown)
            coVerify(exactly = 0) { mockCreationUploader.upload(any()) }
            coVerify(exactly = 0) { mockRepo.saveTag(any(), any()) }
        }
    }

    @Test
    fun playShorts_summary_checkPdpVideo_error() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.checkProductCustomVideo(any()) } throws mockException

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
            }

            events.last().assertType<PlayShortsUiEvent.ErrorCheckInterspersing>()
            state.uploadState.assertType<PlayShortsUploadUiState.Error>()
            coVerify(exactly = 0) { mockCreationUploader.upload(any()) }
            coVerify(exactly = 0) { mockRepo.saveTag(any(), any()) }
        }
    }

    @Test
    fun playShorts_summary_checkPdpVideo_noPdpVideo() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.checkProductCustomVideo(any()) } returns mockHasNoPdpVideo
        coEvery { mockRepo.saveTag(any(), any()) } returns true

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordState {
                submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
            }

            state.uploadState.assertEqualTo(PlayShortsUploadUiState.Success)
            coVerify(exactly = 1) { mockCreationUploader.upload(any()) }
        }
    }

    @Test
    fun playShorts_summary_checkPdpVideo_interspersingNotChecked() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.checkProductCustomVideo(any()) } returns mockHasPdpVideo
        coEvery { mockRepo.saveTag(any(), any()) } returns true

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val state = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
                submitAction(PlayShortsAction.SwitchInterspersing)
            }.recordState {
                submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
            }

            state.uploadState.assertEqualTo(PlayShortsUploadUiState.Success)
            coVerify(exactly = 0) { mockRepo.checkProductCustomVideo(any()) }
            coVerify(exactly = 1) { mockCreationUploader.upload(any()) }
        }
    }

    @Test
    fun playShorts_summary_checkPdpVideo_interspersingNotAllowed() {
        val mockSelectedIdxList = listOf(2, 4)

        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigInterspersingNotAllowed
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getTagRecommendation(any()) } returns mockTagsSize5
        coEvery { mockRepo.checkProductCustomVideo(any()) } returns mockHasNoPdpVideo
        coEvery { mockRepo.saveTag(any(), any()) } returns true

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SetProduct(mockProducts))
                submitAction(PlayShortsAction.ClickNext)
                submitAction(PlayShortsAction.LoadTag)
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.first()]))
                submitAction(PlayShortsAction.SelectTag(mockTagsSize5.tags.toList()[mockSelectedIdxList.last()]))
            }.recordStateAndEvent {
                submitAction(PlayShortsAction.UploadVideo(needCheckInterspersing = true))
            }

            state.uploadState.assertEqualTo(PlayShortsUploadUiState.Success)
            coVerify(exactly = 1) { mockCreationUploader.upload(any()) }
        }
    }
}
