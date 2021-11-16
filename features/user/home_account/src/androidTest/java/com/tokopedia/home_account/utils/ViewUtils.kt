package com.tokopedia.home_account.utils

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.home_account.R
import com.tokopedia.home_account.view.adapter.viewholder.BalanceAndPointItemViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.MemberItemViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.ProfileViewHolder
import com.tokopedia.home_account.view.adapter.viewholder.SettingViewHolder
import com.tokopedia.test.application.espresso_component.CommonAssertion
import org.hamcrest.CoreMatchers
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
        return object: BoundedMatcher<RecyclerView.ViewHolder, ProfileViewHolder>(ProfileViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: ProfileViewHolder): Boolean {
                return item.getMemberTitle().equals(title, ignoreCase = true)
            }

        }
    }

    fun withTitleMemberItemViewHolder(title: String): Matcher<RecyclerView.ViewHolder> {
        return object: BoundedMatcher<RecyclerView.ViewHolder, MemberItemViewHolder>(MemberItemViewHolder::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("view holder with title: $title")
            }

            override fun matchesSafely(item: MemberItemViewHolder): Boolean {
                return item.getSubTitle().equals(title, ignoreCase = true)
            }

        }
    }

    fun checkSettingViewIsDisplayed(title: String, totalItem: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.home_account_user_fragment_rv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val matcher = withSettingViewHolder(title)
        Espresso.onView(ViewMatchers.withId(R.id.home_account_user_fragment_rv)).perform(RecyclerViewActions.scrollToHolder(matcher))

        Espresso.onView(
                CoreMatchers.allOf(
                        ViewMatchers.isDescendantOfA(CoreMatchers.allOf(
                                ViewMatchers.withId(R.id.home_account_expandable_layout_container),
                                ViewMatchers.hasDescendant(ViewMatchers.withText(title))
                        )),
                        ViewMatchers.withId(R.id.home_account_expandable_layout_rv),
                )
        ).check(CommonAssertion.RecyclerViewItemCountAssertion(totalItem))
    }
}