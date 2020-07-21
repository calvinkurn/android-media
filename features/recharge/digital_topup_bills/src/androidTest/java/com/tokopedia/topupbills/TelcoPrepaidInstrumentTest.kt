package com.tokopedia.topupbills

import android.content.Intent
import android.os.SystemClock
import android.provider.ContactsContract
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ViewPagerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.android.material.tabs.TabLayout
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TelcoPrepaidInstrumentTest {
    @get:Rule
    var mActivityRule: IntentsTestRule<TelcoPrepaidActivity> = object : IntentsTestRule<TelcoPrepaidActivity>(TelcoPrepaidActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, TelcoPrepaidActivity::class.java).apply {
                putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
                putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
                putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
                putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
            }
        }
    }

    @Before
    fun stubAllExternalIntents() {
        val telcoContactHelper = TelcoContactHelper()
        val contentResolver = mActivityRule.activity.contentResolver

        Intents.intending(AllOf.allOf(IntentMatchers.hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                IntentMatchers.hasAction(Intent.ACTION_PICK))
        ).respondWith(telcoContactHelper.createUriContact(contentResolver))
    }

    /**
     * activate the comment below if the test is new on the device
     */
    @Test
    fun show_contents_pdp_telco_not_login() {
//        Thread.sleep(1000)
//        onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
//        onView(withText(R.string.Telco_title_showcase_client_number)).perform(click())
//        onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
//        onView(withText(R.string.telco_title_showcase_promo)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.page_container)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
    }

    @Test
    fun interaction_menu() {
        Thread.sleep(1000)
        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.menu_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_help)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_order_list)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.btn_close)).perform(click())
    }

    @Test
    fun click_done_keyboard_fav_number() {
        Thread.sleep(1000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.edit_text_search)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.pressImeActionButton())
        onView(withId(R.id.ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    @Test
    fun click_phonebook() {
        Thread.sleep(1000)
        onView(withId(R.id.ac_input_number)).check(matches(withText("")))
        onView(withId(R.id.btn_contact_picker)).perform(click())
        onView(withId(R.id.ac_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.ac_input_number)).check(matches(withText(VALID_PHONE_BOOK)))

        Thread.sleep(1000)
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
        onView(withId(R.id.tab_layout)).perform(selectTabLayoutPosition(2))
        Thread.sleep(1000)
    }

    private fun selectTabLayoutPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "with tab at index $tabIndex"
            }

            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex) ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()
                tabAtIndex.select()
            }
        }
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
    }

}