package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

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
import com.tokopedia.power_merchant.subscribe.data.model.Data
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.data.model.Option
import com.tokopedia.power_merchant.subscribe.data.model.Question
import com.tokopedia.power_merchant.subscribe.data.model.QuestionnaireData
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetGoldCancellationsQuestionaireUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireData
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel.OptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel.Companion.TYPE_MULTIPLE_OPTION
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel.Companion.TYPE_RATE
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook
import rx.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class PMCancellationQuestionnaireViewModelTest {

    companion object {
        private const val QUERY = "query"
    }

    private val getPMCancellationQuestionnaireDataUseCase by lazy {
        GetPMCancellationQuestionnaireDataUseCase(
                getShopStatusUseCase,
                getGoldCancellationsQuestionaireUseCase
        )
    }

    private val deactivatePowerMerchantUseCase by lazy {
        DeactivatePowerMerchantUseCase(
            graphqlUseCase,
            QUERY
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
                CoroutineTestDispatchersProvider
        )
    }

    @Before
    fun setup() {
        registerRxPlugins()
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        resetRxPlugins()
    }

    @Test
    fun testGetPMCancellationRateQuestionnaireDataSuccess() {
        val shopId = "1"
        val questionTitle = "Question A"
        val questionType = TYPE_RATE

        val question = Question(question = questionTitle, questionType = questionType)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(GoldGetPmOsStatus())

        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(GoldCancellationsQuestionaire(questionnaireData))

        pmCancellationQuestionnaireViewModel.getPMCancellationQuestionnaireData(shopId)

        val questionnaireList = mutableListOf<PMCancellationQuestionnaireQuestionModel>(
            PMCancellationQuestionnaireRateModel(questionType, questionTitle))

        val expected = Success(PMCancellationQuestionnaireData(listQuestion = questionnaireList))
        val actual = pmCancellationQuestionnaireViewModel.pmCancellationQuestionnaireData
            .observeAwaitValue() as? Success<PMCancellationQuestionnaireData>

        assertCancellationQuestionnaireData(expected, actual)
    }

    @Test
    fun testGetPMCancellationMultipleOptionsQuestionnaireDataSuccess() {
        val shopId = "2"
        val optionTitle = "Option 1"
        val questionTitle = "Question B"
        val questionType = TYPE_MULTIPLE_OPTION

        val options = mutableListOf(Option(value = optionTitle))
        val question = Question(question = questionTitle, questionType = questionType, option = options)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(GoldGetPmOsStatus())

        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(GoldCancellationsQuestionaire(questionnaireData))

        pmCancellationQuestionnaireViewModel.getPMCancellationQuestionnaireData(shopId)

        val questionOptions = listOf(OptionModel(optionTitle))
        val questionnaireList = mutableListOf<PMCancellationQuestionnaireQuestionModel>(
            PMCancellationQuestionnaireMultipleOptionModel(questionType, questionTitle, questionOptions))

        val expected = Success(PMCancellationQuestionnaireData(listQuestion = questionnaireList))
        val actual = pmCancellationQuestionnaireViewModel.pmCancellationQuestionnaireData
            .observeAwaitValue() as? Success<PMCancellationQuestionnaireData>

        assertCancellationQuestionnaireData(expected, actual)
    }

    @Test
    fun testGetPMCancellationQuestionnaireDataError() {
        val shopId = "3"
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.error(Throwable())
        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.error(Throwable())
        pmCancellationQuestionnaireViewModel.getPMCancellationQuestionnaireData(shopId)
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

    private fun assertCancellationQuestionnaireData(
        expected: Success<PMCancellationQuestionnaireData>,
        actual: Success<PMCancellationQuestionnaireData>?
    ) {
        val expectedQuestionList = expected.data.listQuestion
        val actualQuestionList = actual?.data?.listQuestion.orEmpty()

        actualQuestionList.forEachIndexed { index, actualQuestion ->
            val expectedQuestion = expectedQuestionList[index]

            when {
                actualQuestion is PMCancellationQuestionnaireRateModel &&
                    expectedQuestion is PMCancellationQuestionnaireRateModel -> {
                    assertRatingQuestionnaireData(expectedQuestion, actualQuestion)
                }
                actualQuestion is PMCancellationQuestionnaireMultipleOptionModel &&
                    expectedQuestion is PMCancellationQuestionnaireMultipleOptionModel -> {
                    assertMultipleOptionsQuestionnaireData(expectedQuestion, actualQuestion)
                }
            }
        }
    }

    private fun assertRatingQuestionnaireData(
        expected: PMCancellationQuestionnaireRateModel,
        actual: PMCancellationQuestionnaireRateModel
    ) {
        assertEquals(expected.question, actual.question)
        assertEquals(expected.type, actual.type)
    }

    private fun assertMultipleOptionsQuestionnaireData(
        expected: PMCancellationQuestionnaireMultipleOptionModel,
        actual: PMCancellationQuestionnaireMultipleOptionModel
    ) {
        assertEquals(expected.question, actual.question)
        assertEquals(expected.type, actual.type)

        actual.listOptionModel.forEachIndexed { index, actualOption ->
            val expectedOption = expected.listOptionModel[index]
            assertEquals(expectedOption.value, actualOption.value)
            assertEquals(expectedOption.isChecked, actualOption.isChecked)
        }
    }

    private fun registerRxPlugins() {
        RxJavaPlugins.getInstance().reset()
        RxAndroidPlugins.getInstance().registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
        RxJavaPlugins.getInstance().registerSchedulersHook(object : RxJavaSchedulersHook() {
            override fun getIOScheduler(): Scheduler {
                return Schedulers.immediate()
            }

            override fun getNewThreadScheduler(): Scheduler {
                return Schedulers.immediate()
            }
        })
    }

    private fun resetRxPlugins() {
        @Suppress("UnstableApiUsage")
        RxAndroidPlugins.getInstance().reset()
        RxJavaPlugins.getInstance().reset()
    }
}