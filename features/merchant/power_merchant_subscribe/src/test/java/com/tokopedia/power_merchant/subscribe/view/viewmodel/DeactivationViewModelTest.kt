package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMDeactivationQuestionnaireUseCase
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyBoolean

/**
 * Created By @ilhamsuaib on 21/04/21
 */

@ExperimentalCoroutinesApi
class DeactivationViewModelTest {

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPmDeactivationQuestionnaireUseCase: GetPMDeactivationQuestionnaireUseCase

    @RelaxedMockK
    lateinit var deactivatePMUseCase: DeactivatePMUseCase

    private lateinit var viewModel: DeactivationViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = DeactivationViewModel(
                Lazy { getPmDeactivationQuestionnaireUseCase },
                Lazy { deactivatePMUseCase },
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get pm questionnaire list should return success`() = runBlocking {
        val pmTire = anyInt()
        getPmDeactivationQuestionnaireUseCase.params = GetPMDeactivationQuestionnaireUseCase.createParams(anyString(), pmTire)

        val result = DeactivationQuestionnaireUiModel()

        coEvery {
            getPmDeactivationQuestionnaireUseCase.executeOnBackground()
        } returns result

        viewModel.getPMCancellationQuestionnaireData(pmTire, anyBoolean())

        val expected = Success(result)

        viewModel.pmCancellationQuestionnaireData.verifySuccessEquals(expected)
    }

    @Test
    fun `when get pm questionnaire list should return failed`() = runBlocking {
        val pmTire = anyInt()
        getPmDeactivationQuestionnaireUseCase.params = GetPMDeactivationQuestionnaireUseCase.createParams(anyString(), pmTire)

        val exception = Throwable()

        coEvery {
            getPmDeactivationQuestionnaireUseCase.executeOnBackground()
        } throws exception

        viewModel.getPMCancellationQuestionnaireData(pmTire, anyBoolean())

        val expected = Fail(exception)

        viewModel.pmCancellationQuestionnaireData.verifyErrorEquals(expected)
    }

    @Test
    fun `when submit deactivation PM should return success`() = runBlocking {
        val questionsData = mutableListOf<PMCancellationQuestionnaireAnswerModel>()
        val currentShopTire = anyInt()
        val nextShopTire = anyInt()
        deactivatePMUseCase.params = DeactivatePMUseCase.createRequestParam(questionsData, currentShopTire, nextShopTire)

        coEvery {
            deactivatePMUseCase.executeOnBackground()
        } returns true

        viewModel.submitPmDeactivation(questionsData, currentShopTire, nextShopTire)

        val expectedResult = Success(true)

        viewModel.isSuccessDeactivate.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when submit deactivation PM should return failed`() = runBlocking {
        val questionsData = mutableListOf<PMCancellationQuestionnaireAnswerModel>()
        val currentShopTire = 2
        val nextShopTire = 1
        deactivatePMUseCase.params = DeactivatePMUseCase.createRequestParam(questionsData, currentShopTire, nextShopTire)

        val error = Throwable()
        coEvery {
            deactivatePMUseCase.executeOnBackground()
        } throws error

        viewModel.submitPmDeactivation(questionsData, currentShopTire, nextShopTire)

        val expectedResult = Fail(error)

        viewModel.isSuccessDeactivate.verifyErrorEquals(expectedResult)
    }
}