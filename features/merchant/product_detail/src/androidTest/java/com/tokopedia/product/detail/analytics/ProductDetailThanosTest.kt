package com.tokopedia.product.detail.analytics

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.VariantDataModel
import com.tokopedia.product.detail.util.ProductDetailIdlingResource
import com.tokopedia.product.detail.util.ProductDetailNetworkIdlingResource
import com.tokopedia.product.detail.util.ProductIdlingInterface
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.viewholder.ProductVariantViewHolder
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class ProductDetailThanosTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val idlingResource by lazy {
        ProductDetailNetworkIdlingResource(object : ProductIdlingInterface {
            override fun getName(): String = "networkFinish"

            override fun idleState(): Boolean {
                val fragment = activityRule.activity.supportFragmentManager.findFragmentByTag("productDetailTag") as DynamicProductDetailFragment
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
                val fragment = activityRule.activity.supportFragmentManager.findFragmentByTag("productDetailTag") as DynamicProductDetailFragment
                val variantPosition = fragment.productAdapter?.currentList?.indexOfFirst {
                    it is VariantDataModel
                } ?: return false

                val variantVh = fragment.getRecyclerView()?.findViewHolderForAdapterPosition(variantPosition) as? ProductVariantViewHolder
                val vhContainer = variantVh?.view?.findViewById<RecyclerView>(R.id.rvContainerVariant)

                return vhContainer?.findViewHolderForAdapterPosition(0) != null
            }
        })
    }

    @get:Rule
    var activityRule = IntentsTestRule(ProductDetailActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(isFromNetwork = true,
        sendValidationResult = true)

    @Before
    fun setup() {
        setupGraphqlMockResponse(ProductDetailMockResponse())
        InstrumentationAuthHelper.clearUserSession()
        val intent = ProductDetailActivity.createIntent(targetContext,
            ProductDetailActivityTest.PRODUCT_ID
        )
        activityRule.launchActivity(intent)

        setUpTimeoutIdlingResource()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(
            ProductDetailIdlingResource.idlingResource,
            idlingResource)
    }

    /**
     * view product page
     * impression - modular component
     */
    @Test
    fun tracker_journey_id_56() {
        actionTest {
            InstrumentationAuthHelper.loginInstrumentationTestUser1()
        } assertTest {
            assertIsLoggedIn(targetContext, true)
            waitFor()
            performClose(activityRule)

            assertThat(cassavaTestRule.validate("56"), hasAllSuccess())
        }
    }

    private fun setUpTimeoutIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
        IdlingRegistry.getInstance().register(idlingResource)
        waitVariantToLoad()
    }

    private fun waitVariantToLoad() {
        IdlingRegistry.getInstance().register(idlingResourceVariant)

        onView(withId(R.id.rv_pdp)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                ViewMatchers.hasDescendant(allOf(withId(R.id.rvContainerVariant))),
                ViewActions.scrollTo()
            )
        )

        onView(allOf(withId(R.id.rvContainerVariant))).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(idlingResourceVariant)
    }

}
