package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.data.DeleteSearchHistoryParam
import com.tokopedia.privacycenter.data.DeleteSearchHistoryResponse
import com.tokopedia.privacycenter.data.UniverseDeleteRecentSearch
import com.tokopedia.privacycenter.utils.createSuccessResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeleteSearchHistoryUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: DeleteSearchHistoryUseCase
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = DeleteSearchHistoryUseCase(
            repository,
            userSession,
            dispatcher
        )
    }

    @Test
    fun `delete one item then success`() = runBlocking{
        val position = 0
        val isClearAll = false
        val parameter = DeleteSearchHistoryParam(
            clearAll = isClearAll,
            position = position
        )
        val response = createSuccessResponse(
            DeleteSearchHistoryResponse(UniverseDeleteRecentSearch(status = "success"))
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is DeleteSearchHistoryResult.Success)
        assertEquals(position, result.position)
        assertEquals(isClearAll, result.isClearAll)
    }

    @Test
    fun `delete one item then failed`() = runBlocking{
        val position = 0
        val isClearAll = false
        val parameter = DeleteSearchHistoryParam(
            clearAll = isClearAll,
            position = position
        )
        val response = createSuccessResponse(
            DeleteSearchHistoryResponse(UniverseDeleteRecentSearch())
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(position, result.position)
        assertEquals(isClearAll, result.isClearAll)
    }

    @Test
    fun `delete one item with position less than 0 then success`() = runBlocking{
        val position = -1
        val isClearAll = false
        val parameter = DeleteSearchHistoryParam(
            clearAll = isClearAll,
            position = position
        )
        val response = createSuccessResponse(
            DeleteSearchHistoryResponse(UniverseDeleteRecentSearch(status = "success"))
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(0, result.position)
        assertEquals(isClearAll, result.isClearAll)
    }

    @Test
    fun `delete all item then success`() = runBlocking{
        val isClearAll = true
        val parameter = DeleteSearchHistoryParam(
            clearAll = isClearAll
        )
        val response = createSuccessResponse(
            DeleteSearchHistoryResponse(UniverseDeleteRecentSearch(status = "success"))
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is DeleteSearchHistoryResult.Success)
        assertEquals(isClearAll, result.isClearAll)
    }

    @Test
    fun `delete all item then failed`() = runBlocking{
        val isClearAll = false
        val parameter = DeleteSearchHistoryParam(
            clearAll = isClearAll
        )
        val response = createSuccessResponse(
            DeleteSearchHistoryResponse(UniverseDeleteRecentSearch())
        )

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(parameter)
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(isClearAll, result.isClearAll)
    }

}
