package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.gm.common.data.source.cloud.model.GoldDeactivationSubscription
import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.power_merchant.subscribe.data.model.*
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetGoldCancellationsQuestionaireUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireData
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import java.lang.reflect.Type
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 21/04/21
 */

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

    @Test
    fun testGetPMCancellationRateQuestionnaireDataSuccess() {
        val shopId = "1"
        val questionTitle = "Question A"
        val questionType = PMCancellationQuestionnaireQuestionModel.TYPE_RATE

        val question = Question(question = questionTitle, questionType = questionType)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(GoldGetPmOsStatus())

        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(GoldCancellationsQuestionaire(questionnaireData))

        viewModel.getPMCancellationQuestionnaireData(shopId)

        val questionnaireList = mutableListOf<PMCancellationQuestionnaireQuestionModel>(
                PMCancellationQuestionnaireRateModel(questionType, questionTitle))

        val expected = Success(PMCancellationQuestionnaireData(listQuestion = questionnaireList))
        val actual = viewModel.pmCancellationQuestionnaireData
                .observeAwaitValue() as? Success<PMCancellationQuestionnaireData>

        assertCancellationQuestionnaireData(expected, actual)
    }

    @Test
    fun testGetPMCancellationMultipleOptionsQuestionnaireDataSuccess() {
        val shopId = "2"
        val optionTitle = "Option 1"
        val questionTitle = "Question B"
        val questionType = PMCancellationQuestionnaireQuestionModel.TYPE_MULTIPLE_OPTION

        val options = mutableListOf(Option(value = optionTitle))
        val question = Question(question = questionTitle, questionType = questionType, option = options)
        val questionnaireData = QuestionnaireData(data = Data(mutableListOf(question)))

        every {
            getShopStatusUseCase.createObservable(any())
        } returns Observable.just(GoldGetPmOsStatus())

        every {
            getGoldCancellationsQuestionaireUseCase.createObservable(any())
        } returns Observable.just(GoldCancellationsQuestionaire(questionnaireData))

        viewModel.getPMCancellationQuestionnaireData(shopId)

        val questionOptions = listOf(PMCancellationQuestionnaireMultipleOptionModel.OptionModel(optionTitle))
        val questionnaireList = mutableListOf<PMCancellationQuestionnaireQuestionModel>(
                PMCancellationQuestionnaireMultipleOptionModel(questionType, questionTitle, questionOptions))

        val expected = Success(PMCancellationQuestionnaireData(listQuestion = questionnaireList))
        val actual = viewModel.pmCancellationQuestionnaireData
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
        viewModel.getPMCancellationQuestionnaireData(shopId)
        val pmCancellationQuestionnaireData = viewModel
                .pmCancellationQuestionnaireData.observeAwaitValue()
        Assert.assertTrue(pmCancellationQuestionnaireData is Fail)
    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendSuccess() {

    }

    @Test
    fun testSendQuestionAnswerDataAndTurnOffAutoExtendError() {

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
}