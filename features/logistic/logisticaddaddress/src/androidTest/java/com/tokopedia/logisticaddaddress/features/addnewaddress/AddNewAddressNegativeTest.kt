package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
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
class AddNewAddressNegativeTest {

    @get: Rule
    var mActivityTestRule = ActivityTestRule(PinpointMapActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(AUTOCOMPLETE_KEY, InstrumentationMockHelper.getRawString(context, R.raw.autocomplete_jak_negative), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(GET_DISTRICT_KEY, InstrumentationMockHelper.getRawString(context, R.raw.district_recommendation_jakarta), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SAVE_ADDRESS_KEY, InstrumentationMockHelper.getRawString(context, R.raw.save_address_success), MockModelConfig.FIND_BY_CONTAINS)
        }
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun addAddressUserFunnel_PassedAnalyticsTest() {
        val query = "tracker/logistic/addaddress_user_funnel_negative.json"
        val screenName = "/user/address/create"
        addAddress {
            launchFrom(mActivityTestRule, screenName)
            searchWithKeyword(JAK_KEYWORD)
            clickCity()
            searchCityWithKeyword(JAK_KEYWORD)
            selectFirstCityItem()
            zipCode()
            selectFirstZipCode()
            address(TEST_DETAILS)
            receiver(TEST_RECEIVER)
            phoneNumber(TEST_PHONE)
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }

    @Test
    fun addAddressCartFunnel_PassedAnalyticsTest() {
        val query = "tracker/logistic/addaddress_cart_funnel_negative.json"
        val screenName = "/cart/address/create"
        addAddress {
            launchFrom(mActivityTestRule, screenName)
            searchWithKeyword(JAK_KEYWORD)
            clickCity()
            searchCityWithKeyword(JAK_KEYWORD)
            selectFirstCityItem()
            zipCode()
            selectFirstZipCode()
            address(TEST_DETAILS)
            receiver(TEST_RECEIVER)
            phoneNumber(TEST_PHONE)
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }

    @Test
    fun addAddressNewUserFunnel_PassedAnalyticsTest() {
        val query = "tracker/logistic/addaddress_new_user_funnel_negative.json"
        val screenName = "/user/address/create/cart"
        addAddress {
            launchFrom(mActivityTestRule, screenName)
            searchWithKeyword(JAK_KEYWORD)
            clickCity()
            searchCityWithKeyword(JAK_KEYWORD)
            selectFirstCityItem()
            zipCode()
            selectFirstZipCode()
            address(TEST_DETAILS)
            receiver(TEST_RECEIVER)
            phoneNumber(TEST_PHONE)
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }

    companion object {
        const val JAK_KEYWORD = "jak"
        const val TEST_RECEIVER = "Anonymous"
        const val TEST_PHONE = "087255991177"
        const val TEST_DETAILS = "no 27 RT 1/ RW X"
        const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
        const val GET_DISTRICT_KEY = "KeroDistrictRecommendation"
        const val SAVE_ADDRESS_KEY = "kero_add_address"
    }

}