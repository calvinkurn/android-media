package com.tokopedia.home.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.tokopedia.home.R
import com.tokopedia.home.component.disableCoachMark
import com.tokopedia.home.environment.InstrumentationHomeEmptyActivity
import com.tokopedia.home.environment.InstrumentationHomeRevampTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_ATF_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_DYNAMIC_CHANNEL_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_HEADER_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.MOCK_RECOMMENDATION_TAB_COUNT
import com.tokopedia.home.ui.HomeMockValueHelper.setupAbTestRemoteConfig
import com.tokopedia.home.ui.HomeMockValueHelper.setupDynamicChannelQueryRemoteConfig
import com.tokopedia.home.util.HomeInstrumentationTestHelper.deleteHomeDatabase
import com.tokopedia.home.util.HomeRecyclerViewIdlingResource
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by devarafikry on 16/11/21.
 */
@UiTest
class HomeFragmentRefreshTest {
    private var homeRecyclerViewIdlingResource: HomeRecyclerViewIdlingResource? = null
    private val context = getInstrumentation().targetContext
    private val totalData =
        MOCK_HEADER_COUNT + MOCK_ATF_COUNT + MOCK_DYNAMIC_CHANNEL_COUNT + MOCK_RECOMMENDATION_TAB_COUNT
    private var mDevice: UiDevice? = null
    private var dataChangedCount: Int = 0
    companion object {
        /**
         * Header will be refreshed exactly 2 times on Resume with 3 minutes rule not reached
         * 1 For get home balance widget
         * 1 For wallet data refresh
         * 1 For Membership data refresh
         *
         * But if 3 minutes rule reached, total refresh will be above 2
         */
        private const val TOTAL_PARTIAL_HEADER_REFRESH_COUNT = 2
        private const val TOTAL_PARTIAL_HEADER_RESUME_COUNT = 1

        private const val BELOW_THREE_MINUTES_ELAPSED_TIME = 1000L

        // Should be 3 mins rule, but mocked to 3 secs
        private const val ABOVE_THREE_MINUTES_ELAPSED_TIME = 5000L

        private const val ADDRESS_1_ID = "0"
        private const val ADDRESS_1_CITY_ID = "228"
        private const val ADDRESS_1_DISTRICT_ID = "3171"
        private const val ADDRESS_1_LAT = ""
        private const val ADDRESS_1_LONG = ""
        private const val ADDRESS_1_LABEL = "Dampit, Kab. Malang"
        private const val ADDRESS_1_POSTAL_CODE = ""
        private const val ADDRESS_1_SHOP_ID = "11530573"
        private const val ADDRESS_1_WAREHOUE_ID = "0"
        private val ADDRESS_1_WAREHOUSES = listOf(LocalWarehouseModel(warehouse_id = 12345, service_type = "2h"), LocalWarehouseModel(warehouse_id = 0, service_type = "15m"))
        private const val ADDRESS_1_SERVICE_TYPE = "15m"

        private const val ADDRESS_2_ID = "0"
        private const val ADDRESS_2_CITY_ID = "463"
        private const val ADDRESS_2_DISTRICT_ID = "1616"
        private const val ADDRESS_2_LAT = ""
        private const val ADDRESS_2_LONG = ""
        private const val ADDRESS_2_LABEL = "Serpong, Kota Tangerang Selatan"
        private const val ADDRESS_2_POSTAL_CODE = ""
        private const val ADDRESS_2_SHOP_ID = "11530573"
        private const val ADDRESS_2_WAREHOUE_ID = "0"
        private val ADDRESS_2_WAREHOUSES = listOf(LocalWarehouseModel(warehouse_id = 12345, service_type = "2h"), LocalWarehouseModel(warehouse_id = 0, service_type = "15m"))
        private const val ADDRESS_2_SERVICE_TYPE = "15m"
    }

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeRevampTestActivity>(
        InstrumentationHomeRevampTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            InstrumentationRegistry.getInstrumentation().context.deleteHomeDatabase()
            InstrumentationAuthHelper.clearUserSession()
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
            setupGraphqlMockResponse(HomeMockResponseConfig())
            disableCoachMark(context)
            setupAbTestRemoteConfig()
            setupDynamicChannelQueryRemoteConfig()
            dataChangedCount = 0
            setToLocation1()
            super.beforeActivityLaunched()
        }
    }

    @Before
    fun setupEnvironment() {
        val recyclerView: RecyclerView =
            activityRule.activity.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerViewIdlingResource = HomeRecyclerViewIdlingResource(
            recyclerView = recyclerView,
            limitCountToIdle = totalData
        )
        IdlingRegistry.getInstance().register(homeRecyclerViewIdlingResource)
        mDevice = UiDevice.getInstance(getInstrumentation())
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(homeRecyclerViewIdlingResource)
    }

    @Test
    fun test_onresume_when_elapsed_below_three_minutes_then_do_partial_refresh() {
        dataChangedCount = 0
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        /**
         * Setup adapter data observer to observe data changes
         */
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        recyclerView.adapter?.registerAdapterDataObserver(changeCountDetector())

        goToOtherPage()
        Thread.sleep(BELOW_THREE_MINUTES_ELAPSED_TIME)

        mDevice?.pressBack()

        /**
         * Assert data changes count
         * Partial refresh will only trigger 1 data changes
         * - Dynamic channel
         */
        waitForAssertion()
        Assert.assertTrue(dataChangedCount >= TOTAL_PARTIAL_HEADER_RESUME_COUNT)
    }

    @Test
    fun test_onresume_when_elapsed_above_three_minutes_then_do_full_refresh() {
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        /**
         * Setup adapter data observer to observe data changes
         */
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        recyclerView.adapter?.registerAdapterDataObserver(changeCountDetector())

        goToOtherPage()

        Thread.sleep(ABOVE_THREE_MINUTES_ELAPSED_TIME)

        mDevice?.pressBack()

        /**
         * Assert data changes count
         * Full refresh will trigger more than 2 data changes
         * - Get Home Balance Widget
         * - Other data changes (from dynamic channel i.e Best seller widget, Play carousel widget, etc)
         */
        waitForAssertion()
        Assert.assertTrue(dataChangedCount > TOTAL_PARTIAL_HEADER_REFRESH_COUNT)
    }

    @Test
    fun test_onresume_when_elapsed_below_three_minutes_and_change_address_then_do_full_refresh() {
        onView(withId(R.id.home_fragment_recycler_view)).check(matches(isDisplayed()))
        /**
         * Setup adapter data observer to observe data changes
         */
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        recyclerView.adapter?.registerAdapterDataObserver(changeCountDetector())

        goToOtherPage()

        /**
         * Set to another location
         */
        setToLocation2()

        Thread.sleep(BELOW_THREE_MINUTES_ELAPSED_TIME)

        mDevice?.pressBack()

        /**
         * Assert data changes count
         * Full refresh will trigger more than 2 data changes
         * - Wallet data changes
         * - Membership data changes
         * - Other data changes (from dynamic channel i.e Best seller widget, Play carousel widget, etc)
         *
         * Address data changed in other page will change home page choose address in onResume
         */
        waitForAssertion()
        Assert.assertTrue(dataChangedCount > TOTAL_PARTIAL_HEADER_REFRESH_COUNT)
        onView(withText(containsString(ADDRESS_2_LABEL))).check(matches(isDisplayed()))
    }

    private fun goToOtherPage() {
        context.startActivity(
            Intent(context, InstrumentationHomeEmptyActivity::class.java).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private fun setToLocation1() {
        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
            context = context,
            addressId = ADDRESS_1_ID,
            cityId = ADDRESS_1_CITY_ID,
            districtId = ADDRESS_1_DISTRICT_ID,
            lat = ADDRESS_1_LAT,
            long = ADDRESS_1_LONG,
            label = ADDRESS_1_LABEL,
            postalCode = ADDRESS_1_POSTAL_CODE,
            shopId = ADDRESS_1_SHOP_ID,
            warehouseId = ADDRESS_1_WAREHOUE_ID,
            warehouses = ADDRESS_1_WAREHOUSES,
            serviceType = ADDRESS_1_SERVICE_TYPE
        )
    }

    private fun setToLocation2() {
        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
            context = context,
            addressId = ADDRESS_2_ID,
            cityId = ADDRESS_2_CITY_ID,
            districtId = ADDRESS_2_DISTRICT_ID,
            lat = ADDRESS_2_LAT,
            long = ADDRESS_2_LONG,
            label = ADDRESS_2_LABEL,
            postalCode = ADDRESS_2_POSTAL_CODE,
            shopId = ADDRESS_2_SHOP_ID,
            warehouseId = ADDRESS_2_WAREHOUE_ID,
            warehouses = ADDRESS_2_WAREHOUSES,
            serviceType = ADDRESS_2_SERVICE_TYPE
        )
    }

    private fun changeCountDetector(): RecyclerView.AdapterDataObserver {
        val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                if (positionStart == 0) {
                    dataChangedCount++
                }
            }
        }
        return adapterDataObserver
    }

    private fun waitForAssertion() {
        Thread.sleep(2000)
    }
}
