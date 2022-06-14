package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.local.model.PMActivationStatusUiModel
import com.tokopedia.gm.common.data.source.local.model.PMGradeBenefitInfoUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMGradeBenefitInfoUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.gm.common.domain.usecase.GetShopLevelUseCase
import com.tokopedia.gm.common.presentation.model.ShopLevelUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
    lateinit var getShopLevelUseCase: GetShopLevelUseCase

    @RelaxedMockK
    lateinit var getPMGradeBenefitInfoUseCase: GetPMGradeBenefitInfoUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var activatePMUseCase: PowerMerchantActivateUseCase

    private lateinit var viewModel: PowerMerchantSubscriptionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PowerMerchantSubscriptionViewModel(
            { getPMGradeBenefitInfoUseCase },
            { activatePMUseCase },
            { getShopLevelUseCase },
            { userSession },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get PM active state with PM Pro data should set result success`() =
        coroutineTestRule.runBlockingTest {
            val data = PMGradeBenefitInfoUiModel()
            val pmProTire = 1

            coEvery {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            } returns data

            viewModel.getPmActiveStateData(pmProTire)

            coVerify {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            }

            val expected = Success(data)

            viewModel.pmPmActiveData.verifySuccessEquals(expected)
        }

    @Test
    fun `when get PM active state with PM data should set result success`() =
        coroutineTestRule.runBlockingTest {
            val data = PMGradeBenefitInfoUiModel()
            val pmProTire = 0

            coEvery {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            } returns data

            viewModel.getPmActiveStateData(pmProTire)

            coVerify {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            }

            val expected = Success(data)

            viewModel.pmPmActiveData.verifySuccessEquals(expected)
        }

    @Test
    fun `when get PM active state data should set result failed`() =
        coroutineTestRule.runBlockingTest {
            val error = Throwable()
            coEvery {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            } throws error

            viewModel.getPmActiveStateData(1)

            coVerify {
                getPMGradeBenefitInfoUseCase.executeOnBackground()
            }

            val expected = Fail(error)

            viewModel.pmPmActiveData.verifyErrorEquals(expected)
        }

    @Test
    fun `when submit PM activation should set result success`() =
        coroutineTestRule.runBlockingTest {
            val result = PMActivationStatusUiModel()
            val source = anyString()

            activatePMUseCase.params =
                PowerMerchantActivateUseCase.createActivationParam(source)

            coEvery {
                activatePMUseCase.executeOnBackground()
            } returns result

            viewModel.submitPMActivation()

            coVerify {
                activatePMUseCase.executeOnBackground()
            }

            val expected = Success(result)

            viewModel.pmActivationStatus.verifySuccessEquals(expected)
        }

    @Test
    fun `when submit PM activation should set result failed`() = coroutineTestRule.runBlockingTest {
        val error = Throwable()
        val source = anyString()

        activatePMUseCase.params =
            PowerMerchantActivateUseCase.createActivationParam(source)

        coEvery {
            activatePMUseCase.executeOnBackground()
        } throws error

        viewModel.submitPMActivation()

        coVerify {
            activatePMUseCase.executeOnBackground()
        }

        val expected = Fail(error)

        viewModel.pmActivationStatus.verifyErrorEquals(expected)
    }

    @Test
    fun `when submit cancel PM deactivation should set result success`() =
        coroutineTestRule.runBlockingTest {
            val result = PMActivationStatusUiModel()

            coEvery {
                activatePMUseCase.executeOnBackground()
            } returns result

            viewModel.cancelPmDeactivationSubmission()

            coVerify {
                activatePMUseCase.executeOnBackground()
            }

            val expected = Success(result)

            viewModel.pmCancelDeactivationStatus.verifySuccessEquals(expected)
        }

    @Test
    fun `when submit cancel PM deactivation should set result failed`() =
        coroutineTestRule.runBlockingTest {
            val error = Throwable()

            coEvery {
                activatePMUseCase.executeOnBackground()
            } throws error

            viewModel.cancelPmDeactivationSubmission()

            coVerify {
                activatePMUseCase.executeOnBackground()
            }

            val expected = Fail(error)

            viewModel.pmCancelDeactivationStatus.verifyErrorEquals(expected)
        }

    @Test
    fun `when get shop level info widget then should return success result`() {
        coroutineTestRule.runBlockingTest {
            val mock = ShopLevelUiModel()

            coEvery {
                getShopLevelUseCase.execute(any())
            } returns mock

            viewModel.getShopLevelInfo()

            coVerify {
                getShopLevelUseCase.execute(any())
            }

            val expected = Success(mock)
            viewModel.shopLevelInfo.verifySuccessEquals(expected)
        }
    }

    @Test
    fun `when get shop level info widget then should return error result`() {
        coroutineTestRule.runBlockingTest {
            val error = Throwable()
            coEvery {
                getShopLevelUseCase.execute(any())
            } throws error

            viewModel.getShopLevelInfo()

            coVerify {
                getShopLevelUseCase.execute(any())
            }

            val expected = Fail(error)
            viewModel.shopLevelInfo.verifyErrorEquals(expected)
        }
    }
}