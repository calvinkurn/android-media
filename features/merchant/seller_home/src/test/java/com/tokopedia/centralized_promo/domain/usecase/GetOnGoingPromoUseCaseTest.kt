package com.tokopedia.centralized_promo.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralized_promo.domain.mapper.PromotionMapper
import com.tokopedia.centralized_promo.domain.model.GetPromotionListResponseWrapper
import com.tokopedia.centralized_promo.view.model.Footer
import com.tokopedia.centralized_promo.view.model.OnGoingPromoListUiModel
import com.tokopedia.centralized_promo.view.model.OnGoingPromoUiModel
import com.tokopedia.centralized_promo.view.model.Status
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.rules.ExpectedException

@ExperimentalCoroutinesApi
class GetOnGoingPromoUseCaseTest {
    companion object {
        private const val SUCCESS_RESPONSE = "json/get_centralized_promo_on_going_usecase_success_response.json"
        private const val ERROR_RESPONSE_WITH_MESSAGE_ON_HEADER = "json/get_centralized_promo_on_going_usecase_failed_response.json"

        private val successResult = OnGoingPromoListUiModel(
                title = "Track your promotion",
                promotions = arrayListOf(
                        OnGoingPromoUiModel(
                                title = "Flash Sale",
                                status = Status(
                                        text = "Terdaftar",
                                        count = 56,
                                        url = "sellerapp://flashsale/management"
                                ),
                                footer = Footer(
                                        text = "Lihat Semua",
                                        url = "sellerapp://flashsale/management"
                                )
                        )
                ),
                errorMessage = ""
        )

        private val errorResultWithMessage = OnGoingPromoListUiModel(
                title = "Track your promotion",
                promotions = ArrayList(),
                errorMessage = "Terjadi Kesalahan Pada Server"
        )
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val onGoingPromotionMapper: PromotionMapper = PromotionMapper()

    private val usecase by lazy {
        GetOnGoingPromoUseCase(gqlRepository, onGoingPromotionMapper)
    }

    private val params = GetOnGoingPromoUseCase.getRequestParams(false)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get on going promotion data`() = runBlocking {
        usecase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetPromotionListResponseWrapper>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val onGoingPromotions = usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assertions.assertEquals(onGoingPromotions, successResult)
    }

    @Test
    fun `should error with error message inside header`() = runBlocking {
        usecase.params = params
        val errorResponse = TestHelper.createSuccessResponse<GetPromotionListResponseWrapper>(ERROR_RESPONSE_WITH_MESSAGE_ON_HEADER)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        val result = usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assertions.assertEquals(result, errorResultWithMessage)
    }

    @Test
    fun `should error get on going promotion data`() = runBlocking {
        usecase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetPromotionListResponseWrapper>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }
    }
}