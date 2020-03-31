package com.tokopedia.product.manage.feature.cashback

import com.tokopedia.product.manage.feature.cashback.data.GoldSetProductCashback
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackHeader
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResponse
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProductManageSetCashbackViewModelTest: ProductManageSetCashbackViewModelTestFixture() {

    @Test
    fun `when_set_cashback_success__should_return_succes_result`() {
        runBlocking {
            val productId = "0"
            val cashback = 0
            val productName = "Amazing Product"
            val setCashbackResponse = SetCashbackResponse(
                    GoldSetProductCashback(
                            SetCashbackHeader(
                                  errorCode = "200"
                            )
                    )
            )

            onSetCashback_thenReturn(setCashbackResponse)

            viewModel.setCashback(productId, productName, cashback)

            val expectedSetCashbackResult = Success(SetCashbackResult(productId, productName, cashback))

            verifySetCashbackUseCaseCalled()
            verifySetCashbackSuccessResult(expectedSetCashbackResult)
        }
    }

    @Test
    fun `when_set_cashback_limit_reached__should_return_succes_result`() {
        runBlocking {
            val productId = "0"
            val cashback = 0
            val productName = "Amazing Product"
            val setCashbackResponse = SetCashbackResponse(
                    GoldSetProductCashback(
                            SetCashbackHeader(
                                    errorCode = "422"
                            )
                    )
            )

            onSetCashback_thenReturn(setCashbackResponse)

            viewModel.setCashback(productId, productName, cashback)

            val expectedSetCashbackResult = Success(SetCashbackResult(limitExceeded = true))

            verifySetCashbackUseCaseCalled()
            verifySetCashbackLimitReached(expectedSetCashbackResult)
        }
    }

    @Test
    fun `when_set_cashback_fail__should_return_succes_result`() {
        runBlocking {
            val productId = "0"
            val cashback = 0
            val productName = "Amazing Product"
            val setCashbackResponse = SetCashbackResponse(
                    GoldSetProductCashback(
                            SetCashbackHeader(
                                    errorCode = "404"
                            )
                    )
            )

            onSetCashback_thenReturn(setCashbackResponse)

            viewModel.setCashback(productId, productName, cashback)

            val expectedSetCashbackResult = Fail(SetCashbackResult(productId, productName, cashback))

            verifySetCashbackUseCaseCalled()
            verifySetCashbackFailed(expectedSetCashbackResult)
        }
    }

    private fun onSetCashback_thenReturn(setCashbackResponse: SetCashbackResponse)  {
        coEvery { setCashbackUseCase.executeOnBackground() } returns setCashbackResponse
    }

    private fun verifySetCashbackUseCaseCalled() {
        coVerify { setCashbackUseCase.executeOnBackground() }
    }

    private fun verifySetCashbackSuccessResult(expectedResult: Success<SetCashbackResult>) {
        val actualResult = viewModel.setCashbackResult.value as Success<SetCashbackResult>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifySetCashbackLimitReached(expectedResult: Success<SetCashbackResult>) {
        val actualResult = viewModel.setCashbackResult.value as Success<SetCashbackResult>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifySetCashbackFailed(expectedResult: Fail) {
        val actualResult = viewModel.setCashbackResult.value as Fail
        assertEquals(expectedResult.throwable, actualResult.throwable)
    }
}