package com.tokopedia.tokofood.purchase

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.AgreeConsentData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.AgreeConsentResponse
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPurchaseConsentViewModelTest : TokoFoodPurchaseConsentViewModelTestFixture() {

    @Test
    fun `when agreeConsent success should set success data`() {
        runBlocking {
            val response = AgreeConsentResponse(
                tokofoodSubmitUserConsent = AgreeConsentData(
                    isSuccess = true
                )
            )
            coEvery {
                agreeConsentUseCase.get().execute()
            } returns response

            collectFromSharedFlow(
                whenAction = {
                    viewModel.agreeConsent()
                },
                then = {
                    assert(it is Result.Success)
                }
            )
        }
    }

    @Test
    fun `when agreeConsent failed should set failed data`() {
        runBlocking {
            coEvery {
                agreeConsentUseCase.get().execute()
            } throws MessageErrorException("")

            collectFromSharedFlow(
                whenAction = {
                    viewModel.agreeConsent()
                },
                then = {
                    assert(it is Result.Failure)
                }
            )
        }
    }

    @Test
    fun `when agreeConsent response failed should set failed data`() {
        runBlocking {
            val response = AgreeConsentResponse(
                tokofoodSubmitUserConsent = AgreeConsentData(
                    isSuccess = false
                )
            )
            coEvery {
                agreeConsentUseCase.get().execute()
            } returns response

            collectFromSharedFlow(
                whenAction = {
                    viewModel.agreeConsent()
                },
                then = {
                    assert(it is Result.Failure)
                }
            )
        }
    }

}