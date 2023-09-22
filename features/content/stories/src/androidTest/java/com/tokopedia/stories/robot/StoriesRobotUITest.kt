package com.tokopedia.stories.robot

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.stories.analytic.StoriesAnalytics
import com.tokopedia.stories.analytic.StoriesRoomAnalytic
import com.tokopedia.stories.analytic.StoriesRoomAnalyticImpl
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.factory.StoriesFragmentFactoryUITest
import com.tokopedia.stories.utils.delay
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import com.tokopedia.stories.view.model.StoriesArgsModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.StoriesViewModelFactory
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.empty_state.R as empty_stateR
import com.tokopedia.stories.R as storiesR

internal class StoriesRobotUITest(
    private val args: StoriesArgsModel,
    private val handle: SavedStateHandle,
    private val repository: StoriesRepository,
    userSession: UserSessionInterface,
) {

    private val viewModel: StoriesViewModel by lazy {
        StoriesViewModel(
            args = args,
            handle = handle,
            repository = repository,
        )
    }

    private val viewModelFactory = object : StoriesViewModelFactory.Creator {
        override fun create(
            activity: FragmentActivity,
            args: StoriesArgsModel,
        ): StoriesViewModelFactory {
            return StoriesViewModelFactory(
                activity = activity,
                args = args,
                factory = object : StoriesViewModel.Factory {
                    override fun create(
                        args: StoriesArgsModel,
                        handle: SavedStateHandle
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

    private val storiesAnalyticFactory = object : StoriesAnalytics.Factory {
        override fun create(args: StoriesArgsModel): StoriesAnalytics {
            return StoriesAnalytics(
                args = args,
                storiesRoomAnalytic = object : StoriesRoomAnalytic.Factory {
                    override fun create(args: StoriesArgsModel): StoriesRoomAnalytic {
                        return storiesRoomAnalytic
                    }
                })
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
                )
            }
        )
    )

    private val scenario = launchFragmentInContainer<StoriesGroupFragment>(
        factory = fragmentFactory,
        themeResId = empty_stateR.style.AppTheme,
    )

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    fun moveToDestroyState() = chainable {
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    fun openStoriesRoom() = chainable {
        Espresso
            .onView(withId(storiesR.id.rv_stories_category))
            .check(matches(isCompletelyDisplayed()))

        Espresso
            .onView(withId(storiesR.id.cv_stories_detail_timer))
            .check(matches(isCompletelyDisplayed()))

        Espresso
            .onView(withId(storiesR.id.iv_stories_detail_content))
            .check(matches(isCompletelyDisplayed()))
    }

    fun doNothingUntilNextGroup(duration: Int) = chainable {
        delay(duration.times(3L))
        delay()
    }

    fun tapNextUntilNextGroup() = chainable {
        Espresso
            .onView(withId(storiesR.id.fl_stories_next))
            .perform(click())
            .perform(click())
            .perform(click())
            .perform(click())
    }

    fun tapPrevUntilPrevGroup() = chainable {
        Espresso
            .onView(withId(storiesR.id.fl_stories_prev))
            .perform(click())
            .perform(click())
            .perform(click())
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
            .perform(actionOnItemAtPosition<ViewHolder>(2, click()))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))
    }

    fun swipeGroup() = chainable {
        Espresso.onView(withId(storiesR.id.stories_group_view_pager))
            .perform(swipeLeft())
            .perform(swipeRight())
    }

    private fun chainable(fn: () -> Unit): StoriesRobotUITest {
        fn()
        return this
    }

}
