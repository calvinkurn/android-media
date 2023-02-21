package com.tokopedia.privacycenter.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.DataItem
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.data.SearchHistoryResponse
import com.tokopedia.privacycenter.data.UniverseInitialState
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

class SearchHistoryUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: SearchHistoryUseCase
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val repository = mockk<GraphqlRepository>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = SearchHistoryUseCase(
            repository,
            userSession,
            dispatcher
        )
    }

    @Test
    fun `get recent search then return empty list`() = runBlocking {
        val response = createSuccessResponse(SearchHistoryResponse(
        ))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun `get recent search then return list of search`() = runBlocking {
        val recentSearch = UniverseInitialState(
            data = listOf(DataItem(id = "recent_search",
                items = listOf(ItemSearch(), ItemSearch(), ItemSearch()))
            )
        )
        val response = createSuccessResponse(SearchHistoryResponse(recentSearch))

        coEvery { repository.response(any(), any()) } returns response

        val result = useCase(Unit)
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data.isNotEmpty())
        assertEquals(recentSearch.data.first().items, result.data)
    }

}
