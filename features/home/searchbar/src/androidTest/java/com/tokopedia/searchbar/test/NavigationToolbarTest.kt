package com.tokopedia.searchbar.test

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.searchbar.NavigationToolbarActivity
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.core.IsNot.not
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


class NavigationToolbarTest {
    @get:Rule
    var activityRule: ActivityTestRule<NavigationToolbarActivity> = ActivityTestRule(NavigationToolbarActivity::class.java)

    val context = InstrumentationRegistry.getInstrumentation().context

    // Configurable icon list
    @Test
    fun testWhenSetIcon_NavToolbar_shouldUpdateIconList() {
        var currentIconCount = 0
        val ICON_COUNT_INITIAL = 0
        val ICON_COUNT_AFTER_UPDATE = 4

        val navToolbar: NavToolbar = getNavToolbar()
        val navToolbarIconRv: RecyclerView = getNavToolbarRecyclerView()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            //assert initial icon count
            currentIconCount = navToolbarIconRv.layoutManager?.itemCount?:0
            Assert.assertEquals(ICON_COUNT_INITIAL, currentIconCount)

            //set 3 icon
            navToolbar.setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_NOTIFICATION) {}
                            .addIcon(IconList.ID_MESSAGE) {}
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
                            .addIcon(IconList.ID_NAV_LOTTIE_WISHLIST) {}
            )
        }

        Thread.sleep(1000)
        val viewIcon1 = navToolbarIconRv.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<ImageView>(R.id.nav_icon_image)
        val viewIcon2 = navToolbarIconRv.findViewHolderForAdapterPosition(1)?.itemView?.findViewById<ImageView>(R.id.nav_icon_image)
        val viewIcon3 = navToolbarIconRv.findViewHolderForAdapterPosition(2)?.itemView?.findViewById<ImageView>(R.id.nav_icon_image)
        val viewIcon4 = navToolbarIconRv.findViewHolderForAdapterPosition(3)?.itemView?.findViewById<LottieAnimationView>(R.id.nav_icon_lottieav)

        onView(CommonMatcher.withTagStringValue(IconList.ID_NOTIFICATION.toString())).check(matches(isDisplayed()))
        onView(CommonMatcher.withTagStringValue(IconList.ID_MESSAGE.toString())).check(matches(isDisplayed()))
        onView(CommonMatcher.withTagStringValue(IconList.ID_NAV_GLOBAL.toString())).check(matches(isDisplayed()))
        onView(CommonMatcher.withTagStringValue(IconList.ID_NAV_LOTTIE_WISHLIST.toString())).check(matches(isDisplayed()))

        currentIconCount = navToolbarIconRv.layoutManager?.itemCount?:0
        Assert.assertEquals(IconList.ID_NOTIFICATION.toString(), viewIcon1?.tag.toString())
        Assert.assertEquals(IconList.ID_MESSAGE.toString(), viewIcon2?.tag.toString())
        Assert.assertEquals(IconList.ID_NAV_GLOBAL.toString(), viewIcon3?.tag.toString())
        Assert.assertEquals(IconList.ID_NAV_LOTTIE_WISHLIST.toString(), viewIcon4?.tag.toString())
        Assert.assertEquals(ICON_COUNT_AFTER_UPDATE, currentIconCount)
    }

    // Configurable icon list
    @Test
    fun testWhenSetCounterOnIcon_NavToolbar_shouldUpdateCounterNumberOnlyOnSelectedIcon() {
        val navToolbar: NavToolbar = getNavToolbar()
        val navToolbarIconRv: RecyclerView = getNavToolbarRecyclerView()
        val defaultCounter = 99

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            //set 3 icon
            navToolbar.setIcon(IconBuilder().addIcon(IconList.ID_NOTIFICATION) {}.addIcon(IconList.ID_CART) {})
            navToolbar.setBadgeCounter(IconList.ID_NOTIFICATION, defaultCounter)
        }
        Thread.sleep(1000)

        onView(CommonMatcher.withTagStringValue(getCounterTagById(IconList.ID_NOTIFICATION))).check(matches(isDisplayed()))
        onView(CommonMatcher.withTagStringValue(getCounterTagById(IconList.ID_NOTIFICATION))).check(matches(withText(defaultCounter.toString())))

        onView(CommonMatcher.withTagStringValue(getCounterTagById(IconList.ID_CART))).check(matches(not(isDisplayed())))
    }

    // Back button
    @Test
    fun testWhenSetBackButtonTypeBack_NavToolbar_shouldShowTypeBackButton() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_BACK)
        }
        val backView = navToolbar.findViewById<ImageView>(R.id.nav_icon_back)
        onView(withId(R.id.nav_icon_back)).check(matches(isDisplayed()))
        Assert.assertEquals(NavToolbar.Companion.BackType.BACK_TYPE_BACK, backView?.tag)
    }

    @Test
    fun testWhenSetBackButtonTypeClose_NavToolbar_shouldShowTypeCloseButton() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_CLOSE)
        }
        val backView = navToolbar.findViewById<ImageView>(R.id.nav_icon_back)
        onView(withId(R.id.nav_icon_back)).check(matches(isDisplayed()))
        Assert.assertEquals(NavToolbar.Companion.BackType.BACK_TYPE_CLOSE, backView?.tag)
    }

    @Test
    fun testWhenSetBackButtonTypeNone_NavToolbar_shouldNotShowBackButton() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
        }
        onView(withId(R.id.nav_icon_back)).check(matches(not(isDisplayed())))
    }

    // Searchbar setup
    @Test
    fun testWhenSetupSearchbarWithHint_NavToolbar_shouldShowSearchbarWithHint() {
        val HINT_VALUE = "This is hint data.."
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setupSearchbar(hints = listOf(HintData(HINT_VALUE, HINT_VALUE)))
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
        }

        onView(withId(R.id.layout_search)).check(matches(isDisplayed()))
        onView(withId(R.id.et_search)).check(matches(isDisplayed()))
        onView(withId(R.id.et_search)).check(matches(withHint(HINT_VALUE)));
    }

    // Toolbar title setup
    @Test
    fun testWhenSetToolbarTitle_NavToolbar_shouldShowToolbarWithTitle() {
        val TITLE_VALUE = "This is hint data.."
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setToolbarTitle(TITLE_VALUE)
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
        }

        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.toolbar_title)).check(matches(withText(TITLE_VALUE)));
    }

    // Switch toolbar content at runtime
    @Test
    fun testWhenSetToolbarContentToTitle_NavToolbar_shouldOnlyShowTitleContent() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_TITLE)
        }

        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.et_search)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layout_custom_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testWhenSetToolbarContentToSearchbar_NavToolbar_shouldOnlyShowSearchbarContent() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_SEARCH)
        }

        onView(withId(R.id.toolbar_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.et_search)).check(matches(isDisplayed()))
        onView(withId(R.id.layout_custom_view)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testWhenSetToolbarContentToCustomView_NavToolbar_shouldOnlyShowCustomViewContent() {
        val navToolbar: NavToolbar = getNavToolbar()

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
        }

        onView(withId(R.id.toolbar_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.et_search)).check(matches(not(isDisplayed())))
        onView(withId(R.id.layout_custom_view)).check(matches(isDisplayed()))
    }

    // Set custom toolbar content view
    @Test
    fun testWhenSetCustomViewContent_NavToolbar_shouldShowCustomView() {
        val CUSTOM_TEXT_DUMMY_VALUE = "This is custom view.."
        val customViewTag = "customTextView"
        val navToolbar: NavToolbar = getNavToolbar()
        val activity = activityRule.activity

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
            val customTextView = TextView(activity)
            customTextView.tag = customViewTag

            customTextView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            customTextView.text = CUSTOM_TEXT_DUMMY_VALUE

            navToolbar.setCustomViewContentView(customTextView)
            navToolbar.setToolbarContentType(NavToolbar.Companion.ContentType.TOOLBAR_TYPE_CUSTOM)
        }

        onView(CommonMatcher.withTagStringValue(customViewTag)).check(matches(isDisplayed()))
        onView(CommonMatcher.withTagStringValue(customViewTag)).check(matches(withText(CUSTOM_TEXT_DUMMY_VALUE)));
    }

    private fun getNavToolbar(): NavToolbar  =
            activityRule.activity.findViewById(R.id.test_navigation_toolbar)

    private fun getNavToolbarRecyclerView(): RecyclerView =
            activityRule.activity.findViewById(R.id.rv_icon_list)

    private fun getCounterTagById(id: Int) =
            context.getString(com.tokopedia.searchbar.R.string.tag_counter_id) + id
}
