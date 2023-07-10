package com.tokopedia.tokofood.cassavatest.base

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import com.tokopedia.tokofood.stub.base.presentation.activity.BaseTokofoodActivityStub
import com.tokopedia.tokofood.stub.common.util.AndroidTestUtil
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.util.TokoFoodPurchaseComponentStubInstance
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

open class BaseTokoFoodCassavaTest {

    @get:Rule
    var activityRule = IntentsTestRule(BaseTokofoodActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Inject
    lateinit var checkoutTokoFoodUseCaseStub: CheckoutTokoFoodUseCaseStub

    protected val applicationContext: Context = ApplicationProvider.getApplicationContext()
    protected val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    
    private val getTokofoodPurchaseComponentStub by lazy {
        TokoFoodPurchaseComponentStubInstance.getTokoFoodPurchaseComponentStub(applicationContext)
    }

    protected val checkoutTokoFoodResponseStub by lazy {
        AndroidTestUtil.parse<CartListTokofoodResponse>(
            "raw/purchase/purchase_success.json",
            CartListTokofoodResponse::class.java
        ) ?: CartListTokofoodResponse()
    }

    @Before
    open fun setup() {
        checkoutTokoFoodUseCaseStub = getTokofoodPurchaseComponentStub.checkoutTokofoodUseCase() as CheckoutTokoFoodUseCaseStub
    }

    @After
    open fun cleanUp() {
        checkoutTokoFoodUseCaseStub.clearCache()
    }

    protected fun launchActivity() {
        activityRule.launchActivity(
            BaseTokofoodActivityStub.createIntent(targetContext, "")
        )
    }

    protected fun dismissPage() {
        activityRule.activity.finish()
    }

    protected fun validateTracker(fileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    companion object {
        const val LOAD_CHECKOUT_PAGE =
            "tracker/tokofood/purchase/tokofood_purchase_load_checkout_page.json"
    }

}
