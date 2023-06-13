package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.UpdateWishlistCollectionResponse
import com.tokopedia.privacycenter.data.UpdateWishlistDataModel
import com.tokopedia.privacycenter.data.UpdateWishlistResponse
import com.tokopedia.privacycenter.data.WishlistCollectionByIdDataModel
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpdateWishlistCollectionUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: UpdateWishlistCollectionUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = UpdateWishlistCollectionUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `update wishlist collection then update success`() = runBlocking {
        val parameter = WishlistCollectionByIdDataModel()
        val data = UpdateWishlistResponse(
            status = "OK",
            data = UpdateWishlistDataModel(success = true)
        )
        val response = createSuccessResponse(UpdateWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(data.data, result.data)
    }

    @Test
    fun `update wishlist collection then update failed`() = runBlocking {
        val parameter = WishlistCollectionByIdDataModel()
        val data = UpdateWishlistResponse(
            status = "OK",
            data = UpdateWishlistDataModel(success = false, message = "Error")
        )
        val response = createSuccessResponse(UpdateWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.data.message, result.error.message)
    }

    @Test
    fun `update wishlist collection then status failed`() = runBlocking {
        val parameter = WishlistCollectionByIdDataModel()
        val data = UpdateWishlistResponse(errorMessage = listOf("Error"))
        val response = createSuccessResponse(UpdateWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.errorMessage.first(), result.error.message)
    }

}
