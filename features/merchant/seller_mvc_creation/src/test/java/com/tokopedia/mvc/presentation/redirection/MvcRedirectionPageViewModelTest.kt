package com.tokopedia.mvc.presentation.redirection

import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse
import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse.*
import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse.MerchantPromotionGetPromoList.*
import com.tokopedia.mvc.data.response.GetMerchantPromoListResponse.MerchantPromotionGetPromoList.Data.*
import com.tokopedia.mvc.domain.usecase.GetMerchantPromoListUseCase
import com.tokopedia.mvc.presentation.redirection.uimodel.MvcRedirectionPageAction
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MvcRedirectionPageViewModelTest {

    private lateinit var viewModel: MvcRedirectionPageViewModel

    @RelaxedMockK
    lateinit var getMerchantPromoListUseCase: GetMerchantPromoListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MvcRedirectionPageViewModel(
            CoroutineTestDispatchers,
            userSession,
            getMerchantPromoListUseCase
        )
    }

    @Test
    fun `when getRedirectionAppLink() is called, should emit the correct action and pass mvc applink`() {
        runBlockingTest {
            // Given
            val sellerMvcAppLink = "applink"
            val expectedAction = MvcRedirectionPageAction.RedirectTo(sellerMvcAppLink)

            mockMerchantPromoListDataGQLCall()

            val emittedAction = arrayListOf<MvcRedirectionPageAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.getRedirectionAppLink()

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedAction, actual)

            job.cancel()
        }
    }

    @Test
    fun `when getRedirectionAppLink() is throwing an error, should emit the correct action`() {
        runBlockingTest {
            // Given
            val error = Throwable("Error")
            val expectedAction = MvcRedirectionPageAction.ShowError(error)

            coEvery { getMerchantPromoListUseCase.execute(any()) } throws error

            val emittedAction = arrayListOf<MvcRedirectionPageAction>()
            val job = launch {
                viewModel.uiAction.toList(emittedAction)
            }

            // When
            viewModel.getRedirectionAppLink()

            // Then
            val actual = emittedAction.last()
            assertEquals(expectedAction, actual)

            job.cancel()
        }
    }

    private fun mockMerchantPromoListDataGQLCall() {
        val result = GetMerchantPromoListResponse(
            merchantPromotionGetPromoList = MerchantPromotionGetPromoList(
                data = Data(
                    pages = listOf(
                        Page(
                            pageId = "65",
                            pageName = "",
                            ctaLink = "applink"
                        )
                    )
                )
            )
        )
        coEvery { getMerchantPromoListUseCase.execute(any()) } returns result
    }
}
