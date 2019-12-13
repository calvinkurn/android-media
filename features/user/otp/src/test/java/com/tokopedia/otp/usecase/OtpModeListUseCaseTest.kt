package com.tokopedia.otp.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.otp.ext.InstantRunExecutorSpek
import com.tokopedia.otp.validator.data.OtpModeListPojo
import com.tokopedia.otp.validator.domain.usecase.OtpModeListUseCase
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import stubExecuteOnBackground

@RunWith(JUnitPlatform::class)
class OtpModeListUseCaseTest : Spek({

    InstantRunExecutorSpek(this)

    given("otp mode list") {
        val multiRequestGraphqlUseCase by memoized { mockk<MultiRequestGraphqlUseCase>(relaxed = true) }
        val query = ""
        val otpModeListUseCase = OtpModeListUseCase(multiRequestGraphqlUseCase, query)

        on("get mode list") {
            val otpModeListResponse = OtpModeListPojo()

            otpModeListUseCase.stubExecuteOnBackground().returns(otpModeListResponse)

            it("should be return otp mode response") {
                runBlocking {
                    assert(otpModeListUseCase.executeOnBackground() == otpModeListResponse)
                }
            }
        }
    }
})