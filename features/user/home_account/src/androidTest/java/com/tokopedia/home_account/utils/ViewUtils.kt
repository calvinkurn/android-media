package com.tokopedia.home_account.utils

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.adapter.viewholder.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf


object ViewUtils {
    fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder: RecyclerView.ViewHolder =
                        view.findViewHolderForAdapterPosition(position)
                                ?: // has no item on such position
                                return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }

    fun withSettingViewHolder(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, SettingViewHolder>(SettingViewHolder::class.java) {
            override fun matchesSafely(item: SettingViewHolder): Boolean {
                return item.getTitle() == title
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }

    fun withTitleBalanceViewHolder(title: String, matcher: (item: BalanceAndPointItemViewHolder, title: String) -> Boolean): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, BalanceAndPointItemViewHolder>(BalanceAndPointItemViewHolder::class.java) {
            override fun matchesSafely(item: BalanceAndPointItemViewHolder): Boolean {
                return matcher(item, title)
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }

    fun withTitleProfileViewHolder(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, ProfileViewHolder>(ProfileViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: ProfileViewHolder): Boolean {
                return item.getMemberTitle().equals(title, ignoreCase = true)
            }

        }
    }

    fun withTitleMemberItemViewHolder(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, MemberItemViewHolder>(MemberItemViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: MemberItemViewHolder): Boolean {
                return item.getSubTitle().equals(title, ignoreCase = true)
            }

        }
    }

    fun clickWalletViewHolder(title: String) {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        val matcher: Matcher<RecyclerView.ViewHolder> = withTitleBalanceViewHolder(title) { item, title ->
            item.getSubTitle().equals(title, ignoreCase = true)
        }
        Espresso.onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher), actionOnHolderItem(matcher, click()))
    }

    fun withSettingItemViewHolder(title: String): Matcher<RecyclerView.ViewHolder> {
        return object : BoundedMatcher<RecyclerView.ViewHolder, CommonViewHolder>(CommonViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")

            }

            override fun matchesSafely(item: CommonViewHolder): Boolean {
                return item.getTitle().equals(title, ignoreCase = true)
            }

        }
    }

    fun clickSettingView(settingType: String, title: String) {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        val matcher: Matcher<RecyclerView.ViewHolder> = withSettingItemViewHolder(title)

        Espresso.onView(allOf(
                isDescendantOfA(allOf(
                        withId(R.id.home_account_expandable_layout_container),
                        hasDescendant(allOf(
                                withText(settingType),
                        )),
                )),
                withId(R.id.home_account_expandable_layout_rv)

        )).perform(scrollToHolder(matcher), actionOnHolderItem(matcher, click()))
    }

    fun clickSettingMoreView(settingType: String) {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Espresso.onView(withId(R.id.home_account_user_fragment_rv)).perform(ViewActions.swipeUp())

        Espresso.onView(allOf(
                isDescendantOfA(allOf(
                        withId(R.id.home_account_expandable_layout_container),
                        hasDescendant(allOf(
                                withText(settingType),
                        )),
                )),
                withId(R.id.home_account_expandable_arrow)

        )).perform(click())
    }

    fun clickSwitchOnApplicationSetting(menu: String) {
        Espresso.onView(allOf(
                isDescendantOfA(allOf(
                        withId(R.id.home_account_item_common_layout),
                        hasDescendant(allOf(
                                withText(menu),
                        )),
                )),
                withId(R.id.account_user_item_common_switch),
        )).perform(click(), click())
    }
}