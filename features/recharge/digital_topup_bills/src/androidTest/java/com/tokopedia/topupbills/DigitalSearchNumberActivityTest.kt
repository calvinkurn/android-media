package com.tokopedia.topupbills

import android.Manifest
import android.content.Intent
import android.provider.ContactsContract
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberActivity
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class DigitalSearchNumberActivityTest {

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalSearchNumberActivity> =
            object : IntentsTestRule<DigitalSearchNumberActivity>(DigitalSearchNumberActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val favNumbers = ArrayList<TopupBillsFavNumberItem>()
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, DigitalSearchNumberActivity::class.java).apply {
                        putExtra(TopupBillsSearchNumberFragment.ARG_PARAM_EXTRA_CLIENT_NUMBER, ClientNumberType.TYPE_INPUT_TEL)
                        putExtra(TopupBillsSearchNumberFragment.ARG_PARAM_EXTRA_NUMBER, "")
                        putParcelableArrayListExtra(TopupBillsSearchNumberFragment.ARG_PARAM_EXTRA_NUMBER_LIST, favNumbers)
                    }
                }
            }

    @Before
    fun stubAllExternalIntents() {
        val telcoContactHelper = TelcoContactHelper()
        val contentResolver = mActivityRule.activity.contentResolver

        Intents.intending(AllOf.allOf(hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                hasAction(Intent.ACTION_PICK))
        ).respondWith(telcoContactHelper.createUriContact(contentResolver))
    }

    @Test
    fun click_clear_on_search_number_view() {
        Espresso.onView(ViewMatchers.withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.searchbar_icon)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.searchbar_textfield)).check(matches(ViewMatchers.withText("")))
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
    }
}