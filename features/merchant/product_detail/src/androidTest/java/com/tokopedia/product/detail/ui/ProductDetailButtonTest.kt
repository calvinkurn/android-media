package com.tokopedia.product.detail.ui

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.ui.base.BaseProductDetailUiTest
import com.tokopedia.product.detail.util.*
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.variant_common.view.holder.VariantImageViewHolder
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Test

/**
 * Created by Yehezkiel on 08/04/21
 * Class PartialButtonActionView
 */
class ProductDetailButtonTest : BaseProductDetailUiTest() {

    private val buttonIdlingResource by lazy {
        ProductDetailNetworkIdlingResource(object : ProductIdlingInterface {
            override fun getName(): String = "networkFinish"

            override fun idleState(): Boolean {
                if (activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.base_btn_action) == null) {
                    throw RuntimeException("button not found")
                }

                return activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.base_btn_action)?.visibility == View.VISIBLE
            }
        })
    }

    override fun before() {
        IdlingRegistry.getInstance().register(buttonIdlingResource)
        setupGraphqlMockResponse(createMockModelConfig())
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(buttonIdlingResource)
    }

    @Test
    fun sticky_login_non_login() {
        InstrumentationAuthHelper.clearUserSession()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_pdp_container)).assertVisible()
        onView(withId(R.id.sticky_login_pdp)).assertVisible()
    }

    @Test
    fun sticky_login_shows_login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1() //given user logged in

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_pdp_container)).assertVisible()
        onView(withId(R.id.sticky_login_pdp)).assertNotVisible()
    }

    @Test
    fun check_empty_button_initial_data() {
        setupGraphqlMockResponse{
            addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context,  com.tokopedia.product.detail.test.R.raw.response_mock_p1_negative_case), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse("GetPdpGetData", InstrumentationMockHelper.getRawString(context,  com.tokopedia.product.detail.test.R.raw.response_mock_p2_negative_case), MockModelConfig.FIND_BY_CONTAINS)
        }

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Stok Kosong")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN && !buttonUnify.isEnabled
                }))

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_buy_atc_button_initial_data() {
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Beli test")))

        onView(withId(R.id.btn_add_to_cart))
                .assertVisible()
                .check(matches(ViewMatchers.withText("+ Keranjang test")))

        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_change_variant_change_to_1_button() {
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)
        clickVariantTest()

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Beli pakai OVO")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.MAIN
                }))

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()

        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()
        support_button_not_visible()
    }

    //region Error case
    @Test
    fun check_default_button_p2_error_product_empty() {
        // else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType)

        setupGraphqlMockResponse {
            addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context, com.tokopedia.product.detail.test.R.raw.response_mock_p1_negative_case), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse("GetPdpGetData", "force error", MockModelConfig.FIND_BY_CONTAINS)
        }

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()
        onView(withId(R.id.btn_apply_leasing)).assertNotVisible()
        onView(withId(R.id.seller_button_container)).assertNotVisible()

        onView(withId(R.id.btn_empty_stock)) .assertVisible()
                .check(matches(ViewMatchers.withText("Stok Habis")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN && !buttonUnify.isEnabled
                }))
    }

    @Test
    fun check_default_button_p2_error() {
        // else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType)

        setupGraphqlMockResponse{
            addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context,  com.tokopedia.product.detail.test.R.raw.response_mock_p1_test), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse("GetPdpGetData", "force error", MockModelConfig.FIND_BY_CONTAINS)
        }

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Beli")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.TRANSACTION
                }))

        onView(withId(R.id.btn_add_to_cart)).assertVisible()
                .check(matches(ViewMatchers.withText("+ Keranjang")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.TRANSACTION
                }))

        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()
        support_button_not_visible()
    }
    //endregion

    //region seller side
    @Test
    fun check_button_seller_side() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1() //given user logged in
        InstrumentationAuthHelper.modifyUserSession {
            it.setIsShopOwner(true)
            it.shopId = "1990266"
        }

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertNotVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.base_btn_affiliate_dynamic)).assertNotVisible()

        onView(withId(R.id.seller_button_container)).assertVisible()
        onView(withId(R.id.btn_edit_product))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Ubah Produk")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.ALTERNATE
                }))

        onView(withId(R.id.btn_top_ads))
                .assertVisible()
                .check(matches(ViewMatchers.withText("Iklankan Produk")))
                .check(matches(ViewAttributeMatcher {
                    val buttonUnify = (it as UnifyButton)
                    buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                }))
    }
    //endregion

    private fun clickVariantTest() {
        onView(withId(R.id.rv_pdp)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(ViewMatchers.hasDescendant(AllOf.allOf(withId(R.id.rvContainerVariant))), ViewActions.scrollTo()))
        val viewInteraction = onView(AllOf.allOf(withId(R.id.rvContainerVariant))).check(matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<VariantImageViewHolder>(0, CommonActions.clickChildViewWithId(R.id.containerChipVariant)))
    }

    private fun support_button_not_visible() {
        onView(withId(R.id.btn_apply_leasing)).assertNotVisible()
        onView(withId(R.id.seller_button_container)).assertNotVisible()
        onView(withId(R.id.btn_empty_stock)).assertNotVisible()
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("pdpGetLayout", InstrumentationMockHelper.getRawString(context,  com.tokopedia.product.detail.test.R.raw.response_mock_p1_test), FIND_BY_CONTAINS)
                addMockResponse("GetPdpGetData", InstrumentationMockHelper.getRawString(context,  com.tokopedia.product.detail.test.R.raw.response_mock_p2_ui_test), FIND_BY_CONTAINS)
                return this
            }
        }
    }
}