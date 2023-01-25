package com.tokopedia.catalog

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.catalog.ui.activity.CatalogDetailPageActivity
import com.tokopedia.catalog.utils.TkpdIdlingResource
import com.tokopedia.catalog.utils.TkpdIdlingResourceProvider
import com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_CATALOG_ID
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CatalogFragmentTest
{

    @get:Rule
    val activityRule = ActivityTestRule(CatalogDetailPageActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: TkpdIdlingResource? = null


    @Before
    fun setUp() {
        login()
        launchActivity()
        setupIdlingResource()
        Thread.sleep(3000)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }

    @Test
    fun check_catalog_product_section() {
        onView(CommonMatcher.firstView(AllOf.allOf(
                withId(R.id.product_name),
                ViewMatchers.isDisplayed())))
    }

//    @Test
//    fun check_image_gallery_opening() {
//        actionTest {
//            Thread.sleep(3000)
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.catalog_images_rv),
//                    ViewMatchers.isDisplayed())))
//            Thread.sleep(3000)
//            val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_images_rv).let {
//                it.adapter!!.itemCount
//            }
//            if (itemCount > 0) {
//                Thread.sleep(3000)
//                onView(withId(R.id.catalog_images_rv))
//                        .perform(RecyclerViewActions.actionOnItemAtPosition<CatalogImagesViewHolder>(0,
//                        CommonActions.clickChildViewWithId(R.id.catalog_image_root)))
//                Thread.sleep(3000)
//                onView(CommonMatcher.firstView(AllOf.allOf(
//                        withId(R.id.cross),
//                        ViewMatchers.isDisplayed())))
//                onView(CommonMatcher.firstView(AllOf.allOf(
//                        withId(R.id.cross),
//                        ViewMatchers.isDisplayed()))).perform(ViewActions.click())
//                Thread.sleep(3000)
//                onView(withId(R.id.catalog_images_rv))
//                        .perform(RecyclerViewActions.actionOnItemAtPosition<CatalogImagesViewHolder>(0,
//                                CommonActions.clickChildViewWithId(R.id.catalog_image_root)))
//                Thread.sleep(3000)
//                onView(CommonMatcher.firstView(AllOf.allOf(
//                        withId(R.id.cross),
//                        ViewMatchers.isDisplayed())))
//            } else {
//                assert(false)
//            }
//        }.assertTest {
//            val query = listOf(
//                    mapOf(
//                            Event.EVENT_KEY to CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
//                            Event.CATEGORY_KEY to CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
//                            Event.ACTION_KEY to Event.ALL_STAR,
//                            Event.LABEL_KEY to Event.ALL_STAR
//                    )
//            )
//            assertThat(cassavaTestRule.validate(query, CassavaTestRule.MODE_SUBSET), hasAllSuccess())
//        }
//    }
//
//    @Test
//    fun check_image_gallery_swipe() {
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_images_rv),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_images_rv).let {
//            it.adapter!!.itemCount
//        }
//        if (itemCount > 0) {
//            Thread.sleep(3000)
//            onView(withId(R.id.catalog_detail_rv))
//                    .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                            ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_images_rv))),
//                            ViewActions.scrollTo()))
//            Thread.sleep(3000)
//            onView(withId(R.id.catalog_images_rv)).perform(CatalogViewActions.ScrollToBottomAction())
//            Thread.sleep(3000)
//        } else {
//
//            assert(false)
//        }
//    }
//
//    @Test
//    fun check_lihat_description_page_opening() {
//        actionTest {
//            onView(withId(R.id.catalog_detail_rv))
//                    .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                            ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_specification_rv))),
//                            ViewActions.scrollTo()))
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.view_more_description),
//                    ViewMatchers.isDisplayed())))
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.view_more_description),
//                    ViewMatchers.isDisplayed()))
//            ).perform(ViewActions.click())
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.view_pager_specs),
//                    ViewMatchers.isDisplayed())))
//        }.assertTest {
//            val query = listOf(
//                    mapOf(
//                            Event.EVENT_KEY to CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
//                            Event.CATEGORY_KEY to CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
//                            Event.ACTION_KEY to CatalogDetailAnalytics.ActionKeys.CLICK_MORE_DESCRIPTION,
//                            Event.LABEL_KEY to Event.ALL_STAR
//                    )
//            )
//            assertThat(cassavaTestRule.validate(query, CassavaTestRule.MODE_SUBSET), hasAllSuccess())
//        }
//    }
//
//    @Test
//    fun check_lihat_specifications_page_opening() {
//        actionTest {
//            Thread.sleep(2000)
//            onView(withId(R.id.catalog_detail_rv))
//                    .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                            ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_specification_rv))),
//                            ViewActions.scrollTo()))
//            Thread.sleep(2000)
//            onView(withId(R.id.catalog_specification_rv)).perform(CatalogViewActions.ScrollToBottomAction())
//            Thread.sleep(2000)
//            val viewInteraction = onView(withId(R.id.catalog_specification_rv))
//                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//            val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_specification_rv)
//                    .let { it.adapter!!.itemCount }
//            viewInteraction.perform(RecyclerViewActions
//                    .actionOnItemAtPosition<SpecificationsViewHolder>(itemCount - 1,
//                            CommonActions.clickChildViewWithId(R.id.catalog_specifications_card_parent)))
//            Thread.sleep(2000)
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.view_pager_specs),
//                    ViewMatchers.isDisplayed())))
//            Thread.sleep(2000)
//            onView(CommonMatcher.firstView(AllOf.allOf(withId(R.id.bottom_sheet_close))))
//                    .perform(ViewActions.click())
//            onView(CommonMatcher.firstView(AllOf.allOf(withId(R.id.catalog_specification_rv),
//                    ViewMatchers.isDisplayed())))
//        }.assertTest {
//            val query = listOf(
//                    mapOf(
//                        Event.EVENT_KEY to CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
//                        Event.CATEGORY_KEY to CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
//                        Event.ACTION_KEY to CatalogDetailAnalytics.ActionKeys.CLICK_MORE_SPECIFICATIONS,
//                        Event.LABEL_KEY to Event.ALL_STAR
//                    )
//            )
//            assertThat(cassavaTestRule.validate(query, CassavaTestRule.MODE_SUBSET), hasAllSuccess())
//        }
//    }
//
//    @Test
//    fun check_videos_section() {
//        Thread.sleep(3000)
//        onView(withId(R.id.catalog_detail_rv))
//                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                        ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_videos_rv))),
//                        ViewActions.scrollTo()))
//        Thread.sleep(2000)
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_videos_rv),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_videos_rv).let {
//            it.adapter!!.itemCount
//        }
//        if (itemCount > 0) {
//            assert(true)
//        } else {
//            assert(false)
//        }
//    }
//
//    @Test
//    fun check_video_section_opening() {
//        Thread.sleep(3000)
//        onView(withId(R.id.catalog_detail_rv))
//                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                        ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_videos_rv))),
//                        ViewActions.scrollTo()))
//        Thread.sleep(3000)
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_videos_rv),
//                ViewMatchers.isDisplayed())))
//        Thread.sleep(3000)
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_videos_rv).let {
//            it.adapter!!.itemCount
//        }
//        if (itemCount > 0) {
//            assert(true)
//        } else {
//
//            assert(false)
//        }
//    }
//
//    @Test
//    fun check_product_listing_section() {
//        onView(withId(R.id.catalog_detail_rv))
//                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
//                        ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.products_container_frame))),
//                        ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.search_product_quick_sort_filter),
//                ViewMatchers.isDisplayed())))
//    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString(EXTRA_CATALOG_ID, "57735")
        val intent = Intent(context, CatalogDetailPageActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
        Thread.sleep(5000)
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("SIMULATION")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    interface Event {
        companion object {
            const val EVENT_KEY = "event"
            const val CATEGORY_KEY = "eventCategory"
            const val ACTION_KEY = "eventAction"
            const val LABEL_KEY = "eventLabel"
            const val ALL_STAR = ".*"
        }
    }
}
