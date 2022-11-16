package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.interceptor.GET_EPHARMACY_CHECKOUT_DATA_EMPTY_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_EPHARMACY_CHECKOUT_DATA_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.GET_OCC_CART_PAGE_EPHARMACY_PRODUCT_RESPONSE_PATH
import com.tokopedia.oneclickcheckout.common.interceptor.OneClickCheckoutInterceptor
import com.tokopedia.oneclickcheckout.common.robot.orderSummaryPage
import com.tokopedia.oneclickcheckout.common.rule.FreshIdlingResourceTestRule
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class OrderSummaryPageActivityEthicalDrugTest {

    @get:Rule
    var activityRule = IntentsTestRule(TestOrderSummaryPageActivity::class.java, false, false)

    @get:Rule
    val freshIdlingResourceTestRule = FreshIdlingResourceTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var idlingResource: IdlingResource? = null

    private val cartInterceptor = OneClickCheckoutInterceptor.cartInterceptor
    private val ethicalDrugInterceptor = OneClickCheckoutInterceptor.ethicalDrugTestInterceptor

    @Before
    fun setup() {
        OneClickCheckoutInterceptor.resetAllCustomResponse()
        OneClickCheckoutInterceptor.setupGraphqlMockResponse(context)
        idlingResource = OccIdlingResource.getIdlingResource()
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun happyFlow_DisplayUploadWidgetWithoutPrescription() {
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_EPHARMACY_PRODUCT_RESPONSE_PATH
        ethicalDrugInterceptor.customGetPrescriptionIdsResponsePath =
            GET_EPHARMACY_CHECKOUT_DATA_EMPTY_RESPONSE_PATH

        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProductCard(
                productName = "obat mual 1",
                productPrice = "Rp5.000",
                productQty = 1,
                productInfo = listOf("Butuh Resep"),
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productNotes = null
            )
            assertUploadPrescription(
                prescriptionText = "Lampirkan Resep Dokter"
            )
        }
    }

    @Test
    fun happyFlow_DisplayUploadWidgetWithPrescription() {
        cartInterceptor.customGetOccCartResponsePath =
            GET_OCC_CART_PAGE_EPHARMACY_PRODUCT_RESPONSE_PATH
        ethicalDrugInterceptor.customGetPrescriptionIdsResponsePath =
            GET_EPHARMACY_CHECKOUT_DATA_RESPONSE_PATH
        activityRule.launchActivity(null)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        orderSummaryPage {
            assertProductCard(
                productName = "obat mual 1",
                productPrice = "Rp5.000",
                productQty = 1,
                productInfo = listOf("Butuh Resep"),
                productSlashPrice = null,
                productSlashPriceLabel = null,
                productVariant = null,
                productWarningMessage = null,
                productAlertMessage = null,
                productNotes = null
            )

            assertUploadPrescription(
                prescriptionText = "Resep Terlampir",
                descriptionText = "Kamu punya 1 resep dokter"
            )
        }

    }

}
