package com.tokopedia.tokofood.cassavatest.base

import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.AndroidTestUtil
import com.tokopedia.tokofood.stub.common.util.isViewDisplayed
import com.tokopedia.tokofood.stub.common.util.onClick
import com.tokopedia.tokofood.stub.common.util.onIdView
import com.tokopedia.tokofood.stub.common.util.purchasePageScrollTo
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutGeneralTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.domain.usecase.CheckoutTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.util.TokoFoodPurchaseComponentStubInstance
import org.junit.After
import org.junit.Before

open class TokoFoodPurchaseCassavaTest: BaseTokoFoodCassavaTest() {

    lateinit var checkoutTokoFoodUseCaseStub: CheckoutTokoFoodUseCaseStub
    lateinit var checkoutGeneralUseCaseStub: CheckoutGeneralTokoFoodUseCaseStub
    lateinit var graphQlRepositoryStub: GraphqlRepositoryStub
    
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

    protected fun clickPurchaseButton() {
        activityRule.activity.purchasePageScrollTo<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>(
            recyclerViewId = R.id.recycler_view_purchase
        )
        onIdView(com.tokopedia.totalamount.R.id.amount_cta).isViewDisplayed().onClick()
    }

    companion object {
        const val LOAD_CHECKOUT_PAGE =
            "tracker/tokofood/purchase/tokofood_purchase_load_checkout_page.json"
        const val CHECKOUT_GENERAL =
            "tracker/tokofood/purchase/tokofood_purchase_checkout_general.json"

    }

}
