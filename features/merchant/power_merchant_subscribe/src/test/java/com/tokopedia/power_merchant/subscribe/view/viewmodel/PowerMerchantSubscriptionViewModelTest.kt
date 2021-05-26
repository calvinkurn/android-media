package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.data.source.local.model.PMGradeWithBenefitsUiModel
import com.tokopedia.gm.common.data.source.local.model.PMShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusAndShopInfoUiModel
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusAndShopInfoUseCase
import com.tokopedia.gm.common.domain.interactor.PowerMerchantActivateUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMActiveDataUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMGradeBenefitAndShopInfoUseCase
import com.tokopedia.power_merchant.subscribe.view.model.PMActiveDataUiModel
import com.tokopedia.power_merchant.subscribe.view.model.PMGradeBenefitAndShopInfoUiModel
import com.tokopedia.power_merchant.subscribe.view_old.util.PowerMerchantRemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    lateinit var getPMStatusAndShopInfo: GetPMStatusAndShopInfoUseCase

    @RelaxedMockK
    lateinit var getPMGradeWithBenefitAndShopInfoUseCase: GetPMGradeBenefitAndShopInfoUseCase

    @RelaxedMockK
    lateinit var getPMActiveDataUseCase: GetPMActiveDataUseCase

    @RelaxedMockK
    lateinit var activatePMUseCase: PowerMerchantActivateUseCase

    @RelaxedMockK
    lateinit var remoteConfig: PowerMerchantRemoteConfig

    private lateinit var viewModel: PowerMerchantSubscriptionViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PowerMerchantSubscriptionViewModel(
                Lazy { getPMStatusAndShopInfo },
                Lazy { getPMGradeWithBenefitAndShopInfoUseCase },
                Lazy { getPMActiveDataUseCase },
                Lazy { activatePMUseCase },
                Lazy { remoteConfig },
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when get PM status and shop info should set result success`() = coroutineTestRule.runBlockingTest {
        val data = PMStatusAndShopInfoUiModel(PMStatusUiModel(), PMShopInfoUiModel(), true)
        coEvery {
            remoteConfig.isFreeShippingEnabled()
        } returns true

        coEvery {
            getPMStatusAndShopInfo.executeOnBackground()
        } returns data

        viewModel.getPmStatusAndShopInfo()

        coEvery {
            remoteConfig.isFreeShippingEnabled()
        }

        coVerify {
            getPMStatusAndShopInfo.executeOnBackground()
        }

        val expected = Success(data)

        viewModel.pmStatusAndShopInfo.verifySuccessEquals(expected)
    }

    @Test
    fun `when get PM status and shop info should set result failed`() = coroutineTestRule.runBlockingTest {
        val throwable = Throwable()
        coEvery {
            getPMStatusAndShopInfo.executeOnBackground()
        } throws throwable

        viewModel.getPmStatusAndShopInfo()

        coEvery {
            remoteConfig.isFreeShippingEnabled()
        }

        coVerify {
            getPMStatusAndShopInfo.executeOnBackground()
        }

        val expected = Fail(throwable)

        viewModel.pmStatusAndShopInfo.verifyErrorEquals(expected)
    }

    @Test
    fun `when get PM registration data should set result success`() = coroutineTestRule.runBlockingTest {
        val data = PMGradeBenefitAndShopInfoUiModel(PMShopInfoUiModel(), listOf(PMGradeWithBenefitsUiModel()))
        coEvery {
            getPMGradeWithBenefitAndShopInfoUseCase.executeOnBackground()
        } returns data

        viewModel.getPmRegistrationData()

        coVerify {
            getPMGradeWithBenefitAndShopInfoUseCase.executeOnBackground()
        }

        val expected = Success(data)

        viewModel.shopInfoAndPMGradeBenefits.verifySuccessEquals(expected)
    }

    @Test
    fun `when get PM registration data should set result failed`() = coroutineTestRule.runBlockingTest {
        val error = Throwable()
        coEvery {
            getPMGradeWithBenefitAndShopInfoUseCase.executeOnBackground()
        } throws error

        viewModel.getPmRegistrationData()

        coVerify {
            getPMGradeWithBenefitAndShopInfoUseCase.executeOnBackground()
        }

        val expected = Fail(error)

        viewModel.shopInfoAndPMGradeBenefits.verifyErrorEquals(expected)
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
        val result = true

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
    fun `when submit cancel PM deactivation should set result success`() = coroutineTestRule.runBlockingTest {
        val result = true
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
    fun `when submit cancel PM deactivation should set result failed`() = coroutineTestRule.runBlockingTest {
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
}