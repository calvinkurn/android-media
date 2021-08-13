package com.tokopedia.sellerappwidget.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerappwidget.data.model.GetChatResponse
import com.tokopedia.sellerappwidget.domain.mapper.ChatMapper
import com.tokopedia.sellerappwidget.utils.TestHelper
import com.tokopedia.sellerappwidget.view.model.ChatItemUiModel
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

/**
 * Created By @ilhamsuaib on 21/12/20
 */

class GetChatUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_chat_list_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var mapper: ChatMapper

    private lateinit var getChatUseCase: GetChatUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getChatUseCase = GetChatUseCase(gqlRepository, mapper)
    }

    @Test
    fun `should success get chat list`() = runBlocking {
        getChatUseCase.params = GetChatUseCase.creteParams()

        val successResponse: GraphqlResponse = TestHelper.createSuccessResponse<GetChatResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse

        coEvery {
            mapper.mapRemoteModelToUiModel(any())
        } returns chatUiModel()

        val actualResult: ChatUiModel = getChatUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        coVerify {
            mapper.mapRemoteModelToUiModel(any())
        }

        Assert.assertEquals(chatUiModel(), actualResult)
    }

    @Test
    fun `when failed get chat list then throw RuntimeException`() = runBlocking {
        getChatUseCase.params = GetChatUseCase.creteParams()

        val errorResponse = TestHelper.createErrorResponse<GetChatResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(RuntimeException::class.java)
        val actualResult = getChatUseCase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertNull(actualResult)
    }

    private fun chatUiModel() = ChatUiModel(
            chats = listOf(
                    ChatItemUiModel(
                            messageId = 144209147,
                            messageKey = "user-12299749~user-13270700",
                            userDisplayName = "Tokopedia Seller",
                            lastMessage = "Produk Product 50 telah dipindahkan ke Stok Kosong karena stok habis. Tambahkan stok di sini jika produk sudah tersedia kembali.",
                            lastReplyTime = "14.09"
                    )
            ),
            unreads = 1
    )
}