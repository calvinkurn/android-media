package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.logisticaddaddress.util.getJsonDataFromAsset
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

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
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
        val query = getJsonDataFromAsset(context, "{\n  \"mode\": \"subset\",\n  \"query\": [\n    {\n      \"screenName\": \"/user/address/create\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click suggestion kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click chips kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field alamat\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field nama penerima\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field no ponsel\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address\",\n      \"eventAction\": \"click button simpan\",\n      \"eventLabel\": \"negative success - logistic\"\n    }\n  ],\n  \"readme\": \"Logistic Add Address user funnel, also verifies CVR. STR: Create New Address from User Settings\"\n}")
                ?: throw AssertionError("Validator Query not found")
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
            hasPassedAnalytics(gtmLogDBSource, query)
        }
    }

    @Test
    fun addAddressCartFunnel_PassedAnalyticsTest() {
        val query = getJsonDataFromAsset(context, "{\n  \"mode\": \"subset\",\n  \"query\": [\n    {\n      \"screenName\": \"/cart/address/create\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click suggestion kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click chips kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field alamat\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field nama penerima\",\n      \"sessionIris\": \"{{.*}}\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field no ponsel\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address\",\n      \"eventAction\": \"click button simpan\",\n      \"eventLabel\": \"negative success - logistic\"\n    }\n  ],\n  \"readme\": \"Logistic Add Address cart funnel, also verifies CVR. STR: Create New Address from Cart Address Choice\"\n}")
                ?: throw AssertionError("Validator Query not found")
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
            hasPassedAnalytics(gtmLogDBSource, query)
        }
    }

    @Test
    fun addAddressNewUserFunnel_PassedAnalyticsTest() {
        val query = getJsonDataFromAsset(context, "{\n  \"mode\": \"subset\",\n  \"query\": [\n    {\n      \"screenName\": \"/user/address/create/cart\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click suggestion kota/kecamatan\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click chips kode pos\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field alamat\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field nama penerima\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address negative\",\n      \"event\": \"clickAddress\",\n      \"eventAction\": \"click field no ponsel\",\n      \"eventLabel\": \"logistic\"\n    },\n    {\n      \"eventCategory\": \"cart change address\",\n      \"eventAction\": \"click button simpan\",\n      \"eventLabel\": \"negative success - logistic\"\n    }\n  ],\n  \"readme\": \"Logistic Add Address new user funnel, also verifies CVR. STR: Go to checkout while user does not have any address \"\n}")
                ?: throw AssertionError("Validator Query not found")
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
            hasPassedAnalytics(gtmLogDBSource, query)
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