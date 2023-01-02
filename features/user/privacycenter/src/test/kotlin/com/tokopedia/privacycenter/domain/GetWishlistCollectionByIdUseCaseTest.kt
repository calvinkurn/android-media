package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.GetWishlistCollectionByIdResponse
import com.tokopedia.privacycenter.data.WishlistCollectionByIdResponse
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

class GetWishlistCollectionByIdUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetWishlistCollectionByIdUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = GetWishlistCollectionByIdUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get wishlist bt id then success get list`() = runBlocking {
        val parameter = 1
        val data = WishlistCollectionByIdResponse(status = "OK")
        val response = createSuccessResponse(GetWishlistCollectionByIdResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(data.data, result.data)
    }

    @Test
    fun `get wishlist bt id then failed get list`() = runBlocking {
        val parameter = 1
        val data = WishlistCollectionByIdResponse(errorMessage = listOf("Error"))
        val response = createSuccessResponse(GetWishlistCollectionByIdResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(data.errorMessage.first(), result.error.message)
    }

}
