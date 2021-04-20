package com.tokopedia.onboarding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DynamicOnboardingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val dispatcher = CoroutineTestDispatchersProvider

    private val onboardingUseCase = mockk<DynamicOnboardingUseCase>(relaxed = true)
    private val observer = mockk<Observer<Result<ConfigDataModel>>>(relaxed = true)

    lateinit var viewmodel : DynamicOnboardingViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        viewmodel = DynamicOnboardingViewModel(dispatcher, onboardingUseCase)
        viewmodel.configData.observeForever(observer)
    }

    @Test
    fun `get data config - success`() {
        val mockkResponse = ConfigDataModel()
        val successValue = Success(mockkResponse)

        every {
            onboardingUseCase.getDynamicOnboardingData(any(), any())
        } answers {
            firstArg<(ConfigDataModel) -> Unit>().invoke(mockkResponse)
        }

        viewmodel.getData()

        verify {
            observer.onChanged((Success(mockkResponse)))
            assert(viewmodel.configData.value == successValue)
        }
    }

    @Test
    fun `get data config  - fail`() {
        val mockThrowable = Throwable("Ops!")
        val errorValue = Fail(mockThrowable)

        every {
            onboardingUseCase.getDynamicOnboardingData(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewmodel.getData()

        verify {
            observer.onChanged((Fail(mockThrowable)))
            assert(viewmodel.configData.value == errorValue)
        }
    }
}