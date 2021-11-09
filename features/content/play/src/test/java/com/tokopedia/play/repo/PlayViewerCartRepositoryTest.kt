package com.tokopedia.play.repo

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.play.data.repository.PlayViewerCartRepositoryImpl
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.domain.repository.PlayViewerCartRepository
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.view.uimodel.mapper.PlayCartMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created By : Jonathan Darwin on November 08, 2021
 */
class PlayViewerCartRepositoryTest {

    lateinit var cartRepository: PlayViewerCartRepository

    private val mockPostAddToCartUseCase: PostAddToCartUseCase = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val testDispatcher = CoroutineTestDispatchers
    lateinit var mockMapper: PlayUiModelMapper
    private val cartMapper = PlayCartMapper()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        mockMapper = PlayUiModelMapper(
            productTagMapper = mockk(relaxed = true),
            merchantVoucherMapper = mockk(relaxed = true),
            chatMapper = mockk(relaxed = true),
            channelStatusMapper = mockk(relaxed = true),
            channelInteractiveMapper = mockk(relaxed = true),
            interactiveLeaderboardMapper = mockk(relaxed = true),
            cartMapper = cartMapper
        )

        cartRepository = PlayViewerCartRepositoryImpl(
            mockPostAddToCartUseCase,
            mockUserSession,
            testDispatcher,
            mockMapper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when atc success, it should return cart appropriate success response`() {
        runBlockingTest {

            /** Prepare */
            val mockErrorMessage = ""
            val mockCartId = "1"
            val mockResponse = AddToCartDataModel(
                errorMessage = arrayListOf(),
                data = DataModel(
                    success = 1,
                    cartId = mockCartId
                )
            )
            coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns mockResponse

            /** Call */
            val response = cartRepository.addItemToCart(
                "1",
                "Product Test",
                "1",
                "12.000",
                1
            )

            /** Verify */
            response.isSuccess.assertTrue()
            response.errorMessage.isEqualTo(mockErrorMessage)
            response.cartId.isEqualTo(mockCartId)
        }
    }

    @Test
    fun `when atc failed, it should return cart appropriate error response`() {
        runBlockingTest {

            /** Prepare */
            val mockErrorMessage = "Error 1 , Error 2 "
            val mockResponse = AddToCartDataModel(
                errorMessage = arrayListOf("Error 1", "Error 2"),
                data = DataModel(
                    success = 0
                )
            )
            coEvery { mockPostAddToCartUseCase.executeOnBackground() } returns mockResponse

            /** Call */
            val response = cartRepository.addItemToCart(
                "1",
                "Product Test",
                "1",
                "12.000",
                1
            )

            /** Verify */
            response.isSuccess.assertFalse()
            response.errorMessage.isEqualTo(mockErrorMessage)
            response.cartId.isEqualTo("")
        }
    }
}