package com.tokopedia.dg_transaction.testing

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.dg_transaction.testing.di.DaggerStubEmoneyPdpComponent
import com.tokopedia.dg_transaction.testing.di.StubEmoneyPdpComponent
import com.tokopedia.dg_transaction.testing.di.util.StubCommonTopupBillsComponentInstance
import com.tokopedia.dg_transaction.testing.digital_checkout.robot.digitalCheckout
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivityStub
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.robot.emoneyPdp
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.utils.PdpCheckoutThankyouResponseConfig
import com.tokopedia.dg_transaction.testing.thankyou_native.robot.thankYou
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.thankyou_native.presentation.activity.ARG_MERCHANT
import com.tokopedia.thankyou_native.presentation.activity.ARG_PAYMENT_ID
import com.tokopedia.thankyou_native.presentation.activity.ThankYouPageActivity
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PdpCheckoutThankyouJourneyTest {
    @get:Rule
    var mActivityRule = ActivityTestRule(
        EmoneyPdpActivityStub::class.java,
        false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

    private lateinit var activity: EmoneyPdpActivityStub
    private var emoneyPdpComponentStub: StubEmoneyPdpComponent? = null

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var localCacheHandler: LocalCacheHandler

    @Before
    fun stubAllIntent() {
        Intents.init()
    }

    @Test
    fun journeyTest() {
        setUpLaunchActivity()
        emoneyPdp {
            clickOnFavNumberOnInputView()
            clickProduct()
            clickBuy()
        }
        digitalCheckout {
            waitForData()
            clickCheckout()
            waitForData()
        }
        thankYou {
            mockPayAndNavigateThankYou(activity)
            waitForData()
        }
        MatcherAssert.assertThat(cassavaTestRule.validate(ANALYTIC_VALIDATOR_QUERY_ID), hasAllSuccess())
    }

    private fun setUpLaunchActivity() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        setupGraphqlMockResponse(PdpCheckoutThankyouResponseConfig(isLogin = true))
        val intent = Intent(context, EmoneyPdpActivityStub::class.java).setData(
            Uri.parse("tokopedia://digital/form?category_id=34&menu_id=267&template=electronicmoney")
        )
        mActivityRule.launchActivity(intent)
        activity = mActivityRule.activity

        localCacheHandler = LocalCacheHandler(context,
            EmoneyPdpFragment.EMONEY_PDP_PREFERENCES_NAME
        )
        localCacheHandler.apply {
            putBoolean(EmoneyPdpFragment.EMONEY_PDP_COACH_MARK_HAS_SHOWN, true)
            applyEditor()
        }

        setupComponent()
        Thread.sleep(2000)
    }

    private fun setupComponent() {
        emoneyPdpComponentStub = DaggerStubEmoneyPdpComponent.builder()
            .stubCommonTopupBillsComponent(
                StubCommonTopupBillsComponentInstance.getCommonTopupBillsComponent(activity.application))
            .build()
        emoneyPdpComponentStub?.inject(this)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_ID = "51"
    }
}
