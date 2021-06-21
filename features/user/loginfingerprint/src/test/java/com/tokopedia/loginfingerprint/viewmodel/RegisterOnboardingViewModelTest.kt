package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.util.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
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

class RegisterOnboardingViewModelTest : Spek({
    InstantTaskExecutorRule(this)
    val registerFingerprintUseCase = mockk<RegisterFingerprintUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val fingerprintSetting = mockk<FingerprintSetting>(relaxed = true)
    val cryptographyUtils = mockk<Cryptography>(relaxed = true)

    val dispatcher = CoroutineTestDispatchersProvider

    val viewModel = RegisterOnboardingViewModel(
            dispatcher = dispatcher,
            userSession = userSession,
            cryptographyUtils = cryptographyUtils,
            fingerprintSetting = fingerprintSetting,
            registerFingerprintUseCase = registerFingerprintUseCase
    )

    Feature("Unregister Fingerprint") {
        Scenario("Call unregister fingerprint") {
            When("unregister function called"){
                viewModel.unregisterFP()
            }
            Then("preference helper called") {
                verify { fingerprintSetting.unregisterFingerprint() }
            }
        }
    }

    Feature("Register Fingerprint") {
        val observerSuccess = mockk<Observer<Result<RegisterFingerprintResult>>>(relaxed = true)
        val observerError = mockk<Observer<Result<RegisterFingerprintResult>>>(relaxed = true)

        val registerFingerprintResult = RegisterFingerprintResult(success = true, errorMessage = "")
        val registerFingerprintPojo = RegisterFingerprintPojo(data = registerFingerprintResult)
        val registerFpResultSuccess = Success(registerFingerprintPojo.data)

        val throwableMock = Throwable("Error")
        val registerFpResultError = Fail(throwableMock)

        Scenario("register fingerprint api success") {
            Given("usecase on success properly") {
                every { registerFingerprintUseCase.executeUseCase(captureLambda(), any()) } answers {
                    val onSuccess = lambda<(RegisterFingerprintPojo) -> Unit>()
                    onSuccess.invoke(registerFingerprintPojo)
                }
                viewModel.verifyRegisterFingerprintResult.observeForever(observerSuccess)
            }
            When("hit api") {
                viewModel.registerFingerprint()
            }
            Then("it should return registration info correctly") {
                verify { fingerprintSetting.registerFingerprint() }
                verify { fingerprintSetting.saveUserId(any()) }
                verify { observerSuccess.onChanged(registerFpResultSuccess) }
                viewModel.verifyRegisterFingerprintResult.value.shouldBeInstanceOf<Success<RegisterFingerprintResult>>()
            }
        }

        Scenario("throw exception when error") {
            Given("usecase with throwable") {
                every { registerFingerprintUseCase.executeUseCase(any(), captureLambda()) } answers {
                    val onError = lambda<(Throwable) -> Unit>()
                    onError.invoke(throwableMock)
                }
                viewModel.verifyRegisterFingerprintResult.observeForever(observerError)
            }
            When("hit api") {
                viewModel.registerFingerprint()
            }

            Then("it should instance of Fail") {
                verify { observerError.onChanged(registerFpResultError) }
                viewModel.verifyRegisterFingerprintResult.value.shouldBeInstanceOf<Fail>()
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