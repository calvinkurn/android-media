package com.tokopedia.topupbills

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.database.Cursor
import android.net.Uri
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
        Intents.intending(AllOf.allOf(hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                hasAction(Intent.ACTION_PICK))
        ).respondWith(createUriContact())
    }

    @Test
    fun click_clear_on_search_number_view() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_search)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        Espresso.onView(ViewMatchers.withId(R.id.image_button_close)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_search)).check(matches(ViewMatchers.withText("")))
    }

    @Test
    fun click_on_contact_picker() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_search)).check(matches(ViewMatchers.withText("")))
        Espresso.onView(ViewMatchers.withId(R.id.btnContactPicker)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_search)).check(matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.edit_text_search)).check(matches(ViewMatchers.withText(VALID_PHONE_BOOK)))
    }

    private fun createUriContact(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.data = getContactUriByName("Tes")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    /**
     * @param contactName should set based on contact in device test
     */
    private fun getContactUriByName(contactName: String): Uri? {
        val cursor: Cursor? = mActivityRule.activity.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null)
        cursor?.let {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    if (name == contactName) {
                        return Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, id)
                    }
                }
            }
        }
        return null
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
    }
}