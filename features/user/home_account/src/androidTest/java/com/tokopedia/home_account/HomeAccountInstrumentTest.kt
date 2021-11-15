package com.tokopedia.home_account

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.utils.ViewUtils
import com.tokopedia.test.application.espresso_component.CommonAssertion
import org.junit.Test

import androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import com.tokopedia.home_account.stub.data.TestState

import org.hamcrest.Matcher


class HomeAccountInstrumentTest : HomeAccountTest() {

    @Test
    fun check_profile_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).check(matches(isDisplayed()))

            //Check name
            onView(withId(R.id.account_user_item_profile_name)).check(matches(withText("Rama Widragama Putra")))

            //Check Phone
            onView(withId(R.id.account_user_item_profile_phone)).check(matches(withText(userSession.phoneNumber)))

            onView(withId(R.id.account_user_item_profile_email)).check(matches(withText(userSession.email)))

        }
    }

    @Test
    fun check_gopay_wallet_is_displayed_when_eligible() {
        runTest {
            onView(withId(R.id.home_account_balance_and_point_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_balance_and_point_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher: Matcher<RecyclerView.ViewHolder> = ViewUtils.withTitleBalanceViewHolder("GoPay \u0026 Coins") { item, title ->
                item.getSubTitle().equals(title, ignoreCase = true)
            }
            onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher))
        }
    }

    @Test
    fun check_gopay_wallet_is_not_displayed_when_not_eligible() {
        repo.testState = TestState.WALLET_NOT_ELIGIBLE

        runTest {
            onView(withId(R.id.home_account_balance_and_point_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_balance_and_point_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(2))
        }
    }

    @Test
    fun check_tokopoints_is_displayed_when_gopay_not_exists() {
        repo.testState = TestState.WALLET_NOT_ELIGIBLE

        runTest {
            onView(withId(R.id.home_account_balance_and_point_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_balance_and_point_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher: Matcher<RecyclerView.ViewHolder> = ViewUtils.withTitleBalanceViewHolder("Tokopoints") { item, title ->
                item.getSubTitle().equals(title, ignoreCase = true)
            }
            onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher))
        }
    }


    @Test
    fun check_ovo_wallet_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_balance_and_point_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_balance_and_point_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher: Matcher<RecyclerView.ViewHolder> = ViewUtils.withTitleBalanceViewHolder("OVO") { item, title ->
                item.getSubTitle().equals(title, ignoreCase = true)
            }
            onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher))
        }
    }

    @Test
    fun check_saldo_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_balance_and_point_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_balance_and_point_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher: Matcher<RecyclerView.ViewHolder> = ViewUtils.withTitleBalanceViewHolder("Saldo Tokopedia") { item, title ->
                item.getSubTitle().equals(title, ignoreCase = true)
            }
            onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher))
        }
    }

    @Test
    fun check_member_status_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).check(matches(isDisplayed()))
            val matcher: Matcher<RecyclerView.ViewHolder> = ViewUtils.withTitleProfileViewHolder("Member Silver 2")
            onView(withId(R.id.home_account_user_fragment_rv)).perform(scrollToHolder(matcher))
            onView(withId(R.id.home_account_member_layout_member_icon)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun check_tokomember_shorcutlist_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_member_layout_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_member_layout_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher = ViewUtils.withTitleMemberItemViewHolder("TokoMember")
            onView(withId(R.id.home_account_member_layout_rv)).perform(scrollToHolder(matcher))
        }
    }

    @Test
    fun check_topquest_shorcutlist_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_member_layout_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_member_layout_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher = ViewUtils.withTitleMemberItemViewHolder("TopQuest")
            onView(withId(R.id.home_account_member_layout_rv)).perform(scrollToHolder(matcher))
        }
    }

    @Test
    fun check_mycoupon_shorcutlist_is_displayed() {
        runTest {
            onView(withId(R.id.home_account_member_layout_rv)).check(matches(isDisplayed()))
            onView(withId(R.id.home_account_member_layout_rv)).check(CommonAssertion.RecyclerViewItemCountAssertion(3))
            val matcher = ViewUtils.withTitleMemberItemViewHolder("Kupon Saya")
            onView(withId(R.id.home_account_member_layout_rv)).perform(scrollToHolder(matcher))
        }
    }
}