package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.Manifest
import androidx.test.espresso.Espresso
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
@RunWith(AndroidJUnit4::class)
@LargeTest
class EditAddressRevampNoPinpointTest {

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
        logisticInterceptor.getAddressResponsePath = getRawString(context, R.raw.address_detail_no_pinpoint)
        logisticInterceptor.getDistrictRecommendationResponsePath = getRawString(context, R.raw.district_recommendation_jakarta)
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
        val queryPath = "tracker/logistic/editaddress_user_revamp_negative.json"
        editAddressRevamp {
            launchWithParam(context, mActivityTestRule)
            Espresso.closeSoftKeyboard()
            clickKotaKecamatan()
            searchKotaKecamatan(KEYWORD)
            clickKotaKecamatanItem()
            clickPostalCode()
            clickPostalCodeItem()
            clickChoosePostalCode()
            scrollToBottom()
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }

    companion object {
        const val KEYWORD = "Tokopedia"
    }
}
