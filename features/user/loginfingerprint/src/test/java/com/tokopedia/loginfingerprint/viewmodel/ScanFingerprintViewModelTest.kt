package com.tokopedia.loginfingerprint.viewmodel

/**
 * Created by Yoris Prayogo on 2020-02-24.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintResult
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.domain.usecase.ValidateFingerprintUseCase
import com.tokopedia.loginfingerprint.util.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class ScanFingerprintViewModelTest : Spek({
    InstantTaskExecutorRule(this)
    val validateFingerprintUseCase = mockk<ValidateFingerprintUseCase>(relaxed = true)
    val loginTokenUseCase = mockk<LoginTokenUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val fingerprintSetting = mockk<FingerprintSetting>(relaxed = true)
    val cryptographyUtils = mockk<Cryptography>(relaxed = true)

    val dispatcher = CoroutineTestDispatchersProvider

    val viewModel = ScanFingerprintViewModel(
            dispatcher = dispatcher,
            userSession = userSession,
            cryptographyUtils = cryptographyUtils,
            fingerprintSetting = fingerprintSetting,
            loginTokenUseCase = loginTokenUseCase,
            validateFingerprintUseCase = validateFingerprintUseCase
    )

    Feature("Login Token") {
        val observer = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)

        val loginToken = LoginToken()
        val loginTokenPojo = LoginTokenPojo(loginToken = loginToken)

        val throwableMock = Throwable()
        val validateFpResultError = Fail(throwableMock)

        Scenario("on success login token") {
            Given("usecase on success properly") {
                every { loginTokenUseCase.executeLoginFingerprint(any(), any()) } answers {
                    viewModel.loginSubscriber.onSuccessLoginToken.invoke(loginTokenPojo)
                }
                viewModel.loginFingerprintResult.observeForever(observer)
            }
            When("hit api") {
                viewModel.loginToken("")
            }
            Then("it should change value") {
                viewModel.loginFingerprintResult.value.shouldBeInstanceOf<Success<LoginTokenPojo>>()
            }
        }

        Scenario("throw exception when error") {
            Given("usecase with throwable") {
                every { validateFingerprintUseCase.executeUseCase(any(), any(), captureLambda()) } answers {
                    val onError = lambda<(Throwable) -> Unit>()
                    onError.invoke(throwableMock)
                }
                viewModel.loginFingerprintResult.observeForever(observer)
            }
            When("hit api") {
                viewModel.validateFingerprint()
            }

            Then("it should instance of Fail") {
                verify { observer.onChanged(validateFpResultError) }
                viewModel.loginFingerprintResult.value.shouldBeInstanceOf<Fail>()
            }
        }
    }

    Feature("Validate Fingerprint") {
        val observer = mockk<Observer<Result<LoginTokenPojo>>>(relaxed = true)

        val throwableMock = Throwable()
        val validateFpResultError = Fail(throwableMock)

        Scenario("validate fingerprint api success") {
            val validateFingerprintResult = ValidateFingerprintResult(success = true, validateToken = "abc123")

            Given("usecase on success properly") {
                every { validateFingerprintUseCase.executeUseCase(any(), captureLambda(), any()) } answers {
                    val onSuccess = lambda<(ValidateFingerprintResult) -> Unit>()
                    onSuccess.invoke(validateFingerprintResult)
                }
            }
            When("hit api") {
                viewModel.validateFingerprint()
            }
            Then("it should call login token") {
                verify { loginTokenUseCase.executeLoginFingerprint(any(), any()) }
            }
        }

        Scenario("throw exception when isSuccess = false") {
            val validateFingerprintResult = ValidateFingerprintResult(success = false, validateToken = "")
            Given("usecase on success properly") {
                every { validateFingerprintUseCase.executeUseCase(any(), captureLambda(), any()) } answers {
                    val onSuccess = lambda<(ValidateFingerprintResult) -> Unit>()
                    onSuccess.invoke(validateFingerprintResult)
                }
                viewModel.loginFingerprintResult.observeForever(observer)
            }

            When("hit api") {
                viewModel.validateFingerprint()
            }

            Then("it should changed to Fail") {
                viewModel.loginFingerprintResult.value.shouldBeInstanceOf<Fail>()
            }
        }

        Scenario("throw exception when errorMessage is not blank") {
            val validateFingerprintResult = ValidateFingerprintResult(success = true, validateToken = "", errorMessage = "error")
            Given("usecase on success properly") {
                every { validateFingerprintUseCase.executeUseCase(any(), captureLambda(), any()) } answers {
                    val onSuccess = lambda<(ValidateFingerprintResult) -> Unit>()
                    onSuccess.invoke(validateFingerprintResult)
                }
                viewModel.loginFingerprintResult.observeForever(observer)
            }

            When("hit api") {
                viewModel.validateFingerprint()
            }

            Then("it should changed to Fail") {
                viewModel.loginFingerprintResult.value.shouldBeInstanceOf<Fail>()
            }
        }

        Scenario("throw exception when error") {
            Given("usecase with throwable") {
                every { validateFingerprintUseCase.executeUseCase(any(), any(), captureLambda()) } answers {
                    val onError = lambda<(Throwable) -> Unit>()
                    onError.invoke(throwableMock)
                }
                viewModel.loginFingerprintResult.observeForever(observer)
            }
            When("hit api") {
                viewModel.validateFingerprint()
            }

            Then("it should instance of Fail") {
                verify { observer.onChanged(validateFpResultError) }
                viewModel.loginFingerprintResult.value.shouldBeInstanceOf<Fail>()
            }
        }

    }
})

private inline fun <reified T> Any?.shouldBeInstanceOf() {
    if (this !is T) {
        val actualClassName = if (this == null) "null" else this::class.simpleName
        val expectedClassName = T::class.simpleName

        throw AssertionError("$actualClassName should be instance of $expectedClassName")
    }
}