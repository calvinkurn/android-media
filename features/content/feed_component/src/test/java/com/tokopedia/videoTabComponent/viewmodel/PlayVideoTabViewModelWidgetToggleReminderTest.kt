package com.tokopedia.videoTabComponent.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videoTabComponent.domain.PlayVideoTabRepository
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetFeedReminderInfoData
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti agarwal on 24/11/22.
 */

class PlayVideoTabViewModelWidgetToggleReminderTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val playWidgetTools: PlayWidgetTools = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayVideoTabRepository = mockk(relaxed = true)
    private val viewModel = PlayFeedVideoTabViewModel(
        testDispatcher,
        mockRepo,
        playWidgetTools,
        userSession
    )

    private val fakeState = PlayWidgetState(
        model = PlayWidgetUiModel(
            "title",
            "action title",
            "applink",
            true,
            PlayWidgetConfigUiModel(
                true,
                1000,
                true,
                1,
                1,
                2,
                1
            ),
            PlayWidgetBackgroundUiModel("", "", "", listOf(), ""),
            listOf()
        ),
        isLoading = false
    )
    private val channelId = "123"

    @Test
    fun `play widget toggle reminder success when user is logged in`() {
        val position = 0
        val reminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedResult = PlayWidgetFeedReminderInfoData(channelId, reminderType, position)
        val expectedMapReminder = true

        coEvery {
            mockRepo.updateToggleReminder(channelId, reminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        viewModel.updatePlayWidgetToggleReminder(
            channelId,
            reminderType,
            position,
            true
        )

        val result = viewModel.reminderObservable.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `play widget toggle reminder fail cause by map reminder return false when logged in`() {
        val position = 0
        val reminderType = PlayWidgetReminderType.Reminded

        val expectedReminder = PlayWidgetReminder()
        val expectedMapReminder = false

        coEvery {
            mockRepo.updateToggleReminder(channelId, reminderType)
        } returns expectedReminder

        coEvery {
            playWidgetTools.mapWidgetToggleReminder(expectedReminder)
        } returns expectedMapReminder

        viewModel.updatePlayWidgetToggleReminder(
            channelId,
            reminderType,
            position,
            true
        )

        val result = viewModel.reminderObservable.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `play widget toggle reminder fail cause by exception when logged in`() {
        val reminderType = PlayWidgetReminderType.Reminded
        val position = 0

        val expectedThrowable = Throwable()

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                channelId,
                reminderType
            )
        } returns fakeState

        coEvery {
            mockRepo.updateToggleReminder(channelId, reminderType)
        } throws expectedThrowable

        coEvery {
            playWidgetTools.updateActionReminder(
                fakeState,
                channelId,
                reminderType.switch()
            )
        } returns fakeState

        viewModel.updatePlayWidgetToggleReminder(
            channelId,
            reminderType,
            position,
            true
        )

        val result = viewModel.reminderObservable.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `play widget redirects to login page if not logged in`() {
        val position = 0
        val reminderType = PlayWidgetReminderType.Reminded
        val expectedResult = PlayWidgetFeedReminderInfoData(channelId, reminderType, position)

        viewModel.updatePlayWidgetToggleReminder(
            channelId,
            reminderType,
            position,
            false
        )

        val result = viewModel.playWidgetReminderEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(expectedResult)
    }

    @Test
    fun `play widget redirects to login page with default login value`() {
        val position = 0
        val reminderType = PlayWidgetReminderType.Reminded
        val expectedResult = PlayWidgetFeedReminderInfoData(channelId, reminderType, position)

        viewModel.updatePlayWidgetToggleReminder(
            channelId,
            reminderType,
            position
        )

        val result = viewModel.playWidgetReminderEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(expectedResult)
    }
}
