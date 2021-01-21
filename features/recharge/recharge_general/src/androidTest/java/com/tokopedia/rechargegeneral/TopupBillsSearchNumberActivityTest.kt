package com.tokopedia.rechargegeneral

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TopupBillsSearchNumberActivityTest {

    @get:Rule
    var mActivityRule: IntentsTestRule<TopupBillsSearchNumberActivity> =
            object : IntentsTestRule<TopupBillsSearchNumberActivity>(TopupBillsSearchNumberActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val favNumbers = arrayListOf<TopupBillsFavNumberItem>()
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, TopupBillsSearchNumberActivity::class.java).apply {
                        putExtra(TopupBillsSearchNumberActivity.EXTRA_CLIENT_NUMBER, ClientNumberType.TYPE_INPUT_TEL)
                        putExtra(TopupBillsSearchNumberActivity.EXTRA_NUMBER, "")
                        putParcelableArrayListExtra(TopupBillsSearchNumberActivity.EXTRA_NUMBER_LIST, favNumbers)
                    }
                }
            }

    @Test
    fun click_clear_on_search_number_view() {
        onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        onView(AllOf.allOf(withText(VALID_PHONE_NUMBER), isDescendantOfA(withId(R.id.topupbills_search_number_rv)))).check(matches(isDisplayed()))
        onView(withId(R.id.searchbar_icon)).perform(ViewActions.click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
    }
}