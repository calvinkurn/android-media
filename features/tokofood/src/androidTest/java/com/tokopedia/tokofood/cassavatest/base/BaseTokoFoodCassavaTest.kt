package com.tokopedia.tokofood.cassavatest.base

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.stub.base.presentation.activity.BaseTokofoodActivityStub
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.AndroidTestUtil
import com.tokopedia.tokofood.stub.common.util.isViewDisplayed
import com.tokopedia.tokofood.stub.common.util.onClick
import com.tokopedia.tokofood.stub.common.util.onIdView
import com.tokopedia.tokofood.stub.common.util.purchasePageScrollTo
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutGeneralTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.util.TokoFoodPurchaseComponentStubInstance
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseTokoFoodCassavaTest {

    @get:Rule
    var activityRule = IntentsTestRule(BaseTokofoodActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    lateinit var checkoutTokoFoodUseCaseStub: CheckoutTokoFoodUseCaseStub
    lateinit var checkoutGeneralUseCaseStub: CheckoutGeneralTokoFoodUseCaseStub
    lateinit var graphQlRepositoryStub: GraphqlRepositoryStub

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

    protected val checkoutGeneralResponseStub by lazy {
        AndroidTestUtil.parse<CheckoutGeneralTokoFoodResponse>(
            "raw/purchase/checkout_general_success.json",
            CheckoutGeneralTokoFoodResponse::class.java
        ) ?: CheckoutGeneralTokoFoodResponse()
    }

    @Before
    open fun setup() {
        graphQlRepositoryStub = getTokofoodPurchaseComponentStub.graphqlRepository() as GraphqlRepositoryStub
        checkoutTokoFoodUseCaseStub = getTokofoodPurchaseComponentStub.checkoutTokofoodUseCase() as CheckoutTokoFoodUseCaseStub
        checkoutGeneralUseCaseStub = getTokofoodPurchaseComponentStub.checkoutGeneralTokoFoodUseCase() as CheckoutGeneralTokoFoodUseCaseStub
    }

    @After
    open fun cleanUp() {
        graphQlRepositoryStub.clearMocks()
    }

    protected fun launchActivity() {
        activityRule.launchActivity(
            BaseTokofoodActivityStub.createIntent(targetContext, "")
        )
    }

    protected fun dismissPage() {
        activityRule.activity.finish()
    }

    protected fun clickPurchaseButton() {
        activityRule.activity.purchasePageScrollTo<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>(
            recyclerViewId = R.id.recycler_view_purchase
        )
        onIdView(R.id.amount_cta).isViewDisplayed().onClick()
    }

    protected fun validateTracker(fileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    companion object {
        const val LOAD_CHECKOUT_PAGE =
            "tracker/tokofood/purchase/tokofood_purchase_load_checkout_page.json"
        const val CHECKOUT_GENERAL_PAGE =
            "tracker/tokofood/purchase/tokofood_purchase_checkout_general.json"
    }

}
