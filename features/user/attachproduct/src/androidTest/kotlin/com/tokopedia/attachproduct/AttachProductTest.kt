package com.tokopedia.attachproduct

import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.attachproduct.data.model.AceSearchProductResponse
import com.tokopedia.attachproduct.fake.di.*
import com.tokopedia.attachproduct.fake.usecase.FakeAttachProductUseCase
import com.tokopedia.attachproduct.fake.view.AttachProductTestActivity
import com.tokopedia.attachproduct.test.R
import com.tokopedia.attachproduct.utils.FileUtils
import com.tokopedia.attachproduct.view.viewholder.AttachProductListItemViewHolder
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class AttachProductTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        AttachProductTestActivity::class.java, false, false)

    @Inject
    lateinit var  useCase: FakeAttachProductUseCase

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private val gtmLogDbSource = GtmLogDBSource(context)


    @Before
    fun before() {
        fakeBaseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext)).build()
        gtmLogDbSource.deleteAll().subscribe()
        fakeComponent = DaggerFakeAttachProductComponent.builder().fakeBaseAppComponent(
            fakeBaseComponent!!).fakeAttachProductModule(FakeAttachProductModule(context)).build()
        fakeComponent?.inject(this)
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
        useCase.response = response
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(com.tokopedia.baselist.R.id.text_view_empty_content_text)).check(matches(isDisplayed()))

        onView(withId(com.tokopedia.baselist.R.id.no_result_image)).check(matches(isDisplayed()))
    }

    @Test
    fun showing_not_empty_products() {
        //GIVEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        useCase.response = response
        activityTestRule.launchActivity(Intent())

        //THEN
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))

    }

    @Test
    fun showing_button_send_product_when_check_product() {

        //WHEN
        val response = FileUtils.parseRaw<AceSearchProductResponse>(R.raw.example_ace_search_product, AceSearchProductResponse::class.java)
        useCase.response = response
        activityTestRule.launchActivity(Intent())

        //GIVEN
        performClickOnProductCard(R.id.recycler_view)

        //THEN
        onView(withId(R.id.send_button_attach_product)).check(matches(isDisplayed()))
        onView(withId(R.id.send_button_attach_product)).check(matches(withText("Kirim (1/3)")))
    }

    @Test
    fun showing_search_bar() {
        activityTestRule.launchActivity(Intent())

        onView(withId(R.id.search_input_view)).check(matches(isDisplayed()))
    }

    private fun performClickOnProductCard(@IdRes recyclerViewId: Int) {
        val viewAction =
            RecyclerViewActions.actionOnItemAtPosition<AttachProductListItemViewHolder>(
                0, ViewActions.click()
            )
        onView(AllOf.allOf(isDisplayed(), withId(recyclerViewId)))
            .perform(viewAction)
    }


    companion object {
        var fakeComponent: FakeAttachProductComponent? = null
        var fakeBaseComponent: FakeBaseAppComponent? = null
    }

//    protected fun waitForFragmentResumed() {
//        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
//        Espresso.onView(ViewMatchers.withId(R.id.recycler_view))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
//    }
}