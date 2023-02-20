package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.data.GetWishlistCollectionResponse
import com.tokopedia.privacycenter.data.WishlistCollectionResponse
import com.tokopedia.privacycenter.data.WishlistCollectionsDataModel
import com.tokopedia.privacycenter.data.WishlistDataModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistStateResult
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

class GetWishlistCollectionUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: GetWishlistCollectionUseCase
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = GetWishlistCollectionUseCase(
            repository,
            dispatcher
        )
    }

    @Test
    fun `get wishlist collection then return not empty list`() = runBlocking {
        val parameter = 1
        val data = WishlistCollectionResponse(
            status = "OK",
            data = WishlistDataModel(
                collections = listOf(WishlistCollectionsDataModel(), WishlistCollectionsDataModel())
            )
        )
        val response = createSuccessResponse(GetWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SharingWishlistStateResult.RenderCollection)
        assertEquals(data.data, result.data)
        assertTrue(result.data.collections.isNotEmpty())
    }

    @Test
    fun `get wishlist collection then return empty list`() = runBlocking {
        val parameter = 1
        val data = WishlistCollectionResponse(
            status = "OK",
            data = WishlistDataModel(
                emptyWishlistImageUrl = "url"
            )
        )
        val response = createSuccessResponse(GetWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SharingWishlistStateResult.CollectionEmpty)
        assertEquals(data.data.emptyWishlistImageUrl, result.url)
    }

    @Test
    fun `get wishlist collection then failed`() = runBlocking {
        val parameter = 1
        val data = WishlistCollectionResponse(errorMessage = listOf("Error"))
        val response = createSuccessResponse(GetWishlistCollectionResponse(data))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is SharingWishlistStateResult.Fail)
        assertEquals(data.errorMessage.first(), result.error.message)
    }

}
