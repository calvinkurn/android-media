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
import com.tokopedia.search.R
import com.tokopedia.search.result.mps.DaggerMPSComponent
import com.tokopedia.search.result.mps.MPSFragment
import com.tokopedia.search.result.mps.MPSState
import com.tokopedia.search.result.mps.MPSStateModule
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.utils.createFakeBaseAppComponent
import com.tokopedia.search.utils.rawToObject
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

    @Test
    fun mps_loading() {
        launchFragmentInContainer {
            MPSFragment.newInstance().apply {
                injectDependencies(this, MPSState())
            }
        }

        onView(withId(R.id.mpsLoadingView)).check(matches(isDisplayed()))
    }

    @Test
    fun mps_success() {
        val mpsModel = rawToObject<MPSModel>(RTest.raw.mps_success)

        launchFragmentInContainer {
            MPSFragment.newInstance().apply {
                injectDependencies(this, MPSState().success(mpsModel))
            }
        }

        onView(withId(R.id.mpsSwipeRefreshLayout)).check(matches(isDisplayed()))
    }

    private fun injectDependencies(
        fragment: MPSFragment,
        mpsState: MPSState,
    ) {
        DaggerMPSComponent.builder()
            .baseAppComponent(createFakeBaseAppComponent(context))
            .mPSStateModule(MPSStateModule(mpsState))
            .build()
            .inject(fragment)
    }
}
