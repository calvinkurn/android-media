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
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig.Companion.FIND_BY_CONTAINS
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class EditAddressRevampNoPinpointTest {

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
            addMockResponse(GET_ADDRESS_KEY, getRawString(context, R.raw.address_detail_no_pinpoint), FIND_BY_CONTAINS)
            addMockResponse(GET_DISTRICT_RECOM, getRawString(context, R.raw.district_recommendation_jakarta), FIND_BY_CONTAINS)
            addMockResponse(EDIT_ADDRESS_KEY, getRawString(context, R.raw.editaddress_success_response), FIND_BY_CONTAINS)
        }
    }

    @Test
    fun editAddress_WithPinpoint() {
        val queryPath = "tracker/logistic/editaddress_user_revamp_negative.json"
        editAddressRevamp {
            launchWithParam(context, mActivityTestRule)
            clickKotaKecamatan()
            searchKotaKecamatan(KEYWORD)
            clickKotaKecamatanItem()
            clickPostalCode()
            clickPostalCodeItem()
            clickChoosePostalCode()
        } submit {
            hasPassedAnalytics(cassavaTestRule, queryPath)
        }
    }


    companion object {
        const val KEYWORD = "Tokopedia"
        const val ADDRESS = "Jalan Prof. Dr Satrio 123"
        const val RECEIVER = "Anonymous"
        const val PHONE = "081299875432"

        const val GET_ADDRESS_KEY = "kero_get_address"
        const val GET_DISTRICT_RECOM = "GetDistrictRecommendation"
        const val EDIT_ADDRESS_KEY = "kero_edit_address"
    }
}