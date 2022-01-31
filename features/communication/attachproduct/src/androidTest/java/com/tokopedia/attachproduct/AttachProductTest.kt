package com.tokopedia.attachproduct

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.attachproduct.stub.data.GraphqlRepositoryStub
import com.tokopedia.attachproduct.stub.data.TestState
import com.tokopedia.attachproduct.stub.di.*
import com.tokopedia.attachproduct.test.R
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.attachproduct.utils.ViewUtils
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.test.application.espresso_component.CommonAssertion
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class AttachProductTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        AttachProductActivity::class.java, false, false)

    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    lateinit var repositoryStub: GraphqlRepositoryStub

    @Before
    fun before() {
        val fakeBaseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext)).build()
        ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(fakeBaseComponent)
        repositoryStub = fakeBaseComponent!!.repo() as GraphqlRepositoryStub
    }

    @Test
    fun should_render_empty_view_when_list_empty() {

        //GIVEN
        repositoryStub.state = TestState.EMPTY
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(com.tokopedia.baselist.R.id.text_view_empty_content_text)).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.baselist.R.id.no_result_image)).check(matches(isDisplayed()))

    }

    @Test
    fun showing_not_empty_products_and_showing_10_products() {
        //GIVEN
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view)).check(CommonAssertion.RecyclerViewItemCountAssertion(10))

    }

    @Test
    fun showing_button_send_product_when_check_product() {

        //WHEN
        activityTestRule.launchActivity(Intent())

        //GIVEN
        ViewUtils.performClickOnProductCard(R.id.recycler_view)

        //THEN
        onView(withId(R.id.send_button_attach_product)).check(matches(isDisplayed()))
        onView(withId(R.id.send_button_attach_product)).check(matches(withText("Kirim (1/3)")))

    }

    @Test
    fun showing_search_bar_and_search_product() {

        //WHEN
        activityTestRule.launchActivity(Intent())

        //GIVEN
        val text = "pr0duct t3st variant 2 100 v4r"
        onView(withId(R.id.search_input_view)).check(matches(isDisplayed()))

        onView(withId(R.id.search_input_view)).perform(ViewActions.click())

        onView(withId(R.id.searchbar_textfield)).perform(ViewActions.typeText(text))

        repositoryStub.state = TestState.FILTER

        onView(withId(R.id.searchbar_textfield)).perform(pressImeActionButton())

        //THEN
        onView(withId(R.id.recycler_view)).check(CommonAssertion.RecyclerViewItemCountAssertion(1))

    }

    @Test
    fun swipe_up_should_refresh() {

        //WHEN
        activityTestRule.launchActivity(Intent())

        //GIVEN
        onView(withId(R.id.swipe_refresh_layout)).perform(
            ViewUtils.withCustomConstraints(
            swipeDown(), isDisplayingAtLeast(90)))

        //THEN
        onView(withId(R.id.recycler_view)).check(CommonAssertion.RecyclerViewItemCountAssertion(10))

    }
}