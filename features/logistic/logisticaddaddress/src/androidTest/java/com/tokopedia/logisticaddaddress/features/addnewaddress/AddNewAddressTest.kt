package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
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
    var mActivityTestRule = ActivityTestRule(PinpointMapActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(AddAddressMockConfig().createMockModel(context))

        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun givenCurrentLocationShouldAddAddressPositive() {

        addAddress {
            launchFrom(mActivityTestRule, "/user/address/create")
            searchWithKeyword("jak")
            selectFirstItem()
            addressDetail("no 27 RT 1/ RW X")
            receiver("Anonymous")
            phoneNumber("087255991177")
        }.submit()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context,
                "tracker/logistic/add_address_cvr.json"), hasAllSuccess())
    }

}