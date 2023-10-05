package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.interceptor.AddAddressInterceptor
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddNewAddressRevampTest {

    @get:Rule
    var mActivityTestRule = IntentsTestRule(SearchPageActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val logisticInterceptor = AddAddressInterceptor.logisticInterceptor

    @Before
    fun setup() {
        AddAddressInterceptor.resetAllCustomResponse()
        AddAddressInterceptor.setupGraphqlMockResponse(context)
        logisticInterceptor.autoCompleteResponsePath = getRawString(context, R.raw.autocomplete_tokopedia_tower)
        logisticInterceptor.getDistrictResponsePath = getRawString(context, R.raw.get_district_tokopedia_tower)
        logisticInterceptor.saveAddressResponsePath = getRawString(context, R.raw.save_address_success)
        logisticInterceptor.getCollectionPointResponsePath = getRawString(context, R.raw.get_collection_point_add)
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun addAddress_fromAddressList() {
        val queryPath = "tracker/logistic/addaddress_user_revamp.json"
        val screenName = "/user/address/create"
        addAddressRevamp {
            launchWithParam(mActivityTestRule, screenName)
            searchAddressStreet(KEYWORD)
            clickAddressStreetItem()
            onClickChooseLocation()
            fillAddress(ADDRESS)
            fillReceiver(RECEIVER)
            fillPhoneNumber(PHONE)
            checkTermsAndCondition()
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }

    @Test
    fun addAddress_fromCart() {
        val queryPath = "tracker/logistic/addaddress_cart_revamp.json"
        val screenName = "/cart/address/create"
        addAddressRevamp {
            launchWithParam(mActivityTestRule, screenName)
            searchAddressStreet(KEYWORD)
            clickAddressStreetItem()
            onClickChooseLocation()
            fillAddress(ADDRESS)
            fillReceiver(RECEIVER)
            fillPhoneNumber(PHONE)
            checkTermsAndCondition()
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }

    @Test
    fun addAddress_newUser() {
        val queryPath = "tracker/logistic/addaddress_new_user_revamp.json"
        val screenName = "/user/address/create/cart"
        addAddressRevamp {
            launchWithParam(mActivityTestRule, screenName)
            searchAddressStreet(KEYWORD)
            clickAddressStreetItem()
            onClickChooseLocation()
            fillAddress(ADDRESS)
            fillReceiver(RECEIVER)
            fillPhoneNumber(PHONE)
            checkTermsAndCondition()
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }

    companion object {
        const val KEYWORD = "Tokopedia"
        const val ADDRESS = "Jalan Prof. Dr Satrio 123"
        const val RECEIVER = "Anonymous"
        const val PHONE = "081299875432"
    }
}
