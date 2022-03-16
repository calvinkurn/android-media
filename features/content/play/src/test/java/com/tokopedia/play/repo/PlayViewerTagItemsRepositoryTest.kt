package com.tokopedia.play.repo

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.repository.PlayViewerTagItemRepositoryImpl
import com.tokopedia.play.domain.GetProductTagItemSectionUseCase
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 11/03/22
 */
@ExperimentalCoroutinesApi
class PlayViewerTagItemsRepositoryTest {
    lateinit var tagItemRepo: PlayViewerTagItemRepository

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val testDispatcher = CoroutineTestDispatchers

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val mockMapper: PlayUiModelMapper = mockk(relaxed = true)

    private val mockGetProductTagUseCase: GetProductTagItemSectionUseCase = mockk(relaxed = true)
    private val mockGetProductVariantUseCase: GetProductVariantUseCase = mockk(relaxed = true)
    private val mockAddToCartUseCase: AddToCartUseCase = mockk(relaxed = true)

    private val modelBuilder = ModelBuilder()

    @Before
    fun setUp(){
        tagItemRepo = PlayViewerTagItemRepositoryImpl(
            mockGetProductTagUseCase,
            mockGetProductVariantUseCase,
            mockAddToCartUseCase,
            mockMapper,
            mockUserSession,
            testDispatcher
        )
    }

    @Test
    fun  `when get section success return success with complete config`(){
        runBlockingTest {
            val mockResponse = modelBuilder.generateResponseSectionGql(gradient = null)

            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.getTagItem(
                "12669"
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
        }
    }

    @Test
    fun  `when get section success return success with bg config null`(){
        runBlockingTest {
            val mockResponse = modelBuilder.generateResponseSectionGql(gradient = listOf("3fffff", "#45a5aa"))

            coEvery { mockGetProductTagUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.getTagItem(
                "12669"
            )

            coVerify { mockGetProductTagUseCase.executeOnBackground() }
            response.resultState.assertEqualTo(ResultState.Success)
        }
    }

    @Test
    fun  `when ATC success return success response`(){
        runBlockingTest {
            val mockCartId = "1"
            val mockResponse = AddToCartDataModel(
                errorMessage = arrayListOf(),
                data = DataModel(
                    success = 1,
                    cartId = mockCartId
                )
            )
            coEvery { mockAddToCartUseCase.executeOnBackground() } returns mockResponse

            val response = tagItemRepo.addProductToCart(
                "1",
                "Product Test",
                "1",
                1,
                12000.0
            )

            coVerify { mockAddToCartUseCase.executeOnBackground() }
            response.assertEqualTo(mockCartId)
        }
    }

    @Test
    fun  `when ATC error return failed response = exception`(){
        runBlockingTest {
            val mockCartId = "1"
            val mockErrorResponse = AddToCartDataModel(
                errorMessage = arrayListOf("Error Message"),
                data = DataModel(
                    success = 0,
                    cartId = mockCartId
                )
            )
            coEvery { mockAddToCartUseCase.executeOnBackground() } returns mockErrorResponse

            try {
                val response = tagItemRepo.addProductToCart(
                    "1",
                    "Product Test",
                    "1",
                    1,
                    12000.0
                )
                coVerify { mockAddToCartUseCase.executeOnBackground() }
            } catch (e: Exception){
                (e is MessageErrorException).assertTrue()
            }
        }
    }
}