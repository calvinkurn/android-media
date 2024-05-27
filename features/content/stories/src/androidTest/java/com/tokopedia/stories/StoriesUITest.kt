package com.tokopedia.stories

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.stories.data.mock.mockInitialDataModel
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.robot.StoriesRobotUITest
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.utils.containsEventAction
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class StoriesUITest {

    private val args: StoriesArgsModel = StoriesArgsModel(
        authorId = "123",
        authorType = "shop",
        source = "shop-entrypoint",
        sourceId = "123",
    )
    private val repository: StoriesRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val storiesPref: StoriesPreference = mockk(relaxed = true)

    private fun getStoriesRobot() = StoriesRobotUITest(
        args = args,
        repository = repository,
        userSession = userSession,
        storiesPref = storiesPref,
    )

    @Before
    fun setUp() {
        coEvery { storiesPref.hasVisit() } returns true
    }

    @Test
    fun testAnalytic_storiesRoom() {
        val mockData = mockInitialDataModel()
        val duration = mockData.groupItems.first().detail.detailItems.first().content.duration

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .assertEventAction("view - story circle")

            .doNothingUntilNextGroup(duration)
            .assertEventAction("click - move to other category")

            .tapNext()
            .assertEventAction("click - tap next content")

            .tapPrev()
            .assertEventAction("click - tap previous content")

            .tapGroup()
            .assertEventAction("click - story circle")

            .moveToDestroyState()
            .assertEventAction("click - exit story room")
    }

    @Test
    fun test_holdToPauseAndResume() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .holdToPauseAndResume()
    }
}
