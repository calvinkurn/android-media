package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.data.source.cloud.model.GoldDeactivationSubscription
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.DeactivatePowerMerchantUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetGoldCancellationsQuestionaireUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.*
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class PMCancellationQuestionnaireViewModelTest {

    private val getPMCancellationQuestionnaireDataUseCase by lazy {
        GetPMCancellationQuestionnaireDataUseCase(
                getShopStatusUseCase,
                getGoldCancellationsQuestionaireUseCase
        )
    }

    private val deactivatePowerMerchantUseCase by lazy {
        DeactivatePowerMerchantUseCase(
                graphqlUseCase,
                anyString()
        )
    }

    @RelaxedMockK
    lateinit var graphqlUseCase: GraphqlUseCase

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetShopStatusUseCase

    @RelaxedMockK
    lateinit var getGoldCancellationsQuestionaireUseCase: GetGoldCancellationsQuestionaireUseCase

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    private val pmCancellationQuestionnaireViewModel by lazy {
        PMCancellationQuestionnaireViewModel(
                getPMCancellationQuestionnaireDataUseCase,
                deactivatePowerMerchantUseCase,
                Dispatchers.Main
        )
    }

    @Before
    fun setup() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
        MockKAnnotations.init(this)
    }

    @Test
    fun testGetPMCancellationQuestionnaireDataSuccess() {
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(GoldGetPmOsStatus())
        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(GoldCancellationsQuestionaire())
        pmCancellationQuestionnaireViewModel.getPMCancellationQuestionnaireData(anyString())
        val pmCancellationQuestionnaireData = pmCancellationQuestionnaireViewModel
                .pmCancellationQuestionnaireData.observeAwaitValue()
        assertTrue(pmCancellationQuestionnaireData is Success)
    }

    @Test
    fun testGetPMCancellationQuestionnaireDataError() {
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.error(Throwable())
        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.error(Throwable())
        pmCancellationQuestionnaireViewModel.getPMCancellationQuestionnaireData(anyString())
        val pmCancellationQuestionnaireData = pmCancellationQuestionnaireViewModel
                .pmCancellationQuestionnaireData.observeAwaitValue()
        assertTrue(pmCancellationQuestionnaireData is Fail)
    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendSuccess() {
        every {
            graphqlUseCase.createObservable(any())
        } returns Observable.just(createMockGraphqlResponseDeactivateMerchant())
        pmCancellationQuestionnaireViewModel.sendQuestionAnswerDataAndTurnOffAutoExtend(mockk())
        val isSuccessUnsubscribe = pmCancellationQuestionnaireViewModel
                .isSuccessUnsubscribe.observeAwaitValue()
        assertTrue(isSuccessUnsubscribe is Success)
    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendError() {
        every {
            graphqlUseCase.createObservable(any())
        } returns Observable.error(Throwable())
        pmCancellationQuestionnaireViewModel.sendQuestionAnswerDataAndTurnOffAutoExtend(mockk())
        val isSuccessUnsubscribe = pmCancellationQuestionnaireViewModel
                .isSuccessUnsubscribe.observeAwaitValue()
        assertTrue(isSuccessUnsubscribe is Fail)
    }

    @After
    fun afterTest(){
        RxAndroidPlugins.getInstance().reset()
    }

    private fun createMockGraphqlResponseDeactivateMerchant(): GraphqlResponse {
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        result[GoldDeactivationSubscription::class.java] = GoldDeactivationSubscription()
        return GraphqlResponse(
                result,
                errors,
                false
        )
    }

    private fun <T> LiveData<T>.observeAwaitValue(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}