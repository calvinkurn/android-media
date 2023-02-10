package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.presentation.InstrumentTestAddToCartBottomSheet
import com.tokopedia.product.detail.util.ProductDetailIdlingResource
import com.tokopedia.product.detail.util.ProductDetailNetworkIdlingResource
import com.tokopedia.product.detail.util.ProductIdlingInterface
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.viewholder.ProductDiscussionQuestionViewHolder
import com.tokopedia.product.detail.view.viewholder.ProductSingleVariantViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.user.session.UserSession
import org.hamcrest.core.AllOf.allOf
import org.junit.*
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductDetailActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: ActivityTestRule<ProductDetailActivity> = IntentsTestRule(
        ProductDetailActivity::class.java,
        false,
        false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val idlingResource by lazy {
        ProductDetailNetworkIdlingResource(object : ProductIdlingInterface {
            override fun getName(): String = "networkFinish"

            override fun idleState(): Boolean {
                val fragment =
                    activityRule.activity.supportFragmentManager.findFragmentByTag("productDetailTag") as DynamicProductDetailFragment
                if (fragment.view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action) == null) {
                    throw RuntimeException("button not found")
                }

                return fragment.view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action)?.visibility == View.VISIBLE
            }
        })
    }

    private val idlingResourceVariant by lazy {
        ProductDetailNetworkIdlingResource(object : ProductIdlingInterface {
            override fun getName(): String = "variantFinish"

            override fun idleState(): Boolean {
                val fragment =
                    activityRule.activity.supportFragmentManager.findFragmentByTag("productDetailTag") as DynamicProductDetailFragment
                val variantPosition = fragment.productAdapter?.currentList?.indexOfFirst {
                    it is ProductSingleVariantDataModel
                } ?: return false

                val variantVh = fragment.getRecyclerView()
                    ?.findViewHolderForAdapterPosition(variantPosition) as? ProductSingleVariantViewHolder
                val vhContainer =
                    variantVh?.view?.findViewById<RecyclerView>(R.id.rv_single_variant)

                return vhContainer?.findViewHolderForAdapterPosition(0) != null
            }
        })
    }

    @Before
    fun setup() {
        setupGraphqlMockResponse(ProductDetailMockResponse())
        clearLogin()

        fakeLogin()
        val intent = ProductDetailActivity.createIntent(targetContext, PRODUCT_ID)
        activityRule.launchActivity(intent)

        setUpTimeoutIdlingResource()
        intendingIntent()
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(
            ProductDetailIdlingResource.idlingResource,
            idlingResource
        )
    }

    @Test
    fun validateClickBuyIsLogin() {
        actionTest {
            fakeLogin()
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            assertIsLoggedIn(true)
            waitForTrackerSent()
            performClose(activityRule)
            validate(cassavaRule, BUTTON_BUY_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickAddToCartIsLogin() {
        actionTest {
            fakeLogin()
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            assertIsLoggedIn(true)
            if (addToCartBottomSheetIsVisible() == true) {
                performClose(activityRule)
                waitForTrackerSent()
                validate(cassavaRule, ADD_TO_CART_LOGIN_PATH)
            } else {
                performClose(activityRule)
            }
        }
    }

    @Test
    fun validateClickBuyIsNonLogin() {
        actionTest {
            clearLogin()
            Thread.sleep(500L)
            clickVariantTest()
            clickBuyNow()
        } assertTest {
            assertIsLoggedIn(false)
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, BUTTON_BUY_NON_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickAddToCartIsNonLogin() {
        actionTest {
            clearLogin()
            Thread.sleep(500L)
            clickVariantTest()
            clickAddToCart()
        } assertTest {
            assertIsLoggedIn(false)
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, ADD_TO_CART_NON_LOGIN_PATH)
        }
    }

    @Test
    fun validateClickTabDiscussion() {
        actionTest {
            fakeLogin()
            clickTabDiscussion()
        } assertTest {
            assertIsLoggedIn(true)
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, DISCUSSION_PRODUCT_TAB_PATH)
        }
    }

    @Test
    fun validateClickSeeAllDiscussion() {
        actionTest {
            fakeLogin()
            clickSeeAllDiscussion()
        } assertTest {
            assertIsLoggedIn(true)
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, SEE_ALL_ON_LATEST_DISCUSSION_PATH)
        }
    }

    @Test
    fun validateClickThreadDetail() {
        actionTest {
            fakeLogin()
            clickThreadDetailDiscussion()
        } assertTest {
            assertIsLoggedIn(true)
            performClose(activityRule)
            waitForTrackerSent()
            validate(cassavaRule, THREAD_DETAIL_ON_DISCUSSION_PATH)
        }
    }

    private fun waitVariantToLoad() {
        IdlingRegistry.getInstance().register(idlingResourceVariant)

        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.rv_single_variant))),
                scrollTo()
            )
        )

        onView(
            allOf(withId(R.id.rv_single_variant))
        ).check(
            matches(isDisplayed())
        )
        IdlingRegistry.getInstance().unregister(idlingResourceVariant)
    }

    private fun clickSeeAllDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.productDiscussionMostHelpfulSeeAll))),
                scrollTo()
            )
        )
        onView(allOf(withId(R.id.productDiscussionMostHelpfulSeeAll)))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun clickThreadDetailDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.productDiscussionMostHelpfulQuestions))),
                scrollTo()
            )
        )
        val viewInteraction =
            onView(withId(R.id.productDiscussionMostHelpfulQuestions)).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductDiscussionQuestionViewHolder>(
                0,
                clickChildViewWithId(R.id.productDetailDiscussionThread)
            )
        )
    }

    private fun clickTabDiscussion() {
        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.mini_social_proof_recycler_view))),
                scrollTo()
            )
        )
        onView(allOf(withId(R.id.chipSocialProofItem)))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun setUpTimeoutIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
        IdlingRegistry.getInstance().register(idlingResource)
        waitVariantToLoad()
    }

    private fun addToCartBottomSheetIsVisible(): Boolean? {
        val addToCartBottomSheet =
            activityRule.activity.supportFragmentManager.findFragmentByTag("ADD_TO_CART") as? InstrumentTestAddToCartBottomSheet
        return addToCartBottomSheet?.isVisible
    }

    private fun clickVariantTest() {
        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.rv_single_variant))),
                scrollTo()
            )
        )
        val viewInteraction = onView(
            allOf(withId(R.id.rv_single_variant))
        ).check(
            matches(isDisplayed())
        )
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductSingleVariantViewHolder>(
                0,
                clickChildViewWithId(R.id.atc_variant_chip)
            )
        )
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductSingleVariantViewHolder>(
                1,
                clickChildViewWithId(R.id.atc_variant_chip)
            )
        )
    }

    private fun waitForTrackerSent() {
        Thread.sleep(500L)
    }

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun clearLogin() {
        InstrumentationAuthHelper.clearUserSession()
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun assertIsLoggedIn(actualIsLoggedIn: Boolean) {
        val userSession = UserSession(targetContext)
        Assert.assertEquals(userSession.isLoggedIn, actualIsLoggedIn)
    }

    companion object {
        const val PRODUCT_ID = "1994427767"
        const val ADD_TO_CART_LOGIN_PATH =
            "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_login.json"
        const val ADD_TO_CART_NON_LOGIN_PATH =
            "tracker/merchant/product_detail/pdp_add_to_cart_choose_variant_non_login.json"
        const val BUTTON_BUY_LOGIN_PATH =
            "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_login.json"
        const val BUTTON_BUY_NON_LOGIN_PATH =
            "tracker/merchant/product_detail/pdp_button_buy_now_choose_variant_non_login.json"
        const val SEE_ALL_ON_LATEST_DISCUSSION_PATH =
            "tracker/merchant/product_detail/pdp_click_see_all_on_latest_discussion.json"
        const val THREAD_DETAIL_ON_DISCUSSION_PATH =
            "tracker/merchant/product_detail/pdp_click_thread_detail_on_discussion.json"
        const val DISCUSSION_PRODUCT_TAB_PATH =
            "tracker/merchant/product_detail/pdp_click_discussion_product_tab.json"
    }
}
