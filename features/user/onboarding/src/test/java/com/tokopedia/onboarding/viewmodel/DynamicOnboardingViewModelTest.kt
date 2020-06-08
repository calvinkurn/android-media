package com.tokopedia.onboarding.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.onboarding.domain.model.ConfigDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import com.tokopedia.onboarding.util.InstantRunExecutorSpek
import com.tokopedia.onboarding.util.TestDispatcherProvider
import com.tokopedia.onboarding.view.viewmodel.DynamicOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class DynamicOnboardingViewModelTest : Spek({

    InstantRunExecutorSpek(this)

    val dynamicOnboardingUseCase = mockk<DynamicOnboardingUseCase>(relaxed = true)
    val viewModel = DynamicOnboardingViewModel(TestDispatcherProvider(), dynamicOnboardingUseCase)

    Feature("dynamic onboarding data config") {
        val observer = mockk<Observer<Result<ConfigDataModel>>>(relaxed = true)
        val sampleConfig = ConfigDataModel()

        Scenario("get valid config") {
            val success = Success(sampleConfig)

            Given("usecase return success data") {
                every {
                    dynamicOnboardingUseCase.getDynamicOnboardingData(captureLambda(), any())
                } answers {
                    val config = lambda<(ConfigDataModel) -> Unit>()
                    config.invoke(sampleConfig)
                }

                viewModel.configData.observeForever(observer)
            }

            When("get data") {
                viewModel.getData()
            }

            Then("it should be return success data") {
                verify { observer.onChanged(success) }
                assert(viewModel.configData.value == success)
            }
        }

        Scenario("get valid config") {
            val throwableMock = Throwable("Opps!")
            val fail = Fail(throwableMock)

            Given("usecase return success data") {
                every {
                    dynamicOnboardingUseCase.getDynamicOnboardingData(any(), captureLambda())
                } answers {
                    val throwable = lambda<(Throwable) -> Unit>()
                    throwable.invoke(throwableMock)
                }

                viewModel.configData.observeForever(observer)
            }

            When("get data") {
                viewModel.getData()
            }

            Then("it should be return success data") {
                verify { observer.onChanged(fail) }
                assert(viewModel.configData.value == fail)
            }
        }
    }
})