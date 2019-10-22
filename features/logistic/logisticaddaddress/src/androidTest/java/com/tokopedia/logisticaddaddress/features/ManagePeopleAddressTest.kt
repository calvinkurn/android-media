package com.tokopedia.logisticaddaddress.features

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictViewHolder
import com.tokopedia.logisticaddaddress.features.manage.ManagePeopleAddressActivity
import com.tokopedia.tkpd.R
import com.tokopedia.logisticaddaddress.util.EspressoUtils.childAtPosition
import com.tokopedia.logisticaddaddress.util.EspressoUtils.rvHasItem
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ManagePeopleAddressTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ManagePeopleAddressActivity::class.java)

    @Test
    fun managePeopleAddressTest() {

        // Added a sleep statement to match the app's execution delay.
        Thread.sleep(3000)

        val actionMenuItemView = onView(
                allOf(withId(R.id.action_add_address), withContentDescription("Tambah Alamat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                0),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        Thread.sleep(NETWORK_DELAY)

        onView(withId(R.id.address_type)).perform(replaceText("Rumah Testing X"))
        onView(withId(R.id.receiver_name)).perform(typeText("Team Bob"))
        onView(withId(R.id.address)).perform(typeText("Jl Prof Dr Satrio RT1/RWX"))
        onView(withId(R.id.district_layout)).perform(click())

        Thread.sleep(NETWORK_DELAY)

        val appCompatEditText6 = onView(
                allOf(withId(R.id.edit_text_search),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.search_input_view_content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatEditText6.perform(replaceText("jak"), closeSoftKeyboard())

        Thread.sleep(NETWORK_DELAY)

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition<DistrictViewHolder>(0, click()))

        onView(withId(R.id.postal_code_layout)).perform(click())
        onData(`is`(instanceOf(String::class.java))).atPosition(FIRST_ZIP_ITEM).inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withId(R.id.receiver_phone)).perform(typeText("081234567888"), closeSoftKeyboard())
        onView(withId(R.id.save_button)).perform(click())

        Thread.sleep(NETWORK_DELAY)

        onView(withId(R.id.recycler_view)).perform(scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("Rumah Testing X"))))

        onView(withText("Rumah Testing X")).check(matches(isDisplayed()))
    }

    @Test
    fun delete() {
        Thread.sleep(3000)

        onView(withId(R.id.recycler_view)).perform(scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("Rumah Testing X"))))
        onView(allOf(withText("Hapus"), hasSibling(withText("Rumah Testing X")))).perform(click())
        onView(withText("Ya")).perform(click())

        Thread.sleep(NETWORK_DELAY)

        onView(withId(R.id.recycler_view)).check(matches(not(rvHasItem(hasDescendant(withText("Rumah Testing X"))))))
    }

    companion object {
        const val FIRST_ZIP_ITEM = 1
        const val NETWORK_DELAY = 2000L
    }
}
