package com.tokopedia.tokofood.cassavatest.base

import com.tokopedia.tokofood.feature.purchase.promopage.domain.model.PromoListTokoFoodResponse
import com.tokopedia.tokofood.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.tokofood.stub.common.util.AndroidTestUtil
import com.tokopedia.tokofood.stub.purchase.promo.domain.usecase.PromoListTokoFoodUseCaseStub
import com.tokopedia.tokofood.stub.purchase.promo.util.TokoFoodPromoComponentStubInstance
import org.junit.After
import org.junit.Before

open class TokoFoodPromoCassavaTest: BaseTokoFoodCassavaTest() {

    lateinit var promoListTokoFoodUseCaseStub: PromoListTokoFoodUseCaseStub
    lateinit var graphQlRepositoryStub: GraphqlRepositoryStub

    protected val promoListTokofoodResponseStub by lazy {
        AndroidTestUtil.parse<PromoListTokoFoodResponse>(
            "raw/purchase/promo/promo_tokofood_success.json",
            PromoListTokoFoodResponse::class.java
        ) ?: PromoListTokoFoodResponse()
    }

    private val tokoFoodPromoComponentStubInstance by lazy {
        TokoFoodPromoComponentStubInstance.getTokoFoodPromoComponentStub(applicationContext)
    }

    @Before
    open fun setup() {
        graphQlRepositoryStub = tokoFoodPromoComponentStubInstance.graphqlRepository() as GraphqlRepositoryStub
        promoListTokoFoodUseCaseStub = tokoFoodPromoComponentStubInstance.promoListTokoFoodUseCase() as PromoListTokoFoodUseCaseStub
    }

    @After
    open fun cleanUp() {
        graphQlRepositoryStub.clearMocks()
    }

    companion object {
        const val LOAD_PROMO_LIST =
            "tracker/tokofood/purchase/promo/tokofood_promo_load.json"
    }

}
