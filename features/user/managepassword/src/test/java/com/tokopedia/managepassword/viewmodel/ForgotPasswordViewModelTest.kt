package com.tokopedia.managepassword.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.ext.InstantRunExecutorSpek
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import com.tokopedia.managepassword.forgotpassword.domain.usecase.ForgotPasswordUseCase
import com.tokopedia.managepassword.forgotpassword.view.viewmodel.ForgotPasswordViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ForgotPasswordViewModelTest : Spek({
    InstantRunExecutorSpek(this)

    val useCase: ForgotPasswordUseCase = mockk(relaxed = true)
    val dispatcher = Dispatchers.IO
    val viewModel = ForgotPasswordViewModel(useCase, dispatcher)
    var paramEmail = ""

    Feature("Submit AddPassword") {
        val observer = mockk<Observer<Result<ForgotPasswordResponseModel>>>(relaxed = true)
        val responseMock = ForgotPasswordResponseModel()
        val throwableMock = Throwable("Opps!")

        Scenario("submit - failed response") {
            paramEmail = "test"
            
            Given("set response failed") {
                every { 
                    useCase.sendRequest(any(), captureLambda())
                } answers {
                    val onFailed = lambda<(Throwable) -> Unit>()
                    onFailed.invoke(throwableMock)
                }
            }
            
            Given("set observer") {
                viewModel.response.observeForever(observer)
            }

            When("submit add password") {
                viewModel.resetPassword(paramEmail)
            }

            Then("it should be return fail response") {
                every {
                    observer.onChanged(Fail(throwableMock))
                }

                assert(viewModel.response.value == Fail(throwableMock))
            }
        }

        Scenario("submit - success response") {
            paramEmail = "test1234"

            Given("set response success") {
                every {
                    useCase.sendRequest(captureLambda(), any())
                } answers {
                    val onSuccess = lambda<(ForgotPasswordResponseModel) -> Unit>()
                    onSuccess.invoke(responseMock)
                }
            }

            Given("set observer") {
                viewModel.response.observeForever(observer)
            }

            When("submit add password") {
                viewModel.resetPassword(paramEmail)
            }

            Then("it should be return success response") {
                every {
                    observer.onChanged(Success(responseMock))
                }

                assert(viewModel.response.value == Success(responseMock))
            }
        }
    }
})