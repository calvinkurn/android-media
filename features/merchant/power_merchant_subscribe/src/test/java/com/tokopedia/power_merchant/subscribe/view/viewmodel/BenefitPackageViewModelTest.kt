package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.power_merchant.subscribe.domain.usecase.GetBenefitPackageUseCase
import com.tokopedia.power_merchant.subscribe.view.model.BaseBenefitPackageUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BenefitPackageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var benefitPackageUseCase: GetBenefitPackageUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: BenefitPackageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = BenefitPackageViewModel(
            CoroutineTestDispatchersProvider,
            userSession,
            Lazy { benefitPackageUseCase }
        )
    }

    @Test
    fun `when get benefit package should return success`() = runBlockingTest {
        val mockResult = listOf<BaseBenefitPackageUiModel>()

        coEvery {
            benefitPackageUseCase.executeOnBackground()
        } returns mockResult

        viewModel.getBenefitPackage()

        coVerify {
            benefitPackageUseCase.executeOnBackground()
        }

        val expectedResult = Success(mockResult)
        viewModel.benefitPackage.verifySuccessEquals(expectedResult)
    }

    @Test
    fun `when get benefit package should return failed`() = runBlockingTest {
        val throwable = Throwable()

        coEvery {
            benefitPackageUseCase.executeOnBackground()
        } throws throwable

        viewModel.getBenefitPackage()

        coVerify {
            benefitPackageUseCase.executeOnBackground()
        }

        val expectedResult = Fail(throwable)
        viewModel.benefitPackage.verifyErrorEquals(expectedResult)
    }
}