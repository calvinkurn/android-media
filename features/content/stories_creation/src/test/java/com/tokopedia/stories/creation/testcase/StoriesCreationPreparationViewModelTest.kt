package com.tokopedia.stories.creation.testcase

import com.tokopedia.content.test.util.assertEmpty
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertType
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.upload.uploader.CreationUploader
import com.tokopedia.stories.creation.builder.AccountModelBuilder
import com.tokopedia.stories.creation.builder.CommonModelBuilder
import com.tokopedia.stories.creation.builder.ConfigurationModelBuilder
import com.tokopedia.stories.creation.builder.ProductModelBuilder
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.robot.StoriesCreationViewModelRobot
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration
import com.tokopedia.stories.creation.view.model.StoriesMedia
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.exception.NotEligibleException
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.assertj.core.api.Assertions.fail
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class StoriesCreationPreparationViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo = mockk<StoriesCreationRepository>(relaxed = true)
    private val mockCreationUploader = mockk<CreationUploader>(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val accountModelBuilder = AccountModelBuilder()
    private val configModelBuilder = ConfigurationModelBuilder()
    private val productModelBuilder = ProductModelBuilder()

    @Test
    fun `storiesCreation_prepare_success_noDraft`() {
        val mockStoryId = "jkadsjflk"
        val mockConfig = configModelBuilder.build(storiesId = "")
        val mockAccountList = accountModelBuilder.build()
        val mockAccountListWithoutUgc = mockAccountList.filter { it.isShop }

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig
        coEvery { mockRepo.createStory(any()) } returns mockStoryId

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(StoriesCreationAction.Prepare)
            }

            coVerify(exactly = 1) { mockRepo.getCreationAccountList() }
            coVerify(exactly = 1) { mockRepo.getStoryPreparationInfo(any()) }
            coVerify(exactly = 1) { mockRepo.createStory(any()) }

            robot.storyId.assertEqualTo(mockStoryId)
            robot.selectedAccount.assertEqualTo(mockAccountList[0])
            robot.maxProductTag.assertEqualTo(mockConfig.maxProductTag)
            state.config.storiesId.assertEqualTo(mockStoryId)
            state.accountList.assertEqualTo(mockAccountListWithoutUgc)

            events.last().assertEqualTo(StoriesCreationUiEvent.OpenMediaPicker)
        }
    }

    @Test
    fun `storiesCreation_prepare_success_withDraft`() {
        val mockConfig = configModelBuilder.build()
        val mockAccountList = accountModelBuilder.build()
        val mockAccountListWithoutUgc = mockAccountList.filter { it.isShop }

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(StoriesCreationAction.Prepare)
            }

            coVerify(exactly = 1) { mockRepo.getCreationAccountList() }
            coVerify(exactly = 1) { mockRepo.getStoryPreparationInfo(any()) }
            coVerify(exactly = 0) { mockRepo.createStory(any()) }

            robot.storyId.assertEqualTo(mockConfig.storiesId)
            robot.selectedAccount.assertEqualTo(mockAccountList[0])
            robot.maxProductTag.assertEqualTo(mockConfig.maxProductTag)
            state.config.assertEqualTo(mockConfig)
            state.accountList.assertEqualTo(mockAccountListWithoutUgc)

            events.last().assertEqualTo(StoriesCreationUiEvent.OpenMediaPicker)
        }
    }

    @Test
    fun `storiesCreation_prepare_error`() {

        val mockException = commonModelBuilder.buildException()

        coEvery { mockRepo.getCreationAccountList() } throws mockException

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(StoriesCreationAction.Prepare)
            }

            coVerify(exactly = 1) { mockRepo.getCreationAccountList() }
            coVerify(exactly = 0) { mockRepo.getStoryPreparationInfo(any()) }
            coVerify(exactly = 0) { mockRepo.createStory(any()) }

            state.accountList.assertEmpty()
            state.config.assertEqualTo(StoriesCreationConfiguration.Empty)

            events.last().assertEqualTo(StoriesCreationUiEvent.ErrorPreparePage(mockException))
        }
    }

    @Test
    fun `storiesCreation_prepare_accountNotEligible`() {
        val mockConfig = configModelBuilder.build()
        val mockAccountList = accountModelBuilder.build(tncShop = false)

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(StoriesCreationAction.Prepare)
            }

            coVerify(exactly = 1) { mockRepo.getCreationAccountList() }
            coVerify(exactly = 0) { mockRepo.getStoryPreparationInfo(any()) }
            coVerify(exactly = 0) { mockRepo.createStory(any()) }

            state.config.assertEqualTo(StoriesCreationConfiguration.Empty)
            state.accountList.assertEqualTo(emptyList())

            val lastEvent = events.last()

            if (lastEvent is StoriesCreationUiEvent.ErrorPreparePage) {
                lastEvent.throwable.assertType<NotEligibleException>()
            } else {
                fail("Event should be ErrorPreparePage")
            }
        }
    }

    @Test
    fun `storiesCreation_prepare_reachedMaxStoriesAllowed`() {
        val mockConfig = configModelBuilder.build(
            maxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig(
                isMaxStoryReached = true,
                imageUrl = "asdf",
                title = "asdf",
                description = "asdf",
                primaryText = "asdf",
                secondaryText = "asdf",
            )
        )
        val mockAccountList = accountModelBuilder.build()
        val mockAccountListWithoutUgc = mockAccountList.filter { it.isShop }

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(StoriesCreationAction.Prepare)
            }

            coVerify(exactly = 1) { mockRepo.getCreationAccountList() }
            coVerify(exactly = 1) { mockRepo.getStoryPreparationInfo(any()) }
            coVerify(exactly = 0) { mockRepo.createStory(any()) }

            robot.maxStoriesConfig.assertEqualTo(mockConfig.maxStoriesConfig)
            state.config.assertEqualTo(mockConfig)
            state.accountList.assertEqualTo(mockAccountListWithoutUgc)

            events.last().assertEqualTo(StoriesCreationUiEvent.ShowTooManyStoriesReminder)
        }
    }

    @Test
    fun `storiesCreation_setMedia`() {
        val mockMediaFilePath = "asdf"
        val mockMediaType = ContentMediaType.Video

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val state = it.recordState {
                submitAction(StoriesCreationAction.SetMedia(StoriesMedia(mockMediaFilePath, mockMediaType)))
            }

            state.media.filePath.assertEqualTo(mockMediaFilePath)
            state.media.type.assertEqualTo(mockMediaType)
        }
    }

    @Test
    fun `storiesCreation_setProduct`() {
        val mockProductList = productModelBuilder.buildProductTagSectionList()

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val state = it.recordState {
                submitAction(StoriesCreationAction.SetProduct(mockProductList))
            }

            robot.productTag.assertEqualTo(mockProductList)
            state.productTags.assertEqualTo(mockProductList)
        }
    }

    @Test
    fun `storiesCreation_clickUpload_success`() {
        val mockConfig = configModelBuilder.build()
        val mockAccountList = accountModelBuilder.build()
        val mockMediaFilePath = "asdf"
        val mockMediaType = ContentMediaType.Video

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig
        coEvery { mockRepo.updateStoryStatus(any(), any()) } returns Unit

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            it.recordState {
                submitAction(StoriesCreationAction.Prepare)
                submitAction(StoriesCreationAction.SetMedia(StoriesMedia(mockMediaFilePath, mockMediaType)))
                submitAction(StoriesCreationAction.ClickUpload)
            }

            coVerify(exactly = 1) { mockCreationUploader.upload(any()) }
        }
    }

    @Test
    fun `storiesCreation_clickUpload_updateChannelFaiked`() {
        val mockConfig = configModelBuilder.build()
        val mockAccountList = accountModelBuilder.build()
        val mockMediaFilePath = "asdf"
        val mockMediaType = ContentMediaType.Video

        coEvery { mockRepo.getCreationAccountList() } returns mockAccountList
        coEvery { mockRepo.getStoryPreparationInfo(any()) } returns mockConfig
        coEvery { mockRepo.updateStoryStatus(any(), any()) } throws Exception("Network Issue")

        val robot = StoriesCreationViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            creationUploader = mockCreationUploader,
        )

        robot.use {
            val events = it.recordEvent {
                submitAction(StoriesCreationAction.Prepare)
                submitAction(StoriesCreationAction.SetMedia(StoriesMedia(mockMediaFilePath, mockMediaType)))
                submitAction(StoriesCreationAction.ClickUpload)
            }

            coVerify(exactly = 0) { mockCreationUploader.upload(any()) }
            events.last().assertType<StoriesCreationUiEvent.ShowError>()
        }
    }
}
