package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressFragment.Companion.EXTRA_ADDRESS_NEW
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.EXTRA_REF
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Only works when device has turned on Location
 * */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AddNewAddressTest {

    @get: Rule
    var mActivityTestRule = IntentsTestRule(PinpointMapActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(AddAddressMockConfig().createMockModel(context))
        val i = Intent(context, PinpointMapActivity::class.java)
        i.putExtra(EXTRA_REF, "/user/address/create")
        mActivityTestRule.launchActivity(i)
    }

    @Test
    fun givenCurrentLocationShouldAddAddressPositive() {

        // Startup activity
        delayShort()

        onView(withId(R.id.et_search)).perform(typeText("jak"), closeSoftKeyboard())
        delayShort()

        onView(withId(R.id.rv_poi_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        delayShort()

        onView(withId(R.id.et_detail_address))
                .perform(typeText("no 27 RT 1/ RW X"), closeSoftKeyboard())
        onView(withId(R.id.btn_choose_location)).perform(click())

        onView(withId(R.id.et_receiver_name)).perform(typeText("Anonymous"), closeSoftKeyboard())
        onView(withId(R.id.et_phone)).perform(typeText("087255991177"), closeSoftKeyboard())

        onView(withId(R.id.btn_save_address)).perform(scrollTo(), click())
        delayShort()

        assertThat(mActivityTestRule.activityResult, hasResultCode(Activity.RESULT_OK))
        assertThat(mActivityTestRule.activityResult, hasResultData(hasExtraWithKey(EXTRA_ADDRESS_NEW)))
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context,
                "tracker/logistic/add_address_cvr.json"), hasAllSuccess())
    }

    private fun delayShort() {
        Thread.sleep(1000L)
    }

}