package com.tokopedia.stories.robot

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.stories.analytics.StoriesAnalytics
import com.tokopedia.stories.analytics.StoriesRoomAnalytic
import com.tokopedia.stories.analytics.StoriesRoomAnalyticImpl
import com.tokopedia.stories.analytics.StoriesSharingAnalytics
import com.tokopedia.stories.analytics.StoriesSharingAnalyticsImpl
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.factory.StoriesFragmentFactoryUITest
import com.tokopedia.stories.utils.StoriesPreference
import com.tokopedia.stories.utils.containsEventAction
import com.tokopedia.stories.utils.delay
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.utils.KEY_ARGS
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import com.tokopedia.empty_state.R as empty_stateR
import com.tokopedia.stories.R as storiesR

internal class StoriesRobotUITest(
    private val args: StoriesArgsModel,
    private val repository: StoriesRepository,
    private val userSession: UserSessionInterface,
    private val storiesPref: StoriesPreference,
) {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticStoriesMainTracker = "tracker/content/stories/stories_main_tracker.json"

    private val viewModel: StoriesViewModel by lazy {
        StoriesViewModel(
            args = args,
            repository = repository,
            userSession = userSession,
            sharedPref = storiesPref,
        )
    }

    private val viewModelFactory = object : StoriesViewModelFactory.Creator {
        override fun create(
            args: StoriesArgsModel,
        ): StoriesViewModelFactory {
            return StoriesViewModelFactory(
                args = args,
                factory = object : StoriesViewModel.Factory {
                    override fun create(
                        args: StoriesArgsModel,
                    ): StoriesViewModel {
                        return viewModel
                    }
                })
        }
    }

    private val storiesRoomAnalytic = StoriesRoomAnalyticImpl(
        args = args,
        userSession = userSession,
    )

    private val storiesSharingAnalytic = StoriesSharingAnalyticsImpl(
        shopId = args.authorId,
        userSession = userSession,
    )

    private val storiesAnalyticFactory = object : StoriesAnalytics.Factory {
        override fun create(args: StoriesArgsModel): StoriesAnalytics {
            return StoriesAnalytics(
                args = args,
                storiesRoomAnalytic = object : StoriesRoomAnalytic.Factory {
                    override fun create(args: StoriesArgsModel): StoriesRoomAnalytic {
                        return storiesRoomAnalytic
                    }
                },
                sharingAnalytics = object : StoriesSharingAnalytics.Factory {
                    override fun create(shopId: String): StoriesSharingAnalytics {
                        return storiesSharingAnalytic
                    }
                }
            )
        }
    }

    private val fragmentFactory = StoriesFragmentFactoryUITest(
        mapOf(
            StoriesGroupFragment::class.java to {
                StoriesGroupFragment(
                    viewModelFactory = viewModelFactory,
                    analyticFactory = storiesAnalyticFactory,
                )
            },
            StoriesDetailFragment::class.java to {
                StoriesDetailFragment(
                    analyticFactory = storiesAnalyticFactory,
                    router = mockk(relaxed = true),
                )
            }
        )
    )

    private val scenario = launchFragmentInContainer<StoriesGroupFragment>(
        factory = fragmentFactory,
        themeResId = empty_stateR.style.AppTheme,
        fragmentArgs = Bundle().apply {
            putParcelable(KEY_ARGS, args)
        },
    )

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    fun moveToDestroyState() = chainable {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    fun openStoriesRoom() = chainable {
        delay(1000L)

        Espresso
            .onView(withId(storiesR.id.rv_stories_category))
            .check(matches(isDisplayed()))

        Espresso
            .onView(withId(storiesR.id.cv_stories_detail_timer))
            .check(matches(isDisplayed()))

        Espresso
            .onView(withId(storiesR.id.iv_stories_detail_content))
            .check(matches(isDisplayed()))
    }

    fun doNothingUntilNextGroup(duration: Int) = chainable {
        delay(duration.times(4L))
    }

    fun tapNext() = chainable {
        Espresso
            .onView(withId(storiesR.id.fl_stories_next))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun tapPrev() = chainable {
        Espresso
            .onView(withId(storiesR.id.fl_stories_prev))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun holdToPauseAndResume() = chainable {
        Espresso
            .onView(withId(storiesR.id.fl_stories_prev))
            .perform(longClick())
    }

    fun tapGroup() = chainable {
        Espresso.onView(withId(storiesR.id.rv_stories_category))
            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))
    }

    fun assertEventAction(eventAction: String) = chainable {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticStoriesMainTracker),
            containsEventAction(eventAction)
        )
    }

    private fun chainable(fn: () -> Unit): StoriesRobotUITest {
        fn()
        return this
    }

}
