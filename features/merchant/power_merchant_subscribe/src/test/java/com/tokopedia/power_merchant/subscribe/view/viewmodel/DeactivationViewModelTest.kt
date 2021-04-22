package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.power_merchant.subscribe.data.model.*
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetGoldCancellationsQuestionaireUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaPlugins
import rx.plugins.RxJavaSchedulersHook
import rx.schedulers.Schedulers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 21/04/21
 */

@ExperimentalCoroutinesApi
class DeactivationViewModelTest {

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getGoldCancellationsQuestionaireUseCase: GetGoldCancellationsQuestionaireUseCase

    @RelaxedMockK
    lateinit var deactivatePMUseCase: DeactivatePMUseCase

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetShopStatusUseCase

    lateinit var getPMCancellationQuestionnaireDataUseCase: GetPMCancellationQuestionnaireDataUseCase

    private lateinit var viewModel: DeactivationViewModel

    @Before
    fun setup() {
        registerRxPlugins()
        MockKAnnotations.init(this)

        getPMCancellationQuestionnaireDataUseCase = GetPMCancellationQuestionnaireDataUseCase(
                getShopStatusUseCase,
                getGoldCancellationsQuestionaireUseCase
        )

        viewModel = DeactivationViewModel(
                getPMCancellationQuestionnaireDataUseCase,
                deactivatePMUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @After
    fun tearDown() {
        resetRxPlugins()
        RxAndroidPlugins.getInstance().reset()
    }

    @Test
    fun testGetPMCancellationRateQuestionnaireDataSuccess() {
        val shopId = "1"
        val questionTitle = "Question A"
        val questionType = PMCancellationQuestionnaireQuestionModel.TYPE_RATE

        val question = Question(question = questionTitle, questionType = questionType)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        val pmOsStatus = GoldGetPmOsStatus()
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(pmOsStatus)

        val questionnaireList = GoldCancellationsQuestionaire(questionnaireData)
        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(questionnaireList)

        viewModel.getPMCancellationQuestionnaireData(shopId)

        val listQuestion = questionnaireList.result.data.questionList.map { q ->
            return@map QuestionnaireUiModel.QuestionnaireRatingUiModel(q.question)
        }

        val expected = Success(DeactivationQuestionnaireUiModel(
                expiredDate = pmOsStatus.result.data.powerMerchant.expiredTime,
                listQuestion = listQuestion
        ))
        val actual = viewModel.pmCancellationQuestionnaireData
                .observeAwaitValue() as? Success<DeactivationQuestionnaireUiModel>

        assertCancellationQuestionnaireData(expected, actual)
    }

    @Test
    fun testGetPMCancellationMultipleOptionQuestionnaireDataSuccess() {
        val shopId = "1"

        val pmOsStatus = GoldGetPmOsStatus()
        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(pmOsStatus)

        val optionTitle = "Question A"
        val questionTitle = "Question B"
        val questionType = "multi_answer_question"

        val options = mutableListOf(Option(value = optionTitle))
        val question = Question(question = questionTitle, questionType = questionType, option = options)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        val questionnaireList = GoldCancellationsQuestionaire(questionnaireData)
        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(questionnaireList)

        viewModel.getPMCancellationQuestionnaireData(shopId)

        val numOfQuestions = questionnaireList.result.data.questionList.size
        val listQuestion = questionnaireList.result.data.questionList.mapIndexed { index, q ->
            return@mapIndexed createMultipleOptionQuestion(q, index != numOfQuestions.minus(1))
        }

        val expected = Success(DeactivationQuestionnaireUiModel(
                expiredDate = pmOsStatus.result.data.powerMerchant.expiredTime,
                listQuestion = listQuestion
        ))
        val actual = viewModel.pmCancellationQuestionnaireData
                .observeAwaitValue() as? Success<DeactivationQuestionnaireUiModel>

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

        viewModel.getPMCancellationQuestionnaireData(shopId)

        val pmCancellationQuestionnaireData = viewModel.pmCancellationQuestionnaireData.observeAwaitValue()

        Assert.assertTrue(pmCancellationQuestionnaireData is Fail)
    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendSuccess() = runBlocking {
        val questionsData = mutableListOf<PMCancellationQuestionnaireAnswerModel>()
        deactivatePMUseCase.params = DeactivatePMUseCase.createRequestParam(questionsData)

        coEvery {
            deactivatePMUseCase.executeOnBackground()
        } returns true

        viewModel.submitPmDeactivation(questionsData)

        val expectedResult = Success(true)

        viewModel.isSuccessDeactivate.verifySuccessEquals(expectedResult)
    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendError() = runBlocking {
        val questionsData = mutableListOf<PMCancellationQuestionnaireAnswerModel>()
        deactivatePMUseCase.params = DeactivatePMUseCase.createRequestParam(questionsData)

        val error = Throwable()
        coEvery {
            deactivatePMUseCase.executeOnBackground()
        } throws error

        viewModel.submitPmDeactivation(questionsData)

        val expectedResult = Fail(error)

        viewModel.isSuccessDeactivate.verifyErrorEquals(expectedResult)
    }

    private fun createMultipleOptionQuestion(
            questionData: Question,
            showItemDivider: Boolean
    ): QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel {
        return QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel(
                question = questionData.question,
                options = questionData.option.map {
                    QuestionnaireOptionUiModel(it.value)
                },
                showItemDivider = showItemDivider
        )
    }

    private fun assertCancellationQuestionnaireData(
            expected: Success<DeactivationQuestionnaireUiModel>,
            actual: Success<DeactivationQuestionnaireUiModel>?
    ) {
        val expectedResult = expected.data.listQuestion
        val actualResult = actual?.data?.listQuestion.orEmpty()

        actualResult.forEachIndexed { index, actualQuestion ->
            val expectedQuestion = expectedResult[index]

            when {
                actualQuestion is QuestionnaireUiModel.QuestionnaireRatingUiModel && expectedQuestion is QuestionnaireUiModel.QuestionnaireRatingUiModel -> {
                    assertEquals(expectedQuestion.question, actualQuestion.question)
                    assertEquals(expectedQuestion.type, actualQuestion.type)
                }
                actualQuestion is QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel && expectedQuestion is QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel -> {
                    assertEquals(expectedQuestion.question, actualQuestion.question)
                    assertEquals(expectedQuestion.type, actualQuestion.type)
                    assertEquals(expectedQuestion.showItemDivider, actualQuestion.showItemDivider)

                    actualQuestion.options.forEachIndexed { i, actualOption ->
                        val expectedOption = expectedQuestion.options[i]
                        assertEquals(expectedOption.text, actualOption.text)
                        assertEquals(expectedOption.isChecked, actualOption.isChecked)
                    }
                }
            }
        }
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

    private fun assertRatingQuestionnaireData(
            expected: PMCancellationQuestionnaireRateModel,
            actual: PMCancellationQuestionnaireRateModel
    ) {
        Assert.assertEquals(expected.question, actual.question)
        Assert.assertEquals(expected.type, actual.type)
    }

    private fun assertMultipleOptionsQuestionnaireData(
            expected: PMCancellationQuestionnaireMultipleOptionModel,
            actual: PMCancellationQuestionnaireMultipleOptionModel
    ) {
        Assert.assertEquals(expected.question, actual.question)
        Assert.assertEquals(expected.type, actual.type)

        actual.listOptionModel.forEachIndexed { index, actualOption ->
            val expectedOption = expected.listOptionModel[index]
            Assert.assertEquals(expectedOption.value, actualOption.value)
            Assert.assertEquals(expectedOption.isChecked, actualOption.isChecked)
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