package com.tokopedia.managepassword.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordResponseModel
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordUseCase
import com.tokopedia.managepassword.changepassword.view.viewmodel.ChangePasswordViewModel
import com.tokopedia.managepassword.changepassword.view.viewmodel.LiveDataValidateResult
import com.tokopedia.managepassword.ext.InstantRunExecutorSpek
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ChangePasswordViewModelTest : Spek({
    InstantRunExecutorSpek(this)

    val changePasswordUseCase = mockk<ChangePasswordUseCase>(relaxed = true)
    val dispatcher = Dispatchers.Unconfined
    val viewModel = ChangePasswordViewModel(changePasswordUseCase, dispatcher)

    var encode: String = "qweqweqweqweqwe"
    var old: String = "test123"
    var new: String = "nyoba123"
    var confirmation: String = "nyoba123"
    var validationToken: String = "token123123123"

    Feature("password validation") {
        val observer = mockk<Observer<LiveDataValidateResult>>(relaxed = true)

        Scenario("validate - with empty params") {

            Given("set error observer") {
                old = ""
                new = ""
                confirmation = ""

                viewModel.validatePassword.observeForever(observer)
            }

            When("validate") {
                viewModel.validatePassword(old, new, confirmation)
            }

            Then("it should be not valid password") {
                verify { observer.onChanged(LiveDataValidateResult.EMPTY_PARAMS) }
                assert(viewModel.validatePassword.value == LiveDataValidateResult.EMPTY_PARAMS)
            }
        }

        Scenario("validate - with same password") {

            Given("set error observer") {
                old = "test123"
                new = "test123"
                confirmation = "test123"

                viewModel.validatePassword.observeForever(observer)
            }

            When("validate") {
                viewModel.validatePassword(old, new, confirmation)
            }

            Then("it should be not valid password") {
                verify { observer.onChanged(LiveDataValidateResult.SAME_WITH_OLD) }
                assert(viewModel.validatePassword.value == LiveDataValidateResult.SAME_WITH_OLD)
            }
        }

        Scenario("validate - password does not match") {

            Given("set error observer") {
                old = "nyoba123"
                new = "test123"
                confirmation = "test124"

                viewModel.validatePassword.observeForever(observer)
            }

            When("validate") {
                viewModel.validatePassword(old, new, confirmation)
            }

            Then("it should be not valid password") {
                verify { observer.onChanged(LiveDataValidateResult.CONFIRMATION_INVALID) }
                assert(viewModel.validatePassword.value == LiveDataValidateResult.CONFIRMATION_INVALID)
            }
        }

        Scenario("validate - success validate") {

            Given("set success observer") {
                old = "nyoba123"
                new = "test123"
                confirmation = "test123"

                viewModel.validatePassword.observeForever(observer)
            }

            When("validate") {
                viewModel.validatePassword(old, new, confirmation)
            }

            Then("it should be not valid password") {
                verify { observer.onChanged(LiveDataValidateResult.VALID) }
                assert(viewModel.validatePassword.value == LiveDataValidateResult.VALID)
            }
        }
    }

    Feature("submit password") {
        val observer = mockk<Observer<Result<ChangePasswordResponseModel>>>(relaxed = true)
        val changePasswordResponseModel = ChangePasswordResponseModel()
        val throwableMock = Throwable("Opps!")

        val success = Success(changePasswordResponseModel)
        val fail = Fail(throwableMock)

        Scenario("submit - failed input password") {

            Given("usecase on failed properly") {
                every {
                    changePasswordUseCase.submit(any(), captureLambda())
                } answers {
                    val onError = lambda<(Throwable) -> Unit>()
                    onError.invoke(throwableMock)
                }

                viewModel.response.observeForever(observer)
            }

            When("submit change password") {
                /* Try to type typo when input old password */
                viewModel.submitChangePassword(encode, new, confirmation, validationToken)
            }

            Then("it should be return failed data") {
                verify { observer.onChanged(fail) }
                assert(viewModel.response.value == Fail(throwableMock))
            }
        }

        Scenario("submit - success response") {

            Given("usecase on success properly") {
                every {
                    changePasswordUseCase.submit(captureLambda(), any())
                } answers {
                    val onSuccess = lambda<(ChangePasswordResponseModel) -> Unit>()
                    onSuccess.invoke(changePasswordResponseModel)
                }

                viewModel.response.observeForever(observer)
            }

            When("submit change password") {
                viewModel.submitChangePassword(encode, new, confirmation, validationToken)
            }

            Then("it should be return success data") {
                verify { observer.onChanged(success) }
                assert(viewModel.response.value == Success(changePasswordResponseModel))
            }
        }
    }
})