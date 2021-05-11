package com.tokopedia.manageaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.manageaddress.di.DaggerTestAppComponent
import com.tokopedia.manageaddress.di.FakeAppModule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManageAddressTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(ManageAddressActivity::class.java, false, false)


    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val component = DaggerTestAppComponent.builder().fakeAppModule(FakeAppModule(ctx)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(component)
        mActivityTestRule.launchActivity(Intent())
    }

    @Test
    fun manageAddress() {
        Thread.sleep(2000)
        onView(withId(R.id.address_list)).perform(scrollToPosition<RecyclerView.ViewHolder>(2))
    }
}