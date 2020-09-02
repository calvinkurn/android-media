package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.ChatBlastSellerMetadataMapper
import com.tokopedia.centralizedpromo.domain.model.ChatBlastSellerMetadataResponse
import com.tokopedia.centralizedpromo.view.model.ChatBlastSellerMetadataUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
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
class GetChatBlastSellerMetadataUsecaseTest {
    companion object {
        private const val SUCCESS_RESPONSE = "json/get_centralized_promo_chat_blast_seller_metadata_usecase_success_response.json"

        private val successResult = ChatBlastSellerMetadataUiModel(
                promo = 1000,
                promoType = 2,
                url = "https://m.tokopedia.com/broadcast-chat"
        )
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    private val mapper: ChatBlastSellerMetadataMapper = ChatBlastSellerMetadataMapper()

    private val usecase by lazy { GetChatBlastSellerMetadataUseCase(gqlRepository, mapper) }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get chat blast seller metadata`() = runBlocking {
        val successResponse = TestHelper.createSuccessResponse<ChatBlastSellerMetadataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        val chatBlastMetadata = usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assertions.assertEquals(chatBlastMetadata, successResult)
    }

    @Test
    fun `should error get chat blast seller metadata`() = runBlocking {
        val errorResponse = TestHelper.createErrorResponse<ChatBlastSellerMetadataResponse>()

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