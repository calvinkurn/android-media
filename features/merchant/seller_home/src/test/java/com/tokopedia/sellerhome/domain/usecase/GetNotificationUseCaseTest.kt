package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.mapper.NotificationMapper
import com.tokopedia.sellerhome.domain.model.GetNotificationsResponse
import com.tokopedia.sellerhome.utils.TestHelper
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.rules.ExpectedException

/**
 * Created By @ilhamsuaib on 13/03/20
 */

@ExperimentalCoroutinesApi
class GetNotificationUseCaseTest {

    companion object {
        private const val SUCCESS_RESPONSE = "json/get_notification_success_response.json"
    }

    @get:Rule
    val expectedException = ExpectedException.none()

    @RelaxedMockK
    lateinit var gqlRepository: GraphqlRepository

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private val getNotificationUseCase by lazy {
        GetNotificationUseCase(gqlRepository, NotificationMapper(), userSession)
    }

    private val testShopId = "123"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success when get notification`() = runBlocking {
        val successResponse = TestHelper.createSuccessResponse<GetNotificationsResponse>(SUCCESS_RESPONSE)

        coEvery {
            gqlRepository.response(any(), any())
        } returns successResponse

        every {
            userSession.shopId
        } returns testShopId

        val actualNotification = getNotificationUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertEquals(getExpectedNotification(), actualNotification)
    }

    @Test
    fun `should failed when get notification`() = runBlocking {
        val errorResponse = TestHelper.createErrorResponse<GetNotificationsResponse>()

        coEvery {
            gqlRepository.response(any(), any())
        } returns errorResponse

        every {
            userSession.shopId
        } returns testShopId

        expectedException.expect(MessageErrorException::class.java)
        val actualNotification: NotificationUiModel = getNotificationUseCase.executeOnBackground()

        coVerify {
            gqlRepository.response(any(), any())
        }

        assertNull(actualNotification)
    }

    private fun getExpectedNotification(): NotificationUiModel {
        return NotificationUiModel(
                chat = 0,
                notifCenterUnread = 0,
                sellerOrderStatus = NotificationSellerOrderStatusUiModel(
                        newOrder = 16,
                        readyToShip = 10
                )
        )
    }
}