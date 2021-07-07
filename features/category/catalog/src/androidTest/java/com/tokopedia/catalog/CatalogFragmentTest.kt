package com.tokopedia.catalog

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.catalog.ui.activity.CatalogDetailPageActivity
import com.tokopedia.catalog.utils.CatalogViewActions
import com.tokopedia.catalog.utils.TkpdIdlingResource
import com.tokopedia.catalog.utils.TkpdIdlingResourceProvider
import com.tokopedia.catalog.utils.actionTest
import com.tokopedia.catalog.viewholder.components.CatalogImagesViewHolder
import com.tokopedia.catalog.viewholder.components.SpecificationsViewHolder
import com.tokopedia.catalog.viewholder.components.VideosViewHolder
import com.tokopedia.discovery.common.constants.SearchConstant.EXTRA_CATALOG_ID
import com.tokopedia.test.application.espresso_component.CommonActions
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
class CatalogFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(CatalogDetailPageActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource : TkpdIdlingResource? = null


    @Before
    fun setUp(){
        clearData()
        login()
        launchActivity()
        setupIdlingResource()
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }

//    @Test
//    fun check_catalog_product_section(){
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.product_name),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_image_gallery_section(){
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_images_rv),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_images_rv).let {
//            it.adapter!!.itemCount
//        }
//        if(itemCount > 1){
//            assert(true)
//        }else {
//            assert(false)
//        }
//    }

    @Test
    fun check_image_gallery_opening(){
        actionTest {
            onView(CommonMatcher.firstView(AllOf.allOf(
                    withId(R.id.catalog_images_rv),
                    ViewMatchers.isDisplayed())))
            val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_images_rv).let {
                it.adapter!!.itemCount
            }
            if(itemCount > 1){
                onView(withId(R.id.catalog_images_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<CatalogImagesViewHolder>(0, CommonActions.clickChildViewWithId(R.id.catalog_image_root)))
                onView(CommonMatcher.firstView(AllOf.allOf(
                        withId(R.id.cross),
                        ViewMatchers.isDisplayed())))
            }else {
                assert(false)
            }
        }.assertTest {
            validate(gtmLogDBSource, context, CATALOG_ANALYTICS)
            clearData()
        }
    }

    @Test
    fun check_image_gallery_swipe(){
        actionTest {
            onView(CommonMatcher.firstView(AllOf.allOf(
                    withId(R.id.catalog_images_rv),
                    ViewMatchers.isDisplayed())))
            val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_images_rv).let {
                it.adapter!!.itemCount
            }
            if(itemCount > 1){
                Thread.sleep(3000)
                onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_images_rv))), ViewActions.scrollTo()))
                Thread.sleep(3000)
                onView(withId(R.id.catalog_images_rv)).perform(CatalogViewActions.ScrollToBottomAction())
                Thread.sleep(3000)
            }else {
                assert(false)
            }
        }.assertTest {
            validate(gtmLogDBSource, context, CATALOG_ANALYTICS)
            clearData()
        }
    }

//    @Test
//    fun check_description_component(){
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.product_description),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_lihat_description_page_opening(){
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_specification_rv))), ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.view_more_description),
//                ViewMatchers.isDisplayed())))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.view_more_description),
//                ViewMatchers.isDisplayed()))
//        ).perform(ViewActions.click())
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.view_pager_specs),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_specification_component(){
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_specification_rv))), ViewActions.scrollTo()))
//        onView(withId(R.id.catalog_specification_rv)).perform(CatalogViewActions.ScrollToBottomAction())
//    }
//
//
//    @Test
//    fun check_lihat_specifications_page_opening(){
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_specification_rv))), ViewActions.scrollTo()))
//        onView(withId(R.id.catalog_specification_rv)).perform(CatalogViewActions.ScrollToBottomAction())
//        Thread.sleep(2000)
//        val viewInteraction = onView(withId(R.id.catalog_specification_rv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_specification_rv).let {
//            it.adapter!!.itemCount
//        }
//        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<SpecificationsViewHolder>(itemCount - 1, CommonActions.clickChildViewWithId(R.id.catalog_specifications_card_parent)))
//        Thread.sleep(2000)
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.view_pager_specs),
//                ViewMatchers.isDisplayed())))
//        Thread.sleep(2000)
//        onView(CommonMatcher.firstView(AllOf.allOf(withId(R.id.bottom_sheet_close)))).perform(ViewActions.click())
//        onView(CommonMatcher.firstView(AllOf.allOf(withId(R.id.catalog_specification_rv),ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_videos_section(){
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_videos_rv))), ViewActions.scrollTo()))
//        Thread.sleep(2000)
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_videos_rv),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_videos_rv).let {
//            it.adapter!!.itemCount
//        }
//        if(itemCount > 1){
//            assert(true)
//        }else {
//            assert(false)
//        }
//    }
//
//    @Test
//    fun check_video_section_opening(){
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_videos_rv))), ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.catalog_videos_rv),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.catalog_videos_rv).let {
//            it.adapter!!.itemCount
//        }
//        if(itemCount > 1){
//            onView(withId(R.id.catalog_videos_rv)).perform(RecyclerViewActions.actionOnItemAtPosition<VideosViewHolder>(0, CommonActions.clickChildViewWithId(R.id.video_thumbnail_iv)))
//            Thread.sleep(3000)
//            onView(CommonMatcher.firstView(AllOf.allOf(
//                    withId(R.id.youtube_player_main),
//                    ViewMatchers.isDisplayed())))
//        }else {
//            assert(false)
//        }
//    }
//
//    @Test
//    fun check_comparison_section() {
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_videos_rv))), ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.first_catalog_image),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_comparison_section_opening() {
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.catalog_comparision_rv))), ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.second_catalog_image),
//                ViewMatchers.isDisplayed())))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.comparision_card),
//                ViewMatchers.isDisplayed()))).perform(ViewActions.click())
//        Thread.sleep(3000)
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.product_name),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_product_listing_section() {
//        onView(withId(R.id.catalog_detail_rv)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.products_container_frame))), ViewActions.scrollTo()))
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.search_product_quick_sort_filter),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_quick_filter_product_listing_bottom_sheet() {
//        launchProductListingBottomSheet()
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.search_product_quick_sort_filter),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_dynamic_filter_product_listing_bottom_sheet() {
//        launchProductListingBottomSheet()
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.sort_filter_prefix),
//                ViewMatchers.isDisplayed()))).perform(ViewActions.click())
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.bottom_sheet_header),
//                ViewMatchers.isDisplayed())))
//    }
//
//    @Test
//    fun check_apply_quick_filter_product_listing_bottom_sheet() {
//        launchProductListingBottomSheet()
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.sort_filter_items),
//                ViewMatchers.isDisplayed())))
//
//    }
//
//    @Test
//    fun check_click_on_product_product_listing_bottom_sheet() {
//        launchProductListingBottomSheet()
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.product_recyclerview),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.product_recyclerview).let {
//            it.adapter!!.itemCount
//        }
//        if(itemCount > 1){
//            onView(withId(R.id.product_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<CatalogImagesViewHolder>(0, CommonActions.clickChildViewWithId(R.id.catalog_product_card)))
//        }else {
//            assert(false)
//        }
//
//    }
//
//    @Test
//    fun check_add_remove_wish_list_product_listing_bottom_sheet() {
//        launchProductListingBottomSheet()
//        onView(CommonMatcher.firstView(AllOf.allOf(
//                withId(R.id.product_recyclerview),
//                ViewMatchers.isDisplayed())))
//        val itemCount = activityRule.activity.findViewById<RecyclerView>(R.id.product_recyclerview).let {
//            it.adapter!!.itemCount
//        }
//        if(itemCount > 1){
//            onView(withId(R.id.product_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition<CatalogImagesViewHolder>(0, CommonActions.clickChildViewWithId(R.id.imageThreeDots)))
//        }else {
//            assert(false)
//        }
//    }

    private fun launchProductListingBottomSheet(){
        onView(withId(R.id.bottom_sheet_fragment_container))
                .perform(
                        CatalogViewActions.withCustomConstraints(
                                GeneralSwipeAction(
                                        Swipe.FAST,
                                        GeneralLocation.VISIBLE_CENTER,
                                        CoordinatesProvider { view: View -> floatArrayOf(view.width / 2.toFloat(), 0f) },
                                        Press.FINGER),
                                ViewMatchers.isDisplayingAtLeast(5)))
        Thread.sleep(3000)
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString(EXTRA_CATALOG_ID, "53169")
        val intent = Intent(context, CatalogDetailPageActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
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

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    companion object {
        const val CATALOG_ANALYTICS = "tracker/catalog/catalog_tracking.json"
    }
}