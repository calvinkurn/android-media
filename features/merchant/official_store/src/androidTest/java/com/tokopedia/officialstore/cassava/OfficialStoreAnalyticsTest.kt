package com.tokopedia.officialstore.cassava

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.home_component.viewholders.FeaturedShopViewHolder
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestFullActivity
import com.tokopedia.officialstore.extension.selectTabAtPosition
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialBannerViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialBenefitViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialFeaturedShopViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelSprintSaleViewHolder
import com.tokopedia.officialstore.util.OSRecyclerViewIdlingResource
import com.tokopedia.officialstore.util.removeProgressBarOnOsPage
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

/**
 * Created by Lukas on 1/11/21.
 */
@CassavaTest
class OfficialStoreAnalyticsTest {

    companion object {
        private const val TAG = "OfficialStoreAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME =
            "tracker/official_store/official_store_page.json"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MERCHANT_VOUCHER =
            "tracker/official_store/merchant_voucher_os.json"

        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SPECIAL_RELEASE =
            "tracker/official_store/special_release.json"
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
    }
    private var osRecyclerViewIdlingResource: OSRecyclerViewIdlingResource? = null

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationOfficialStoreTestFullActivity>(
            InstrumentationOfficialStoreTestFullActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
            setToLocation1()
            setupGraphqlMockResponse(OfficialStoreMockResponseConfig())
        }
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun setup() {
        osRecyclerViewIdlingResource = OSRecyclerViewIdlingResource(
                activity = activityRule.activity,
                limitCountToIdle = 3
        )
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        IdlingRegistry.getInstance().register(osRecyclerViewIdlingResource)
    }

    @After
    fun deleteDatabase() {
        IdlingRegistry.getInstance().unregister(osRecyclerViewIdlingResource)
    }

    private fun setToLocation1() {
        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                context = InstrumentationRegistry.getInstrumentation().context,
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

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun waitForLoadCassavaAssert() {
        Thread.sleep(2000)
    }

    private fun doActivityTest() {
        // 1. click category OS
        onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))

        // 2. scroll and click item at OS
        // Scroll to bottom first and then back to top for load all data (recom case)
        val recyclerView =
            activityRule.activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        val productRecommendationOffset = 10

        /**
         * This function needed to remove any loading view, because any infinite loop rendered view such as loading view,
         * shimmering, progress bar, etc can block instrumentation test
         */
        removeProgressBarOnOsPage(recyclerView, activityRule.activity)

        for (i in 0 until (itemCount + productRecommendationOffset)) {
            if (i == itemCount) {
                /**
                 * This function needed to trigger product recommendation usecase in official store,
                 * official store page only hit recommendation usecase on scroll in the end of current list
                 */
                onView(CommonMatcher.firstView(withId(R.id.os_child_recycler_view)))
                        .perform(ViewActions.swipeDown())
                onView(CommonMatcher.firstView(withId(R.id.os_child_recycler_view)))
                        .perform(ViewActions.swipeUp())
                onView(CommonMatcher.firstView(withId(R.id.os_child_recycler_view)))
                        .perform(ViewActions.swipeUp())
            }
            Thread.sleep(2500)
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun endActivityTest() {
        activityRule.activity.moveTaskToBack(true)
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }
    private fun <T: Any> doActivityTestByModelClass(
        delayBeforeRender: Long = 2000L,
        dataModelClass : KClass<T>,
        predicate: (T?) -> Boolean = {true},
        isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int)-> Unit) {
        val officialHomeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)
        val officialHomeAdapter = officialHomeRecyclerView.adapter as? OfficialHomeAdapter

        val visitableList = officialHomeAdapter?.currentList?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName && predicate.invoke(it as? T) }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex->
            scrollRecyclerViewToPosition(officialHomeRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = officialHomeRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder-> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
        endActivityTest()
    }

    private fun checkProductOnDynamicChannel(officialStoreRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = officialStoreRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixLeftComponentViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, MixLeftComponentViewHolder.RECYCLER_VIEW_ID, 0)
            }
            is MixTopComponentViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is OfficialBenefitViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recyclerview_official_benefit, 0)
            }
            is OfficialFeaturedShopViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycler_view_featured_shop,0)
            }
            is OfficialBannerViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.viewpager_banner_category,0)
                onView(withId(R.id.promo_link)).perform(ViewActions.click())
            }
            is DynamicLegoBannerViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList, 0)
            }
            is DynamicChannelSprintSaleViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_sprintsale_rv,0)
            }
            is FeaturedShopViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(
                        viewHolder.itemView,
                        R.id.dc_banner_rv,
                        0
                )
            }
            is OfficialProductRecommendationViewHolder -> {
                activityRule.runOnUiThread {
                    viewHolder.itemView.performClick()
                }
            }
            is FeaturedBrandViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.recycleList,0)
                onView(firstView(withId(R.id.see_all_button))).perform(ViewActions.click())
            }
        }
    }
    @Test
    fun checkOSAnalyticsWithCassava2() {
        onView(firstView(withId(R.id.os_child_recycler_view))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        OSCassavaTest {
            initTest()
            doActivityTest()
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME)
        }
    }

    @Test
    fun testComponentMerchantVoucherWidget() {
        OSCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = MerchantVoucherDataModel::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
                actionOnMerchantVoucherWidget(viewHolder)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_MERCHANT_VOUCHER)
        }
    }

    @Test
    fun testSpecialReleaseWidget() {
        OSCassavaTest {
            initTest()
            doActivityTestByModelClass(dataModelClass = SpecialReleaseDataModel::class) {viewHolder, itemClickLimit ->
                onView(firstView(withId(R.id.see_all_button))).perform(ViewActions.click())
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.home_component_special_release_rv,0)
            }
        } validateAnalytics {
            addDebugEnd()
            hasPassedAnalytics(cassavaTestRule, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_SPECIAL_RELEASE)
        }
    }
}
