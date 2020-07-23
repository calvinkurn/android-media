package com.tokopedia.managepassword.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import com.tokopedia.managepassword.addpassword.domain.usecase.AddPasswordUseCase
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.ext.InstantRunExecutorSpek
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class AddPasswordViewModelTest : Spek({
    InstantRunExecutorSpek(this)

    val useCase: AddPasswordUseCase = mockk(relaxed = true)
    val dispatcher = Dispatchers.IO
    val viewModel = AddPasswordViewModel(useCase, dispatcher)
    var paramPassword = ""

    Feature("AddPassword Validation") {
        val observer = mockk<Observer<Result<String>>>(relaxed = true)
        var throwableMock: Throwable

        Scenario("validation - empty params") {
            throwableMock = Throwable(ERROR_FIELD_REQUIRED)

            Given("set empty params") {
                paramPassword = ""
            }

            Given("set observer") {
                viewModel.validatePassword.observeForever(observer)
                viewModel.validatePasswordConfirmation.observeForever(observer)
            }

            When("run validate") {
                viewModel.validatePassword(paramPassword)
                viewModel.validatePasswordConfirmation(paramPassword)
            }

            Then("it should be not valid password") {
                verify(exactly = 0) {
                    observer.onChanged(Fail(throwableMock))
                }

                assertEquals((viewModel.validatePassword.value as Fail).throwable.message, ERROR_FIELD_REQUIRED)
                assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, ERROR_FIELD_REQUIRED)

                viewModel.validatePassword.removeObserver(observer)
                viewModel.validatePasswordConfirmation.removeObserver(observer)
            }
        }

        Scenario("validation - param less than minimum char") {
            throwableMock = Throwable(ERROR_MIN_CHAR)

            Given("set params with 4 char") {
                paramPassword = "test"
            }

            Given("set observer") {
                viewModel.validatePassword.observeForever(observer)
                viewModel.validatePasswordConfirmation.observeForever(observer)
            }

            When("run validate") {
                viewModel.validatePassword(paramPassword)
                viewModel.validatePasswordConfirmation(paramPassword)
            }

            Then("it should be not valid password") {
                verify(exactly = 0) {
                    observer.onChanged(Fail(throwableMock))
                }

                assertEquals((viewModel.validatePassword.value as Fail).throwable.message, ERROR_MIN_CHAR)
                assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, ERROR_MIN_CHAR)

                viewModel.validatePassword.removeObserver(observer)
                viewModel.validatePasswordConfirmation.removeObserver(observer)
            }
        }

        Scenario("validation - param exceed than max char") {
            throwableMock = Throwable(ERROR_MAX_CHAR)

            Given("set params with 40 char") {
                paramPassword = "test123456789123456789123456789123456789"
            }

            Given("set observer") {
                viewModel.validatePassword.observeForever(observer)
                viewModel.validatePasswordConfirmation.observeForever(observer)
            }

            When("run validate") {
                viewModel.validatePassword(paramPassword)
                viewModel.validatePasswordConfirmation(paramPassword)
            }

            Then("it should be not valid password") {
                verify(exactly = 0) {
                    observer.onChanged(Fail(throwableMock))
                }

                assertEquals((viewModel.validatePassword.value as Fail).throwable.message, ERROR_MAX_CHAR)
                assertEquals((viewModel.validatePasswordConfirmation.value as Fail).throwable.message, ERROR_MAX_CHAR)

                viewModel.validatePassword.removeObserver(observer)
                viewModel.validatePasswordConfirmation.removeObserver(observer)
            }
        }

        Scenario("validation - valid params") {
            val responseMock = ""

            Given("set params with 8 char") {
                paramPassword = "test1234"
            }

            Given("set observer") {
                viewModel.validatePassword.observeForever(observer)
                viewModel.validatePasswordConfirmation.observeForever(observer)
            }

            When("run validate") {
                viewModel.validatePassword(paramPassword)
                viewModel.validatePasswordConfirmation(paramPassword)
            }

            Then("it should be not valid password") {
                verify {
                    observer.onChanged(Success(responseMock))
                }

                assert(viewModel.validatePassword.value == Success(responseMock))
                assert(viewModel.validatePasswordConfirmation.value == Success(responseMock))

                viewModel.validatePassword.removeObserver(observer)
                viewModel.validatePasswordConfirmation.removeObserver(observer)
            }
        }
    }

    Feature("Submit AddPassword") {
        val observer = mockk<Observer<Result<AddPasswordResponseModel>>>(relaxed = true)
        val responseMock = AddPasswordResponseModel(AddPasswordResponseModel.AddPassword(isSuccess = true))
        val throwableMock = Throwable("Opps!")

        Scenario("submit - failed response") {
            paramPassword = "test"


            Given("set response failed") {
                coEvery {
                    useCase.submit(any(), any())
                } coAnswers {
                    (secondArg() as (Throwable) -> Unit).invoke(throwableMock)
                }
            }
            
            Given("set observer") {
                viewModel.response.observeForever(observer)
            }

            When("submit add password") {
                runBlocking {
                    viewModel.createPassword(paramPassword, paramPassword)
                }
            }

            Then("it should be return fail response") {
                every {
                    observer.onChanged(Fail(throwableMock))
                }

                assertEquals(viewModel.response.value, Fail(throwableMock))
            }
        }

        Scenario("submit - success response") {
            paramPassword = "test1234"

            Given("set response success") {
                every {
                    useCase.submit(captureLambda(), any())
                } answers {
                    (firstArg() as (AddPasswordResponseModel) -> Unit).invoke(responseMock)
                }
            }

            Given("set observer") {
                viewModel.response.observeForever(observer)
            }

            When("submit add password") {
                viewModel.createPassword(paramPassword, paramPassword)
            }

            Then("it should be return success response") {
                every {
                    observer.onChanged(Success(responseMock))
                }

                assertEquals(viewModel.response.value, Success(responseMock))
            }
        }
    }
}) {

    companion object {
        private const val MIN_COUNT = 8
        private const val MAX_COUNT = 32
        private const val ERROR_FIELD_REQUIRED = "Harus diisi"
        private const val ERROR_MIN_CHAR = "Minimum $MIN_COUNT karakter"
        private const val ERROR_MAX_CHAR = "Maksimum $MAX_COUNT karakter"
    }
}