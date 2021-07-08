package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressTest
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddNewAddressRevampTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(SearchPageActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(AUTOCOMPLETE_KEY, getRawString(context, R.raw.autocomplete_tokopedia_tower), FIND_BY_CONTAINS)
            addMockResponse(GET_DISTRICT_KEY, getRawString(context, R.raw.get_district_tokopedia_tower), FIND_BY_CONTAINS)
        }
    }

    @Test
    fun addAddress_fromAddressList() {
        val queryPath = "tracker/logistic/addaddress_user_revamp.json"
        val screenName = "/user/address/create"
        addAddressRevamp {
            launchWithParam(mActivityTestRule, screenName)
            searchAddressStreet("Tokopedia")
            clickAddressStreetItem()
            onClickChooseLocation()
            fillAddress("Jalan Prof. Dr Satrio 123")
            fillReceiver("Anonymous")
            fillPhoneNumber("081299875432")
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }

    companion object {
        const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
        const val GET_DISTRICT_KEY = "KeroPlacesGetDistrict"
    }
}