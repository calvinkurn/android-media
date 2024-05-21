package com.tokopedia.stories

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.stories.data.mock.mockInitialDataModel
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.robot.StoriesRobotUITest
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.utils.containsEventAction
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
class StoriesUITest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticStoriesMainTracker = "tracker/content/stories/stories_main_tracker.json"

    private val args: StoriesArgsModel = StoriesArgsModel(
        authorId = "123",
        authorType = "shop",
        source = "shop-entrypoint",
        sourceId = "123",
    )
    private val repository: StoriesRepository = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val mockSharedPref: StoriesPreference = mockk(relaxed = true)

    private fun getStoriesRobot() = StoriesRobotUITest(
        args = args,
        repository = repository,
        userSession = userSession,
        sharedPref = mockSharedPref,
    )

    @Test
    fun test_openStoriesRoom() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .moveToDestroyState()

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("view - story circle")
        )

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - exit story room")
        )
    }

    @Test
    fun test_doNothingUntilNextGroup() {
        val mockData = mockInitialDataModel()
        val duration = mockData.groupItems.first().detail.detailItems.first().content.duration

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .doNothingUntilNextGroup(duration)

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - move to other category")
        )
    }

    @Test
    fun test_tapNextUntilNextGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapNextUntilNextGroup()

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - tap next content"),
        )

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - move to other category")
        )
    }

    @Test
    fun test_tapPrevUntilPrevGroup() {
        val mockData = mockInitialDataModel(
            selectedGroup = 1,
            selectedDetail = 2,
        )

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapPrevUntilPrevGroup()

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - tap previous content"),
        )

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - move to other category")
        )
    }

    @Test
    fun test_holdToPauseAndResume() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .holdToPauseAndResume()
    }

    @Test
    fun test_tapGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .tapGroup()

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - story circle"),
        )

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - move to other category")
        )
    }

    @Test
    fun test_swipeGroup() {
        val mockData = mockInitialDataModel()

        coEvery { repository.getStoriesInitialData(any(), any(), any(), any(), any(), any(), any()) } returns mockData

        getStoriesRobot()
            .openStoriesRoom()
            .swipeGroup()

        assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction("click - move to other category")
        )
    }

}
