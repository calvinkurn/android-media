package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMGradeBenefitUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMActiveDataUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMActiveDataUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

/**
 * Created By @ilhamsuaib on 21/04/21
 */

@ExperimentalCoroutinesApi
class PowerMerchantSubscriptionViewModelTest {

    @get:Rule
    val ruleForLivaData = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getPMGradeBenefitUseCase: GetPMGradeBenefitUseCase

    @RelaxedMockK
    lateinit var getPMActiveDataUseCase: GetPMActiveDataUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var activatePMUseCase: PowerMerchantActivateUseCase

    private lateinit var viewModel: PowerMerchantSubscriptionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PowerMerchantSubscriptionViewModel(
                Lazy { getPMGradeBenefitUseCase },
                Lazy { getPMActiveDataUseCase },
                Lazy { activatePMUseCase },
                Lazy { userSession },
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get PM grade benefit should set result success`() = coroutineTestRule.runBlockingTest {
        val shopId = anyString()
        val source = anyString()
        getPMGradeBenefitUseCase.params = GetPMGradeBenefitUseCase.createParams(shopId, source)

        val result = emptyList<PMGradeWithBenefitsUiModel>()

        coEvery {
            getPMGradeBenefitUseCase.executeOnBackground()
        } returns result

        viewModel.getPmRegistrationData()

        coVerify {
            getPMGradeBenefitUseCase.executeOnBackground()
        }

        val expected = Success(result)

        viewModel.pmGradeBenefitInfo.verifySuccessEquals(expected)
    }

    @Test
    fun `when get PM grade benefit should set result failed`() = coroutineTestRule.runBlockingTest {
        val shopId = anyString()
        val source = anyString()
        getPMGradeBenefitUseCase.params = GetPMGradeBenefitUseCase.createParams(shopId, source)

        val throwable = Throwable()

        coEvery {
            getPMGradeBenefitUseCase.executeOnBackground()
        } throws throwable

        viewModel.getPmRegistrationData()

        coVerify {
            getPMGradeBenefitUseCase.executeOnBackground()
        }

        val expected = Fail(throwable)

        viewModel.pmGradeBenefitInfo.verifyErrorEquals(expected)
    }

    @Test
    fun `when get PM active state data should set result success`() = coroutineTestRule.runBlockingTest {
        val data = PMActiveDataUiModel()

        coEvery {
            getPMActiveDataUseCase.executeOnBackground()
        } returns data

        viewModel.getPmActiveStateData()

        coVerify {
            getPMActiveDataUseCase.executeOnBackground()
        }

        val expected = Success(data)

        viewModel.pmPmActiveData.verifySuccessEquals(expected)
    }

    @Test
    fun `when get PM active state data should set result failed`() = coroutineTestRule.runBlockingTest {
        val error = Throwable()
        coEvery {
            getPMActiveDataUseCase.executeOnBackground()
        } throws error

        viewModel.getPmActiveStateData()

        coVerify {
            getPMActiveDataUseCase.executeOnBackground()
        }

        val expected = Fail(error)

        viewModel.pmPmActiveData.verifyErrorEquals(expected)
    }

    @Test
    fun `when submit PM activation should set result success`() = coroutineTestRule.runBlockingTest {
        val result = PMActivationStatusUiModel()
        val currentShopTire = anyInt()
        val nextShopTire = anyInt()
        val source = anyString()

        activatePMUseCase.params = PowerMerchantActivateUseCase.createActivationParam(currentShopTire, nextShopTire, source)

        coEvery {
            activatePMUseCase.executeOnBackground()
        } returns result

        viewModel.submitPMActivation(currentShopTire, nextShopTire)

        coVerify {
            activatePMUseCase.executeOnBackground()
        }

        val expected = Success(result)

        viewModel.pmActivationStatus.verifySuccessEquals(expected)
    }

    @Test
    fun `when submit PM activation should set result failed`() = coroutineTestRule.runBlockingTest {
        val error = Throwable()
        val currentShopTire = anyInt()
        val nextShopTire = anyInt()
        val source = anyString()

        activatePMUseCase.params = PowerMerchantActivateUseCase.createActivationParam(currentShopTire, nextShopTire, source)

        coEvery {
            activatePMUseCase.executeOnBackground()
        } throws error

        viewModel.submitPMActivation(currentShopTire, nextShopTire)

        coVerify {
            activatePMUseCase.executeOnBackground()
        }

        val expected = Fail(error)

        viewModel.pmActivationStatus.verifyErrorEquals(expected)
    }

    @Test
    fun `when submit cancel PM deactivation should set result success`() = coroutineTestRule.runBlockingTest {
        val result = PMActivationStatusUiModel()
        val currentShopTire = anyInt()

        coEvery {
            activatePMUseCase.executeOnBackground()
        } returns result

        viewModel.cancelPmDeactivationSubmission(currentShopTire)

        coVerify {
            activatePMUseCase.executeOnBackground()
        }

        val expected = Success(result)

        viewModel.pmCancelDeactivationStatus.verifySuccessEquals(expected)
    }

    @Test
    fun `when submit cancel PM deactivation should set result failed`() = coroutineTestRule.runBlockingTest {
        val error = Throwable()
        val currentShopTire = anyInt()

        coEvery {
            activatePMUseCase.executeOnBackground()
        } throws error

        viewModel.cancelPmDeactivationSubmission(currentShopTire)

        coVerify {
            activatePMUseCase.executeOnBackground()
        }

        val expected = Fail(error)

        viewModel.pmCancelDeactivationStatus.verifyErrorEquals(expected)
    }
}