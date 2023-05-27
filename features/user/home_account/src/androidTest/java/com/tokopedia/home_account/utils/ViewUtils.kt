package com.tokopedia.home_account.utils

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.adapter.viewholder.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher

object ViewUtils {
    fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder: ViewHolder =
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

    fun withSettingViewHolder(title: String): Matcher<ViewHolder> {
        return object : BoundedMatcher<ViewHolder, SettingViewHolder>(SettingViewHolder::class.java) {
            override fun matchesSafely(item: SettingViewHolder): Boolean {
                return item.getTitle() == title
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }

    fun withTitleBalanceViewHolder(title: String, matcher: (item: BalanceAndPointItemViewHolder, title: String) -> Boolean): Matcher<ViewHolder> {
        return object : BoundedMatcher<ViewHolder, BalanceAndPointItemViewHolder>(BalanceAndPointItemViewHolder::class.java) {
            override fun matchesSafely(item: BalanceAndPointItemViewHolder): Boolean {
                return matcher(item, title)
            }

            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }
        }
    }

    fun withTitleProfileViewHolder(title: String): Matcher<ViewHolder> {
        return object : BoundedMatcher<ViewHolder, ProfileViewHolder>(ProfileViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: ProfileViewHolder): Boolean {
                return item.getMemberTitle().equals(title, ignoreCase = true)
            }
        }
    }

    fun withTitleMemberItemViewHolder(title: String): Matcher<ViewHolder> {
        return object : BoundedMatcher<ViewHolder, MemberItemViewHolder>(MemberItemViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: MemberItemViewHolder): Boolean {
                return item.getSubTitle().equals(title, ignoreCase = true)
            }
        }
    }

    fun clickWalletViewHolder(title: String) {
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
        val matcher: Matcher<ViewHolder> = withTitleBalanceViewHolder(title) { item, title ->
            item.getSubTitle().equals(title, ignoreCase = true)
        }
        onView(withId(R.id.home_account_balance_and_point_rv)).perform(scrollToHolder(matcher), actionOnHolderItem(matcher, click()))
    }

    fun withSettingItemViewHolder(title: String): Matcher<ViewHolder> {
        return object : BoundedMatcher<ViewHolder, CommonViewHolder>(CommonViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: CommonViewHolder): Boolean {
                return item.getTitle().equals(title, ignoreCase = true)
            }
        }
    }

    fun clickSettingView(settingType: String, title: String) {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(R.id.home_account_user_fragment_rv))
            .perform(
                scrollToHolder(object : BoundedMatcher<ViewHolder, SettingViewHolder>(SettingViewHolder::class.java) {
                    override fun describeTo(description: Description?) {
                        description?.appendText(settingType)
                    }
                    override fun matchesSafely(item: SettingViewHolder?): Boolean {
                        return item?.getTitle() == settingType
                    }
                })
            )

        val matcher: Matcher<ViewHolder> = withSettingItemViewHolder(title)
        onView(withId(R.id.home_account_expandable_layout_rv)).perform(scrollToHolder(matcher))
        onView(withText(title)).perform(click())
    }

    fun openPengaturanAplikasi(settingType: String) {
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        val afterPengaturanAplikasiPosition = 6
        onView(withId(R.id.home_account_user_fragment_rv)).perform(scrollToPosition<ViewHolder>(afterPengaturanAplikasiPosition))
        onView(withText(settingType)).perform(click())
        onView(withId(R.id.home_account_user_fragment_rv)).perform(scrollToPosition<ViewHolder>(afterPengaturanAplikasiPosition))
        Thread.sleep(3000)
    }

    fun clickSwitchOnApplicationSetting(menu: String) {
        onView(
            allOf(
                hasSibling(withText(menu)),
                withId(R.id.account_user_item_common_switch)
            )
        ).perform(click(), click())
    }
}
