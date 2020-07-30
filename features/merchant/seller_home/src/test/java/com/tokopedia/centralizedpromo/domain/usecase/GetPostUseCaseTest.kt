package com.tokopedia.centralizedpromo.domain.usecase

import com.tokopedia.centralizedpromo.domain.mapper.PostMapper
import com.tokopedia.centralizedpromo.view.model.PostListUiModel
import com.tokopedia.centralizedpromo.view.model.PostUiModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.GetPostDataResponse
import com.tokopedia.sellerhome.utils.TestHelper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.rules.ExpectedException

@ExperimentalCoroutinesApi
class GetPostUseCaseTest {
    companion object {
        private const val SUCCESS_RESPONSE = "json/get_centralized_promo_post_usecase_success_response.json"

        private val successResult = PostListUiModel(
                items = listOf(
                        PostUiModel(
                                title = "Test Post",
                                applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                url = "https://static-staging.tokopedia.net/seller/merchant-info/test-post/",
                                featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                subtitle = "<p>Info &#183; 20 SEP 19</p>"
                        ),
                        PostUiModel(
                                title = "Test ke 2",
                                applink = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                url = "https://static-staging.tokopedia.net/seller/merchant-info/test-ke-2/",
                                featuredMediaUrl = "https://ecs7.tokopedia.net/img/blog/seller/2019/09/217_AM_-seller-center-1.jpg",
                                subtitle = "<p>Info &#183; 6 SEP 19</p>"
                        ),
                        PostUiModel(
                                title = "Kumpul Keluarga Tokopedia Bersama Toko Cabang",
                                applink = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                url = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/",
                                featuredMediaUrl = "https://seller.tokopedia.com/edu/seller-events/kumpul-keluarga-tc050320/tokocabang-event-seller-center_1024x439/",
                                subtitle = "<p>Seller Event &#183; 5 MAR 20</p>"
                        )
                ),
                errorMessage = ""
        )
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository
    private val postMapper: PostMapper = PostMapper()
    private val usecase by lazy {
        GetPostUseCase(gqlRepository, postMapper)
    }

    private val params = GetPostUseCase.getRequestParams(
            shopId = 0,
            dataKey = emptyList(),
            startDate = "",
            endDate = ""
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success get post list data`() = runBlocking {
        usecase.params = params
        val successResponse = TestHelper.createSuccessResponse<GetPostDataResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns successResponse


        val postList = usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        Assert.assertEquals(postList, successResult)
    }

    @Test
    fun `should failed when get card widget data`() = runBlocking {
        usecase.params = params
        val errorResponse = TestHelper.createErrorResponse<GetPostDataResponse>()

        coEvery {
            gqlRepository.getReseponse(any(), any())
        } returns errorResponse

        expectedException.expect(MessageErrorException::class.java)
        val result = usecase.executeOnBackground()

        coVerify {
            gqlRepository.getReseponse(any(), any())
        }

        assertTrue(result.items.isNullOrEmpty())
    }
}