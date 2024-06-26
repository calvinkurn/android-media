package com.tokopedia.product.detail.ui

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.annotation.UiThreadTest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.ui.base.BaseProductDetailUiTest
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_MINICART_EMPTY_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_MINICART_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P1_NEGATIVE_CASE_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P1_NON_VARIANT_TOKONOW_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P1_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P1_VARIANT_TOKONOW_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P2_DATA_NEGATIVE_CASE_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P2_DATA_NON_VARIANT_TOKONOW_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P2_DATA_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_P2_DATA_VARIANT_TOKONOW_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_SUCCESS_ATC_NON_VARIANT_TOKONOW_PATH
import com.tokopedia.product.detail.ui.interceptor.RESPONSE_TICKER_PATH
import com.tokopedia.product.detail.util.ProductDetailNetworkIdlingResource
import com.tokopedia.product.detail.util.ProductIdlingInterface
import com.tokopedia.product.detail.util.ViewAttributeMatcher
import com.tokopedia.product.detail.util.assertNotVisible
import com.tokopedia.product.detail.util.assertVisible
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.test.runBlockingTest
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
                if (activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action) == null) {
                    throw RuntimeException("button not found")
                }

                return activityCommonRule.activity.getPdpFragment().view?.findViewById<ConstraintLayout>(R.id.partial_layout_button_action)?.visibility == View.VISIBLE
            }
        })
    }

    override fun before() {
        IdlingRegistry.getInstance().register(buttonIdlingResource)
        customInterceptor.resetInterceptor()
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().resources.forEach {
            IdlingRegistry.getInstance().unregister(it)
        }
        activityCommonRule.finishActivity()
    }

    @Test
    fun sticky_login_non_login() {
        InstrumentationAuthHelper.logout()
        createMockModelConfig()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.pdp_button_container)).assertVisible()
        onView(withId(R.id.sticky_login_pdp)).assertVisible()
    }

    @Test
    fun sticky_login_shows_login() {
        InstrumentationAuthHelper.login() // given user logged in
        createMockModelConfig()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.pdp_button_container)).assertVisible()
        onView(withId(R.id.sticky_login_pdp)).assertNotVisible()
    }

    @Test
    fun check_empty_button_initial_data() {
        InstrumentationAuthHelper.logout()
        customInterceptor.customP1ResponsePath = RESPONSE_P1_NEGATIVE_CASE_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_NEGATIVE_CASE_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()
        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("Stok Kosong")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN && !buttonUnify.isEnabled
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_buy_atc_button_initial_data() {
        InstrumentationAuthHelper.login()
        createMockModelConfig()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("Beli test")))

        onView(withId(R.id.btn_add_to_cart))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang test")))

        support_button_not_visible()
    }

    //region Error case
    @Test
    fun check_default_button_p2_error_product_empty() {
        InstrumentationAuthHelper.login()
        customInterceptor.customP1ResponsePath = RESPONSE_P1_NEGATIVE_CASE_PATH
        customInterceptor.customP2ErrorResponsePath = "custom error"

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.seller_button_container)).assertNotVisible()

        onView(withId(R.id.btn_empty_stock)).assertVisible()
            .check(matches(ViewMatchers.withText("Stok Habis")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN && !buttonUnify.isEnabled
                    }
                )
            )
    }

    @Test
    fun check_default_button_p2_error() {
        InstrumentationAuthHelper.logout()
        customInterceptor.customP1ResponsePath = RESPONSE_P1_PATH
        customInterceptor.customP2ErrorResponsePath = "custom error"

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("Beli")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        support_button_not_visible()
    }

    @Test
    fun check_button_atc_variant_noMinicart_tokonow_login() {
        InstrumentationAuthHelper.login() // given user logged in
        customInterceptor.customP1ResponsePath = RESPONSE_P1_VARIANT_TOKONOW_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_VARIANT_TOKONOW_PATH
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_EMPTY_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_button_atc_variant_minicart_tokonow_login() {
        InstrumentationAuthHelper.login() // given user logged in

        customInterceptor.customP1ResponsePath = RESPONSE_P1_VARIANT_TOKONOW_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_VARIANT_TOKONOW_PATH
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()
        onView(withId(R.id.tokonow_button_container)).assertVisible()
        onView(withId(R.id.txt_total_quantity)).assertVisible()
        onView(withId(R.id.txt_static_total_quantity)).assertVisible()
        onView(withId(R.id.divider_button_quantity)).assertVisible()
        onView(withId(R.id.txt_product_name))
            .assertVisible()
            .check(matches(ViewMatchers.withText("PDP D4G1NG G1L1NG V4R THUMBN41L - Hitam")))

        onView(withId(R.id.btn_atc_tokonow_variant))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_delete_tokonow_non_var)).assertNotVisible()
        onView(withId(R.id.qty_tokonow_non_var)).assertNotVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_button_atc_non_variant_non_login_tokonow() {
        customInterceptor.customP1ResponsePath = RESPONSE_P1_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_EMPTY_PATH
        customInterceptor.customAtcV2ResponsePath = RESPONSE_SUCCESS_ATC_NON_VARIANT_TOKONOW_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun check_quantity_editor_button_non_variant_login_minicart_tokonow() {
        InstrumentationAuthHelper.login() // given user logged in
        customInterceptor.customP1ResponsePath = RESPONSE_P1_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.qty_tokonow_non_var)).assertVisible()
            .check(
                matches(
                    ViewAttributeMatcher {
                        val qtyEditor = (it as QuantityEditorUnify)
                        qtyEditor.getValue() == 5
                    }
                )
            )

        onView(withId(R.id.btn_topchat)).assertVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
    }

    @Test
    fun check_button_atc_non_variant_login_noMinicart_tokonow() {
        InstrumentationAuthHelper.login() // given user logged in

        customInterceptor.customP1ResponsePath = RESPONSE_P1_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_NON_VARIANT_TOKONOW_PATH
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_EMPTY_PATH
        customInterceptor.customAtcV2ResponsePath = RESPONSE_SUCCESS_ATC_NON_VARIANT_TOKONOW_PATH

        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertVisible()

        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        support_button_not_visible()
    }

    @Test
    fun click_button_atc_variant_login_noMinicart_tokonow() = runBlockingTest {
        check_button_atc_variant_noMinicart_tokonow_login()
        customInterceptor.customMiniCartResponsePath = RESPONSE_MINICART_PATH

        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(R.id.btn_buy_now)).perform(click())
        Thread.sleep(200)

        simulateOnResume()
        Thread.sleep(200)

        onView(withId(R.id.tokonow_button_container)).assertVisible()
        onView(withId(R.id.txt_static_total_quantity)).assertVisible()
        onView(withId(R.id.divider_button_quantity)).assertVisible()
        onView(withId(R.id.txt_product_name)).assertVisible()
        onView(withId(R.id.txt_total_quantity)).assertVisible()

        onView(withId(R.id.btn_atc_tokonow_variant))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_delete_tokonow_non_var)).assertNotVisible()

        onView(withId(R.id.qty_tokonow_non_var)).assertNotVisible()

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
    }

    @Test
    fun check_click_atc_tokonow_non_variant_non_login() {
        // else if (!GlobalConfig.isSellerApp() && !onSuccessGetCartType)
        check_button_atc_non_variant_non_login_tokonow()
        intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        onView(withId(R.id.btn_buy_now)).perform(click()).check(matches(isDisplayed()))

        onView(withId(R.id.btn_topchat)).assertVisible()

        // gotologin page
        onView(withId(R.id.btn_buy_now))
            .assertVisible()
            .check(matches(ViewMatchers.withText("+ Keranjang")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )

        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()
    }

    //endregion

    //region seller side
    @Test
    fun check_button_seller_side() {
        // given user logged in
        InstrumentationAuthHelper.login {
            setIsShopOwner(true)
            shopId = "1990266"
        }
        createMockModelConfig()
        activityCommonRule.activity.setupTestFragment(productDetailTestComponent)

        onView(withId(R.id.btn_topchat)).assertNotVisible()
        onView(withId(R.id.btn_buy_now)).assertNotVisible()
        onView(withId(R.id.btn_add_to_cart)).assertNotVisible()

        onView(withId(R.id.seller_button_container)).assertVisible()
        onView(withId(R.id.btn_edit_product))
            .assertVisible()
            .check(matches(ViewMatchers.withText("Ubah Produk")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.GHOST && buttonUnify.buttonType == UnifyButton.Type.ALTERNATE
                    }
                )
            )

        onView(withId(R.id.btn_top_ads))
            .assertVisible()
            .check(matches(ViewMatchers.withText("Iklankan Produk")))
            .check(
                matches(
                    ViewAttributeMatcher {
                        val buttonUnify = (it as UnifyButton)
                        buttonUnify.buttonVariant == UnifyButton.Variant.FILLED && buttonUnify.buttonType == UnifyButton.Type.MAIN
                    }
                )
            )
    }
    //endregion

    @UiThreadTest
    private fun simulateOnResume() {
        val instr: Instrumentation = getInstrumentation()
        getInstrumentation().runOnMainSync {
            instr.callActivityOnResume(activityCommonRule.activity)
        }
    }

    private fun support_button_not_visible() {
        onView(withId(R.id.seller_button_container)).assertNotVisible()
        onView(withId(R.id.btn_empty_stock)).assertNotVisible()
    }

    private fun createMockModelConfig() {
        customInterceptor.customP1ResponsePath = RESPONSE_P1_PATH
        customInterceptor.customP2DataResponsePath = RESPONSE_P2_DATA_PATH
        customInterceptor.customTickerResponsePath = RESPONSE_TICKER_PATH
    }
}
