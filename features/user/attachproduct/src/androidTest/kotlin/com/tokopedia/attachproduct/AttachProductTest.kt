package com.tokopedia.attachproduct

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.fake.di.*
import com.tokopedia.attachproduct.fake.view.AttachProductTestActivity
import com.tokopedia.attachproduct.test.R
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.attachproduct.view.viewholder.AttachProductListItemViewHolder
import com.tokopedia.test.application.espresso_component.CommonAssertion
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AttachProductTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        AttachProductTestActivity::class.java, false, false)

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private val dep = AttachProductDepedency()

    @Before
    fun before() {
        fakeBaseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext)).build()
        fakeBaseComponent?.inject(dep)
        fakeComponent = DaggerFakeAttachProductComponent.builder().fakeBaseAppComponent(
            fakeBaseComponent!!).fakeAttachProductModule(FakeAttachProductModule(dep.repositoryStub)).build()
    }

    @After
    fun after() {
        fakeBaseComponent = null
        fakeBaseComponent = null
    }

    @Test
    fun should_render_empty_view_when_list_empty() {

        //GIVEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_empty, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, response)
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(com.tokopedia.baselist.R.id.text_view_empty_content_text)).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.baselist.R.id.no_result_image)).check(matches(isDisplayed()))
    }

    @Test
    fun showing_not_empty_products_and_showing_10_products() {
        //GIVEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, response)
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(CommonAssertion.RecyclerViewItemCountAssertion(10))
    }

    @Test
    fun showing_button_send_product_when_check_product() {

        //WHEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, response)
        activityTestRule.launchActivity(Intent())

        //GIVEN
        performClickOnProductCard(R.id.recycler_view)

        //THEN
        onView(withId(R.id.send_button_attach_product)).check(matches(isDisplayed()))
        onView(withId(R.id.send_button_attach_product)).check(matches(withText("Kirim (1/3)")))
    }

    @Test
    fun showing_search_bar_and_search_product() {

        //WHEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, response)
        activityTestRule.launchActivity(Intent())

        //GIVEN
        val text = "pr0duct t3st variant 2 100 v4r"
        onView(withId(R.id.search_input_view)).check(matches(isDisplayed()))

        onView(withId(R.id.search_input_view)).perform(ViewActions.click())

        onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(text))

        val responseFilter = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product_filter, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, responseFilter)

        Thread.sleep(1000)
        onView(withId(R.id.searchbar_textfield)).perform(pressImeActionButton())

        //THEN
        Thread.sleep(2000)
        onView(withId(R.id.recycler_view)).check(CommonAssertion.RecyclerViewItemCountAssertion(1))

    }

    @Test
    fun swipe_up_should_refresh() {
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        dep.repositoryStub.setResultData(AceSearchProductResponse::class.java, response)
        activityTestRule.launchActivity(Intent())

        onView(withId(R.id.swipe_refresh_layout)).perform(withCustomConstraints(
            swipeDown(), isDisplayingAtLeast(90)))
    }

    private fun performClickOnProductCard(@IdRes recyclerViewId: Int) {
        val viewAction =
            RecyclerViewActions.actionOnItemAtPosition<AttachProductListItemViewHolder>(
                0, ViewActions.click()
            )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
            .perform(viewAction)
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

    companion object {
        var fakeComponent: FakeAttachProductComponent? = null
        var fakeBaseComponent: FakeBaseAppComponent? = null
    }
}