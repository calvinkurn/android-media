package com.tokopedia.shop.score.common

import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyDetailMergeResponse
import com.tokopedia.shop.score.stub.common.UserSessionStub
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.common.util.AndroidTestUtil
import com.tokopedia.shop.score.stub.common.util.ShopPenaltyComponentStubInstance
import com.tokopedia.shop.score.stub.penalty.domain.mapper.PenaltyMapperStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCaseStub
import com.tokopedia.shop.score.stub.penalty.domain.usecase.GetShopPenaltyDetailUseCaseStub
import com.tokopedia.shop.score.stub.penalty.presentation.activity.ShopPenaltyPageActivityStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopScoreResponseStub
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseShopPenaltyTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPenaltyPageActivityStub::class.java, true, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Context
    protected lateinit var userSessionStub: UserSessionStub
    protected lateinit var graphqlRepositoryStub: GraphqlRepositoryStub
    protected lateinit var getShopPenaltyDetailUseCaseStub: GetShopPenaltyDetailUseCaseStub
    protected lateinit var getShopPenaltyDetailMergeUseCaseStub: GetShopPenaltyDetailMergeUseCaseStub
    protected lateinit var penaltyMapper: PenaltyMapper

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    protected val getShopPenaltyComponentStub by lazy {
        ShopPenaltyComponentStubInstance.getShopPenaltyComponentStub(
            applicationContext
        )
    }

    protected val shopPenaltyResponse = AndroidTestUtil.parse<ShopPenaltyDetailMergeResponse>(
        "raw/seller/cassava/penalty/shop_penalty_response.json",
        ShopPenaltyDetailMergeResponse::class.java
    )

    @Before
    open fun setup() {
        applicationContext = InstrumentationRegistry.getInstrumentation().context.applicationContext
        graphqlRepositoryStub =
            getShopPenaltyComponentStub.graphQlRepository() as GraphqlRepositoryStub
        userSessionStub = getShopPenaltyComponentStub.userSessionInterface() as UserSessionStub
        getShopPenaltyDetailUseCaseStub =
            getShopPenaltyComponentStub.getShopPenaltyDetailUseCaseStub() as GetShopPenaltyDetailUseCaseStub
        getShopPenaltyDetailMergeUseCaseStub =
            getShopPenaltyComponentStub.getShopPenaltyDetailMergeUseCaseStub() as GetShopPenaltyDetailMergeUseCaseStub
        penaltyMapper =
            getShopPenaltyComponentStub.penaltyMapperStub() as PenaltyMapperStub
    }

    @After
    open fun finish() {
        graphqlRepositoryStub.clearMocks()
    }

    protected fun getShopPenaltyPageIntent(): Intent {
        return ShopPenaltyPageActivityStub.createIntent(context)
    }
}