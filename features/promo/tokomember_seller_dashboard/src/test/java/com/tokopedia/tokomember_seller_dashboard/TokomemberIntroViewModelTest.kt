package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashIntroUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberIntroSectionMapperUsecase
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashIntroViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TokomemberIntroViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TokomemberDashIntroViewModel
    private val tokomemberDashIntroUsecase = mockk< TokomemberDashIntroUsecase>(relaxed = true)
    private val tokomemberIntroSectionMapperUsecase = mockk<TokomemberIntroSectionMapperUsecase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel  = TokomemberDashIntroViewModel(tokomemberDashIntroUsecase,tokomemberIntroSectionMapperUsecase ,dispatcher)
    }

    @Test
    fun successIntroResult() {
        val data = mockk<MembershipData>(relaxed = true)
        coEvery {
            tokomemberDashIntroUsecase.getMemberOnboardingInfo(any(), any(), 0)
        } coAnswers {
            firstArg<(MembershipData) -> Unit>().invoke(data)
        }
        viewModel.getIntroInfo(0)

        Assert.assertEquals(
            (viewModel.tokomemberOnboardingResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun failIntroResult() {
        coEvery {
            tokomemberDashIntroUsecase.getMemberOnboardingInfo(any(), any(), 0)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getIntroInfo(0)
        Assert.assertEquals(
            (viewModel.tokomemberOnboardingResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }
}