package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.interceptor.AddAddressInterceptor
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class EditAddressRevampTest {

    @get:Rule
    var mActivityTestRule = IntentsTestRule(AddressFormActivity::class.java, false, false)

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
        logisticInterceptor.getAddressResponsePath = getRawString(context, R.raw.address_detail_tokopedia_tower)
        logisticInterceptor.pinPointValidationResponsePath = getRawString(context, R.raw.pinpoint_validation)
        logisticInterceptor.editAddressResponsePath = getRawString(context, R.raw.editaddress_success_response)
        logisticInterceptor.getCollectionPointResponsePath = getRawString(context, R.raw.get_collection_point_edit)
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun editAddress_WithPinpoint() {
        val queryPath = "tracker/logistic/editaddress_user_revamp_positive.json"
        editAddressRevamp {
            launchWithParam(context, mActivityTestRule)
            fillReceiver(RECEIVER)
            fillAddress(ADDRESS)
            fillPhoneNumber(PHONE)
            onClickChangePinpoint()
            onClickCariUlangAlamat()
            searchAddressStreet(KEYWORD)
            clickAddressStreetItem()
            onClickChooseLocation()
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
