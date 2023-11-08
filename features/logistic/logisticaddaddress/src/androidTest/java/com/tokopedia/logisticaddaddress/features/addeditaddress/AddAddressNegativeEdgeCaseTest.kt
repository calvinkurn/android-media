package com.tokopedia.logisticaddaddress.features.addeditaddress

import android.Manifest
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.logisticaddaddress.features.addeditaddress.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.interceptor.AddAddressInterceptor
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.logisticaddaddress.test.R as logisticaddaddresstestR

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddAddressNegativeEdgeCaseTest {

    @get:Rule
    var mActivityTestRule = IntentsTestRule(SearchPageActivity::class.java, false, false)

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val logisticInterceptor = AddAddressInterceptor.logisticInterceptor

    @Before
    fun setup() {
        AddAddressInterceptor.resetAllCustomResponse()
        AddAddressInterceptor.setupGraphqlMockResponse(context)
        logisticInterceptor.getDistrictRecommendationResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.district_recommendation_jakarta)
        logisticInterceptor.saveAddressResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.save_address_success)
        logisticInterceptor.getCollectionPointResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.get_collection_point_add)
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @After
    fun tear() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun addAddress_EdgeCaseUiTest() {
        val screenName = "/user/address/create/cart"
        logisticInterceptor.getDistrictRecommendationResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.get_district_recom_jakarta)
        logisticInterceptor.getDistrictCenterResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.get_district_center_taman_sari)
        logisticInterceptor.autoCompleteResponsePath =
            getRawString(context, logisticaddaddresstestR.raw.autocomplete_taman_sari)

        addAddressRevamp {
            launchWithParam(mActivityTestRule, screenName)
            clickManualForm()
            fillReceiver(RECEIVER)
            fillAddressNegative(ADDRESS)
            fillPhoneNumber(PHONE)
            clickKotaKecamatan()
            searchKotaKecamatan(KEYWORD)
            clickKotaKecamatanItem()
            clickPostalCode()
            clickPostalCodeItem()
            clickChoosePostalCode()
            scrollToBottom()
            logisticInterceptor.autofillResponsePath =
                getRawString(context, logisticaddaddresstestR.raw.autofill_taman_sari)
            logisticInterceptor.districtBoundaryResponsePath =
                getRawString(context, logisticaddaddresstestR.raw.district_boundary_taman_sari)
            clickPinpointWidgetNegative()
            clickCariUlang()
            searchAddressStreet(KEYWORD)
            logisticInterceptor.getDistrictResponsePath =
                getRawString(context, logisticaddaddresstestR.raw.get_district_glodok)
            clickAddressStreetItem()
            onClickChooseLocation()
            scrollToBottom()
        } check {
            isNegativeFlow()
            assertAlreadyPinpointNegativeFullFlow()
            assertKotaKecamatan("Taman Sari, Jakarta Barat, DKI Jakarta")
            assertAddressDetailNegative(ADDRESS)
        }
    }

    companion object {
        const val KEYWORD = "Jakarta"
        const val ADDRESS = "Jalan Prof. Dr Satrio 123"
        const val RECEIVER = "Anonymous"
        const val PHONE = "081299875432"
    }
}
