package com.tokopedia.stories

import androidx.lifecycle.SavedStateHandle
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.stories.data.mock.mockInitialDataModel
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.robot.StoriesRobotUITest
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
class StoriesRoomUITest {

    private val authorId: String = "123"
    private val handle: SavedStateHandle = SavedStateHandle()
    private val repository: StoriesRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private fun getStoriesRobot() = StoriesRobotUITest(
        authorId = authorId,
        handle = handle,
        repository = repository,
        userSession = userSession,
    )

    @Test
    fun test_openStoriesRoom() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
    }

    @Test
    fun test_doNothingUntilNextGroup() {
        val mockData = mockInitialDataModel()
        val duration = mockData.groupItems.first().detail.detailItems.first().content.duration

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .doNothingUntilNextGroup(duration)
    }

    @Test
    fun test_tapNextUntilNextGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapNextUntilNextGroup()
    }

    @Test
    fun test_tapPrevUntilPrevGroup() {
        val mockData = mockInitialDataModel(
            selectedGroup = 1,
            selectedDetail = 2,
        )

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapPrevUntilPrevGroup()
    }

    @Test
    fun test_holdToPauseAndResume() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .holdToPauseAndResume()
    }

    @Test
    fun test_tapGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapGroup()
    }

    @Test
    fun test_swipeGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .swipeGroup()
    }

}
