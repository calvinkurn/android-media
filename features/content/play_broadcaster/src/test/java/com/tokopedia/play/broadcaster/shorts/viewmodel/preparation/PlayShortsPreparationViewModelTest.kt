package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsPreparationViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()
    private val productModelBuilder = ProductSetupUiModelBuilder()

    private val mockTitle = "pokemon"
    private val mockProductTagList = productModelBuilder.buildProductTagSectionList()
    private val mockCover = uiModelBuilder.buildCoverSetupStateUploaded()
    private val mockCoverUIModel = PlayCoverUiModel(
        croppedCover = mockCover,
        state = SetupDataState.Draft
    )

    @Test
    fun playShorts_preparation_clickNext() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val events = it.recordEvent {
                submitAction(PlayShortsAction.ClickNext)
            }

            events.last().assertEqualTo(PlayShortsUiEvent.GoToSummary)
        }
    }

    @Test
    fun playShorts_preparation_check_isAllMandatoryMenuChecked() {
        coEvery { mockRepo.uploadTitle(any(), any(), any()) } returns Unit

        val robot = PlayShortsViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            it.isAllMandatoryMenuChecked.assertFalse()

            it.submitAction(PlayShortsAction.UploadTitle(mockTitle))

            it.isAllMandatoryMenuChecked.assertFalse()

            it.submitAction(PlayShortsAction.SetProduct(mockProductTagList))

            it.isAllMandatoryMenuChecked.assertTrue()
        }
    }

    @Test
    fun playShorts_preparation_check_isFormFilled() {
        coEvery {
            mockDataStore.getSetupDataStore().setFullCover(mockCoverUIModel)
        }
        coEvery {
            mockDataStore.getSetupDataStore().getSelectedCover()
        } returns mockCoverUIModel

        PlayShortsViewModelRobot(
            repo = mockRepo,
        ).use {
            it.isFormFilled.assertFalse()

            it.submitAction(PlayShortsAction.UploadTitle(mockTitle))

            it.isFormFilled.assertTrue()
        }

        PlayShortsViewModelRobot(
            repo = mockRepo
        ).use {
            it.isFormFilled.assertFalse()

            it.submitAction(PlayShortsAction.SetProduct(mockProductTagList))

            it.isFormFilled.assertTrue()
        }

        PlayShortsViewModelRobot(
            repo = mockRepo,
            dataStore = mockDataStore,
        ).use {
            it.isFormFilled.assertFalse()

            it.submitAction(PlayShortsAction.UpdateCover)

            it.isFormFilled.assertTrue()
        }
    }
}
