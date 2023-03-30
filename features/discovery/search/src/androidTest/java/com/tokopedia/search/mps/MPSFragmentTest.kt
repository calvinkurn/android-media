package com.tokopedia.search.mps

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.search.R
import com.tokopedia.search.di.module.SearchContextModule
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.MPSState
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.presentation.view.activity.SearchComponent
import com.tokopedia.search.utils.createFakeBaseAppComponent
import com.tokopedia.search.utils.rawToObject
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.search.test.R as RTest

@RunWith(AndroidJUnit4::class)
class MPSFragmentTest {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context!!

    private inline fun <reified F : Fragment> launchFragmentInContainer(
        crossinline instantiate: () -> F
    ) = launchFragmentInContainer(
        themeResId = R.style.DiscoveryTheme,
        instantiate = instantiate
    )

    private fun searchComponent(mpsState: MPSState): SearchComponent =
        DaggerMPSFragmentTestComponent.builder()
            .baseAppComponent(createFakeBaseAppComponent(context))
            .searchContextModule(SearchContextModule(context))
            .fakeMPSViewModelModule(FakeMPSViewModelModule(mpsState))
            .build()

    @Test
    fun loading() {
        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(MPSState()))
        }

        onView(withId(R.id.mpsLoadingView)).check(matches(isDisplayed()))
        onView(withId(R.id.mpsSwipeRefreshLayout)).check(matches(not(isDisplayed())))
    }

    @Test
    fun success() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_success)
        val mpsStateSuccess = MPSState().success(mpsModel)

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateSuccess))
        }

        onView(withId(R.id.mpsSwipeRefreshLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.mpsLoadingView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun failed() {
        val mpsStateError = MPSState().error(Error("test exception"))

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateError))
        }

        onView(withId(R.id.mpsSwipeRefreshLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.mpsLoadingView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.main_retry)).check(matches(isDisplayed()))
    }

    @Test
    fun load_more_failed() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_success)
        val mpsStateError = MPSState().success(mpsModel).errorLoadMore(Error("test exception"))

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateError))
        }

        onView(withId(R.id.mpsSwipeRefreshLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun empty_state_keyword() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_empty_state)
        val mpsStateSuccess = MPSState().success(mpsModel)

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateSuccess))
        }

        onView(withId(R.id.mpsEmptyStateKeywordImage)).check(matches(isDisplayed()))
    }

    @Test
    fun empty_state_filter() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_empty_state)
        val appliedFilter = mpsModel.quickFilterList.first().options.first()
        val mpsStateSuccess = MPSState(mapOf(
            appliedFilter.key to appliedFilter.value
        )).success(mpsModel)

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateSuccess))
        }

        onView(withId(R.id.mpsEmptyStateFilterImage)).check(matches(isDisplayed()))
    }

    @Test
    fun open_loading_bottomsheet_filter() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_success)
        val mpsStateSuccess = MPSState().success(mpsModel).openBottomSheetFilter()

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateSuccess))
        }

        onView(withId(R.id.progressBarSortFilterBottomSheet)).check(matches(isDisplayed()))
    }

    @Test
    fun open_bottomsheet_filter() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_success)
        val dynamicFilterModel = rawToObject<DynamicFilterModel>(RTest.raw.mps_dynamic_filter_model)
        val mpsStateSuccess = MPSState().success(mpsModel).setBottomSheetFilterModel(dynamicFilterModel)

        launchFragmentInContainer {
            MPSFragment.newInstance(searchComponent(mpsStateSuccess))
        }

        onView(withId(R.id.recyclerViewSortFilterBottomSheet)).check(matches(isDisplayed()))
    }
}
