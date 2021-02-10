package com.tokopedia.promocheckoutmarketplace

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.promocheckoutmarketplace.interceptor.PromoCheckoutMarketplaceInterceptor
import com.tokopedia.promocheckoutmarketplace.presentation.PromoCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.ARGS_BBO_PROMO_CODES
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule

abstract class BasePromoCheckoutMarketplaceAnalyticsTest {

    abstract fun getAnalyticsValidatorQueryFileName(): String

    abstract fun getPageSource(): Int

    abstract fun getStateParam(): String

    abstract fun getCartTypeParam(): String

    @get:Rule
    val activityRule = ActivityTestRule(PromoCheckoutActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    private val interceptor = PromoCheckoutMarketplaceInterceptor()
    private var idlingResource: IdlingResource? = null

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()

        GraphqlClient.reInitRetrofitWithInterceptors(listOf(interceptor), context)
        interceptor.customCouponListRecommendationResponsePath = "promo/coupon_list_recommendation_empty_response.json"
        interceptor.customValidateUsePromoRevampResponsePath = "promo/validate_use_promo_revamp_success_response.json"

        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        idlingResource = PromoCheckoutIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)

        activityRule.launchActivity(generatePromoIntent())
    }

    @After
    fun cleanup() {
        gtmLogDBSource.deleteAll().toBlocking().first()

        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    fun performPromoAnalyticsActions() {
        promoCheckoutPage {

            // to ensure empty promo list UI is shown
            pullSwipeRefresh()

            interceptor.customCouponListRecommendationResponsePath = "promo/coupon_list_recommendation_ineligible_response.json"

            pullSwipeRefresh()

            interceptor.customCouponListRecommendationResponsePath = "promo/coupon_list_recommendation_response.json"

            pullSwipeRefresh()

            // Select promo
            clickPromoWithTitle("Cashback Rp25.000")

            // Deselect promo
            clickPromoWithTitle("Cashback Rp25.000")

            interceptor.customCouponListRecommendationResponsePath = "promo/coupon_list_recommendation_with_input_code_response.json"

            typePromoCode("TESTCODE")

            clickTerapkanPromoCode()

            // Deselect promo
            clickPromoWithTitle("Discount Rp30.000")

            // Select promo
            clickPromoWithTitle("Discount Rp30.000")

            clickPilihPromoRecommendation()

            clickPakaiPromo()

            // to ensure promo successfully applied
            Thread.sleep(1000)

            assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, getAnalyticsValidatorQueryFileName()), hasAllSuccess())
        }
    }

    private fun generatePromoIntent(): Intent {
        return Intent().apply {
            putExtra(ARGS_PAGE_SOURCE, getPageSource())
            putExtra(ARGS_PROMO_REQUEST, PromoRequest().apply {
                orders = listOf(Order().apply {
                    shopId = 1
                    uniqueId = "1"
                    product_details = listOf(ProductDetail(1, 1))
                    isChecked = true
                    shippingId = 1
                    spId = 1
                    isInsurancePrice = 1
                })
                state = getStateParam()
                cartType = getCartTypeParam()
                codes = ArrayList()
            })
            putExtra(ARGS_VALIDATE_USE_REQUEST, ValidateUsePromoRequest().apply {
                orders = listOf(OrdersItem().apply {
                    shopId = 1
                    uniqueId = "1"
                    productDetails = listOf(ProductDetailsItem(1, 1))
                    shippingId = 1
                    spId = 1
                })
                state = getStateParam()
                cartType = getCartTypeParam()
                codes = mutableListOf()
                skipApply = 0
                isSuggested = 0
            })
            putStringArrayListExtra(ARGS_BBO_PROMO_CODES, ArrayList())
        }
    }
}