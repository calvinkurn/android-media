package com.tokopedia.dg_transaction.testing

import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.dg_transaction.testing.digital_checkout.robot.digitalCheckout
import com.tokopedia.dg_transaction.testing.mock.DigitalAddToCartMockResponse
import com.tokopedia.dg_transaction.testing.mock.DigitalCheckoutMockResponse
import com.tokopedia.dg_transaction.testing.mock.RestRepositoryStub
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.presentation.activity.EmoneyPdpActivityStub
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.robot.emoneyPdp
import com.tokopedia.dg_transaction.testing.recharge_pdp_emoney.utils.PdpCheckoutThankyouResponseConfig
import com.tokopedia.recharge_pdp_emoney.presentation.fragment.EmoneyPdpFragment
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class PdpCheckoutThankyouJourneyTest {
    @get:Rule
    var mActivityRule = ActivityTestRule(
        EmoneyPdpActivityStub::class.java,
        false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = false)

    @Inject
    lateinit var restRepositoryStub: RestRepositoryStub

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private lateinit var localCacheHandler: LocalCacheHandler

    private val digitalAddToCartMockResponse = DigitalAddToCartMockResponse()
    private val digitalCheckoutMockResponse = DigitalCheckoutMockResponse()

    @Before
    fun stubAllIntent() {
        Intents.init()
    }

    @Test
    fun journeyTest() {
        setUpLaunchActivity()
        restRepositoryStub.responses = digitalAddToCartMockResponse.getMockResponse()
        emoneyPdp {
            clickOnFavNumberOnInputView()
            clickProduct()
            clickBuy()
        }
        restRepositoryStub.responses = digitalCheckoutMockResponse.getMockResponse()
        digitalCheckout {
            waitForData()
            clickCheckout()
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

        localCacheHandler = LocalCacheHandler(context,
            EmoneyPdpFragment.EMONEY_PDP_PREFERENCES_NAME
        )
        localCacheHandler.apply {
            putBoolean(EmoneyPdpFragment.EMONEY_PDP_COACH_MARK_HAS_SHOWN, true)
            applyEditor()
        }

        Thread.sleep(2000)
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_ID = "51"
    }
}