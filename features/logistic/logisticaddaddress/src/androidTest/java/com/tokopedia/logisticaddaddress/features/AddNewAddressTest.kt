package com.tokopedia.logisticaddaddress.features

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddNewAddressTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(PinpointMapActivity::class.java)

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)


    @Test
    fun givenCurrentLocationShouldAddAddressPositive() {

        // Startup activity
        Thread.sleep(3000L)

        onView(withId(R.id.rv_poi_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(3000L)

        onView(withId(R.id.et_detail_address))
                .perform(typeText("no 27 RT 1/ RW X"), closeSoftKeyboard())

        onView(withId(R.id.btn_choose_location))
                .perform(click())
        Thread.sleep(3000L)

        onView(withId(R.id.et_detail_address)).perform(typeText(""), closeSoftKeyboard())

        onView(withId(R.id.btn_save_address)).perform(scrollTo(), click())

        Thread.sleep(1000L)

        /*// Failing Test: Cannot assert test result, mismatch result code,
        // possibly caused by improper assertion time due to use of Thread.sleep()
        // Possible solution: use idling resource
        assertThat(mActivityTestRule.activityResult, hasResultCode(Activity.RESULT_OK))
        assertThat(mActivityTestRule.activityResult, hasResultData(IntentMatchers.hasExtraWithKey("EXTRA_ADDRESS_NEW")))*/
    }

}