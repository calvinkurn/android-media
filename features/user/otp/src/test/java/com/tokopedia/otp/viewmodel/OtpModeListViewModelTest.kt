package com.tokopedia.otp.viewmodel

import com.tokopedia.otp.ext.InstantRunExecutorSpek
import com.tokopedia.otp.validator.data.OtpModeListPojo
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.domain.usecase.OtpModeListUseCase
import com.tokopedia.otp.validator.viewmodel.OtpModeListViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import stubExecuteOnBackground

@RunWith(JUnitPlatform::class)
class OtpModeListViewModelTest : Spek({

    InstantRunExecutorSpek(this)

    Feature("OtpModeListViewModel") {
        val otpModeListUseCase = mockk<OtpModeListUseCase>(relaxed = true)
        val dispatcher = Dispatchers.Unconfined

        val otpModeListViewModel = OtpModeListViewModel(
                otpModeListUseCase,
                dispatcher
        )

        Scenario("[onSuccess] get otp mode list") {
            val modeListResponse = OtpModeListPojo()

            Given("set a successful response") {
                otpModeListUseCase.stubExecuteOnBackground().returns(modeListResponse)
            }

            When("get otp mode list") {
                val params = OtpParams()
                otpModeListViewModel.getMethodList(params)
            }

            Then("should return mode list") {
                val result = otpModeListViewModel.modeListResponse.value
                assert(result == Success(modeListResponse.data.modeList.toMutableList()))
            }
        }

        Scenario("[onFailed] shouldn't casting to OtpModeListPojo") {
            val modeListResponse = ClassCastException()

            Given("set a failed response") {
                otpModeListUseCase.stubExecuteOnBackground().returns(modeListResponse)
            }

            When("get otp mode list") {
                val params = OtpParams()
                otpModeListViewModel.getMethodList(params)
            }

            Then("should return mode list") {
                val result = otpModeListViewModel.error.value
                assertEquals(result?.javaClass, modeListResponse.javaClass)
            }
        }
    }
})