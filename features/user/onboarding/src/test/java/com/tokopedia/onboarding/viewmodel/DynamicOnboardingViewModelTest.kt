package com.tokopedia.onboarding.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.domain.model.DynamicOnboardingResponseDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import com.tokopedia.onboarding.util.FileUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DynamicOnboardingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val dispatcher = CoroutineTestDispatchersProvider

    @RelaxedMockK
    lateinit var onboardingUseCase: DynamicOnboardingUseCase

    @RelaxedMockK
    lateinit var observer: Observer<Result<ConfigDataModel>>

    lateinit var viewModel : DynamicOnboardingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DynamicOnboardingViewModel(dispatcher, onboardingUseCase)
        viewModel.configData.observeForever(observer)
    }

    @Test
    fun `get data config - success`() {

        //GIVEN
        val mockkResponse = DynamicOnboardingResponseDataModel()
        val exampleConfigResponse = "{\"user-ob-navigation\":{\"componentName\":\"navigation\",\"visibility\":true,\"previous\":{\"name\":\"\",\"visibility\":false,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\"},\"next\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\"},\"skipButton\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"tokopedia://home\",\"selectedColor\":\"\",\"defaultColor\":\"\"},\"indicators\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\"}},\"user-ob-pages\":[{\"pageNumber\":0,\"pageName\":\"\",\"components\":{\"text\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":0,\"componentName\":\"\",\"text\":\"Pakai Tokopedia, dapat promo \\u0026 hadiah!\",\"textColor\":\"#313131\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"button\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"tokopedia://registration\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":2,\"componentName\":\"\",\"text\":\"Gabung Sekarang\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"image\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":1,\"componentName\":\"\",\"text\":\"\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"https://images.tokopedia.net/img/fux/onboarding/onboarding_fux_01.png\",\"animationUrl\":\"\"}},\"totalComponents\":3},{\"pageNumber\":1,\"pageName\":\"\",\"components\":{\"text\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":0,\"componentName\":\"\",\"text\":\"Belanja lebih hemat pakai Bebas Ongkir!\",\"textColor\":\"#313131\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"button\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"tokopedia://registration\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":2,\"componentName\":\"\",\"text\":\"Gabung Sekarang\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"image\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":1,\"componentName\":\"\",\"text\":\"\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"https://images.tokopedia.net/img/fux/onboarding/onboarding_fux_02.png\",\"animationUrl\":\"\"}},\"totalComponents\":3},{\"pageNumber\":2,\"pageName\":\"\",\"components\":{\"text\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":0,\"componentName\":\"\",\"text\":\"Satu aplikasi untuk segala kebutuhanmu\",\"textColor\":\"#313131\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"button\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"tokopedia://registration\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":2,\"componentName\":\"\",\"text\":\"Gabung Sekarang\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"\",\"animationUrl\":\"\"},\"image\":{\"name\":\"\",\"visibility\":true,\"color\":\"\",\"appLink\":\"\",\"selectedColor\":\"\",\"defaultColor\":\"\",\"componentLevel\":1,\"componentName\":\"\",\"text\":\"\",\"textColor\":\"\",\"typographyType\":\"\",\"imageUrl\":\"https://images.tokopedia.net/img/fux/onboarding/onboarding_fux_03.png\",\"animationUrl\":\"\"}},\"totalComponents\":3}]}"
        mockkResponse.apply {
            dyanmicOnboarding.config = exampleConfigResponse
            dyanmicOnboarding.isEnable = true
        }

        val mockkConfig = FileUtil.parse<ConfigDataModel>(
            "/config_response_example.json",
            ConfigDataModel::class.java
        )

        val successValue = Success(mockkConfig)

        //WHEN
        coEvery {
            onboardingUseCase(Unit)
        } returns mockkResponse

        viewModel.getData()

        //THEN
        coVerify {
            observer.onChanged(successValue)
            assert(viewModel.configData.value == successValue)
        }
    }

    @Test
    fun `get data config calls on error - fail`() {

        //GIVEN
        val mockThrowable = Throwable("Ops!")
        val errorValue = Fail(mockThrowable)

        coEvery {
            onboardingUseCase(Unit)
        } throws mockThrowable

        //WHEN
        viewModel.getData()

        //THEN
        coVerify {
            observer.onChanged(errorValue)
            assert(viewModel.configData.value == errorValue)
        }
    }

    @Test
    fun `get data config disabled - fail`() {
        //GIVEN
        val mockkResponse = DynamicOnboardingResponseDataModel()
        mockkResponse.apply {
            dyanmicOnboarding.isEnable = false
            dyanmicOnboarding.message = "dummy error message"
        }

        val failValue = Fail(Throwable(mockkResponse.dyanmicOnboarding.message))

        coEvery {
            onboardingUseCase(Unit)
        } returns mockkResponse

        //WHEN
        viewModel.getData()

        //THEN
        coVerify {
            observer.onChanged(viewModel.configData.value)
            assert((viewModel.configData.value as Fail).throwable.message == failValue.throwable.message)
        }
    }

    @Test
    fun `get data config - timeout`() {

        //GIVEN
        val errorValue = Fail(Throwable(DynamicOnboardingViewModel.JOB_WAS_CANCELED))
        val mockkResponse = DynamicOnboardingResponseDataModel()

        val answerF = CoFunctionAnswer {
            delay(3000)
            mockkResponse
        }

        coEvery { onboardingUseCase(Unit) }.answers(answerF)

        //WHEN
        viewModel.getData()

        //THEN
        coVerify(timeout = 3000) {
            observer.onChanged(any())
        }
    }
}