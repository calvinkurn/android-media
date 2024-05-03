package com.tokopedia.product.preview.robot

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalyticsImpl
import com.tokopedia.content.product.preview.domain.repository.ProductPreviewRepository
import com.tokopedia.content.product.preview.utils.ProductPreviewSharedPreferences
import com.tokopedia.content.product.preview.view.fragment.ProductFragment
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.view.fragment.ReviewFragment
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import com.tokopedia.content.test.cassava.containsEvent
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.content.test.espresso.recyclerViewSmoothScrollTo
import com.tokopedia.product.preview.factory.ProductPreviewFragmentFactoryUITest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Rule
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.content.product.preview.R as contentproductpreviewR
import com.tokopedia.empty_state.R as empty_stateR

internal class ProductPreviewViewModelUiTestRobot(
    private val context: Context,
    private val composeTestRule: ComposeTestRule,
    private val productPreviewSourceModel: ProductPreviewSourceModel,
    private val repo: ProductPreviewRepository,
    private val userSession: UserSessionInterface,
    private val productPreviewPref: ProductPreviewSharedPreferences,
    private val router: Router
) {

    @get:Rule
    var cassavaTest = CassavaTestRule(sendValidationResult = false)

    private val cassavaPath = "tracker/content/productpreview/product_preview.json"

    private val viewModel: ProductPreviewViewModel by lazy {
        ProductPreviewViewModel(
            productPreviewSource = productPreviewSourceModel,
            repo = repo,
            userSessionInterface = userSession,
            productPrevSharedPref = productPreviewPref
        )
    }

    private val viewModelFactory = object : ProductPreviewViewModelFactory.Creator {
        override fun create(
            productPreviewSource: ProductPreviewSourceModel
        ): ProductPreviewViewModelFactory {
            return ProductPreviewViewModelFactory(
                productPreviewSource = productPreviewSource,
                factory = object : ProductPreviewViewModel.Factory {
                    override fun create(
                        productPreviewSource: ProductPreviewSourceModel
                    ): ProductPreviewViewModel {
                        return viewModel
                    }
                }
            )
        }
    }

    private val productPreviewAnalyticsFactory = object : ProductPreviewAnalytics.Factory {
        override fun create(productId: String): ProductPreviewAnalytics {
            return ProductPreviewAnalyticsImpl(
                productId = productId,
                analyticManagerFactory = object : ContentAnalyticManager.Factory {
                    override fun create(
                        businessUnit: String,
                        eventCategory: String
                    ): ContentAnalyticManager {
                        return ContentAnalyticManager(
                            userSession = userSession,
                            businessUnit = BusinessUnit.content,
                            eventCategory = EventCategory.unifiedViewPDP
                        )
                    }
                }
            )
        }
    }

    private val fragmentFactory = ProductPreviewFragmentFactoryUITest(
        mapOf(
            ProductPreviewFragment::class.java to {
                ProductPreviewFragment(
                    viewModelFactory = viewModelFactory,
                    router = router,
                    analyticsFactory = productPreviewAnalyticsFactory
                )
            },
            ProductFragment::class.java to {
                ProductFragment(
                    analyticsFactory = productPreviewAnalyticsFactory
                )
            },
            ReviewFragment::class.java to {
                ReviewFragment(
                    analyticsFactory = productPreviewAnalyticsFactory,
                    router = router,
                    abTestPlatform = mockk(relaxed = true)
                )
            }
        )
    )

    private val scenario = launchFragmentInContainer<ProductPreviewFragment>(
        factory = fragmentFactory,
        themeResId = empty_stateR.style.AppTheme
    )

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    fun validateEvent(eventName: String) = chainable {
        ViewMatchers.assertThat(
            cassavaTest.validate(cassavaPath),
            containsEvent(eventName)
        )
    }

    fun validateEventAction(eventAction: String) = chainable {
        ViewMatchers.assertThat(
            cassavaTest.validate(cassavaPath),
            containsEventAction(eventAction)
        )
    }

    fun showProductAndReviewTab() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_product_tab_title))
            .check(matches(isDisplayed()))

        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .check(matches(isDisplayed()))
    }

    fun onSwipeProductContent() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.rv_media_product))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(1))

        Espresso
            .onView(withId(contentproductpreviewR.id.rv_media_product))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))

        Espresso
            .onView(withId(contentproductpreviewR.id.rv_media_product))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
    }

    fun onClickTab() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_product_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.tv_product_tab_title))
            .perform(click())
    }

    fun onSwipeTab() = chainable {
        Espresso.onView(withId(contentproductpreviewR.id.vp_product_preview))
            .perform(swipeLeft())
            .perform(swipeRight())
            .perform(swipeLeft())
    }

    fun onClickProductThumbnail() = chainable {
        Espresso.onView(withId(contentproductpreviewR.id.rv_thumbnail_product))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
    }

    fun showATCRemindMe() = chainable {
        composeTestRule.onNodeWithTag(getString(contentproductpreviewR.string.product_prev_test_tag_atc_button))
            .assertIsDisplayed()
    }

    fun onClickAtcButton() = chainable {
        composeTestRule.onNodeWithTag(getString(contentproductpreviewR.string.product_prev_test_tag_atc_button))
            .assertIsDisplayed()
            .performClick()
    }

    fun onClickNavigationButton() = chainable {
        composeTestRule.onNodeWithTag(getString(contentproductpreviewR.string.product_prev_test_tag_navigation_container))
            .assertIsDisplayed()
            .performClick()
    }

    fun onSwipeReviewContentFromProductPage() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        recyclerViewSmoothScrollTo(
            rvId = contentproductpreviewR.id.rv_review,
            position = 1
        )

        recyclerViewSmoothScrollTo(
            rvId = contentproductpreviewR.id.rv_review,
            position = 2
        )
    }

    fun onClickReviewAccountName() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.layout_author_review))
            .perform(click())
    }

    fun onClickReviewThreeDots() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.iv_review_menu))
            .perform(click())
    }

    fun onClickBackButton() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.ic_back))
            .perform(click())
    }

    fun onClickReviewReport() = chainable {
        composeTestRule.onAllNodesWithTag(getString(contentcommonR.string.content_test_tag_three_dots_screen))
            .onLast()
            .assertIsDisplayed()
            .performClick()
    }

    fun onClickReviewWatchMode() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.iv_review_menu))
            .perform(click())

        composeTestRule.onAllNodesWithTag(getString(contentcommonR.string.content_test_tag_three_dots_screen))
            .onFirst()
            .assertIsDisplayed()
            .performClick()
    }

    fun onClickPauseOrPlayVideo() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.video_product_container))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.video_product_container))
            .perform(click())
    }

    fun onClickSubmitReport() = chainable {
        composeTestRule.onAllNodesWithTag(getString(contentproductpreviewR.string.product_prev_test_tag_option_report))
            .onFirst()
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithTag(getString(contentproductpreviewR.string.product_prev_test_tag_submit_report))
            .assertIsDisplayed()
            .performClick()
    }

    fun onClickLikeUnLike() = chainable {
        Espresso
            .onView(withId(contentproductpreviewR.id.tv_review_tab_title))
            .perform(click())

        Espresso
            .onView(withId(contentproductpreviewR.id.layout_like_review))
            .perform(click())
    }

    private fun getString(id: Int): String {
        return context.getString(id)
    }

    private fun chainable(fn: () -> Unit): ProductPreviewViewModelUiTestRobot {
        fn()
        return this
    }
}
