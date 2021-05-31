package com.tokopedia.shop.open.testcase

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.open.presentation.view.activity.ShopOpenRevampActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.common.EspressoIdlingResource
import com.tokopedia.shop.open.mock.ShopOpenMockResponseConfig
import com.tokopedia.shop.open.presentation.view.fragment.ShopOpenRevampQuisionerFragment
import com.tokopedia.shop.open.util.clickClickableSpan
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopOpenAnalyticTest {

    companion object {
        private const val SHOP_OPEN_SHOP_REGISTRATION_MATCHER_PATH = "tracker/shop_open/shop_open_shop_registration_tracker.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<ShopOpenRevampActivity> = IntentsTestRule(ShopOpenRevampActivity::class.java, false, false)
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.setString(Constant.TRACKING_QUEUE_SEND_TRACK_NEW_REMOTECONFIGKEY, "true")
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(ShopOpenMockResponseConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser2()
        activityRule.launchActivity(Intent())
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testShopOpenResultJourney() {
        Intents.intending(IntentMatchers.anyIntent())
                .respondWith(Instrumentation.ActivityResult(0, null))

        testInputShopNameAndDomain()
        testCheckTheSurvey()
        validateTracker()
    }

    private fun testInputShopNameAndDomain() {
        onView(allOf(instanceOf(AppCompatImageButton::class.java), isDescendantOfA(withId(R.id.toolbar_input_shop))))
                .perform(click())

        onView(withText("Batal"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click())

        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.text_input_shop_open_revamp_shop_name))))
                .perform(typeText("hello world"), closeSoftKeyboard())

        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.text_input_shop_open_revamp_domain_name))))
                .perform(replaceText("helloworld"))

        onView(firstView(AllOf.allOf(withId(R.id.recycler_view_shop_suggestions))))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2,  click()))

        onView(withId(R.id.txt_shop_open_revamp_tnc))
                .check(matches(isDisplayed()))
                .perform(clickClickableSpan("Syarat dan Ketentuan"))

        onView(withId(R.id.txt_shop_open_revamp_tnc))
                .check(matches(isDisplayed()))
                .perform(clickClickableSpan("Kebijakan Privasi."))

        onView(firstView(withId(R.id.shop_registration_button)))
                .perform(click())
    }

    private fun testCheckTheSurvey() {
        val mockIntentData = Intent().apply {
            putExtra(ShopOpenRevampQuisionerFragment.EXTRA_ADDRESS_MODEL, SaveAddressDataModel(
                    id = 2132414,
                    title = "for the best",
                    formattedAddress = "Jl. Damai",
                    addressName = "Jl. Damai",
                    receiverName = "kumon,",
                    address1 = "Jl. Damai",
                    address2 = "Jl. Damai",
                    postalCode = "20634",
                    phone = "088888888",
                    cityId = 12,
                    provinceId = 12,
                    districtId = 12,
                    latitude = "3.3096248",
                    longitude = "99.1677132",
                    editDetailAddress = "Jl. Damai",
                    selectedDistrict = "District A",
                    zipCodes = listOf("1222", "1111")
            ))
        }

        onView(allOf(instanceOf(AppCompatImageButton::class.java), isDescendantOfA(withId(R.id.toolbar_questioner))))
                .perform(click())

        onView(withText("Batal"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click())

        onView(firstView(AllOf.allOf(
                withId(R.id.checkbox_choice))))
                .perform(click())

        onView(allOf(withText("Lewati"), isDescendantOfA(withId(R.id.toolbar_questioner))))
                .perform(click())

        onView(withText("Batal"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(pressBack())

        Intents.intending(IntentMatchers.anyIntent())
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, mockIntentData))

        onView(firstView(withId(R.id.next_button_quisioner_page)))
                .perform(click())
    }

    private fun validateTracker() {
        activityRule.activity.finish()
        doAnalyticDebuggerTest()
    }

    private fun doAnalyticDebuggerTest() {
        assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, SHOP_OPEN_SHOP_REGISTRATION_MATCHER_PATH),
                hasAllSuccess()
        )
    }
}