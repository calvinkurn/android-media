package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class EditAddressRevampTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(AddressFormActivity::class.java, false, false)

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
            addMockResponse(GET_ADDRESS_KEY, getRawString(context, R.raw.address_detail), FIND_BY_CONTAINS)
            addMockResponse(PINPOINT_VALIDATION_KEY, getRawString(context, R.raw.pinpoint_validation), FIND_BY_CONTAINS)
            addMockResponse(EDIT_ADDRESS_KEY, getRawString(context, R.raw.editaddress_success_response), FIND_BY_CONTAINS)
        }
    }

    @Test
    fun editAddress_WithPinpoint() {
        val queryPath = "tracker/logistic/editaddress_user_revamp_positive.json"
        editAddressRevamp {
            launchWithParam(context, mActivityTestRule)
            fillAddress(ADDRESS)
            fillPhoneNumber(PHONE)
            fillReceiver(RECEIVER)
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

        const val AUTOCOMPLETE_KEY = "KeroMapsAutoComplete"
        const val GET_DISTRICT_KEY = "KeroPlacesGetDistrict"
        const val PINPOINT_VALIDATION_KEY = "pinpoint_validation"
        const val EDIT_ADDRESS_KEY = "kero_edit_address"
        const val GET_ADDRESS_KEY = "kero_get_address"
    }
}