package com.tokopedia.tokofood.search.initialstate

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.feature.search.initialstate.domain.model.RemoveSearchHistoryResponse
import com.tokopedia.tokofood.feature.search.initialstate.domain.model.TokoFoodInitSearchStateResponse
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString


class InitialSearchStateViewModelTest : InitialSearchStateViewModelTestFixture() {

    @Test
    fun `when fetch initial state should set live data success`() {
        val jsonResponse = JsonResourcesUtil.createSuccessResponse<TokoFoodInitSearchStateResponse>(
            INITIAL_STATE_SUCCESS
        ).tokofoodInitSearchState

        val initialStateWrapperUiModel = mapper.mapToInitialStateWrapperUiModel(jsonResponse)

        val localCacheModel = LocalCacheModel(address_id = "0")

        coEvery {
            initialStateUseCase.get().execute(localCacheModel)
        } returns initialStateWrapperUiModel

        viewModel.fetchInitialState(localCacheModel)

        coVerify {
            initialStateUseCase.get().execute(localCacheModel)
        }

        val actualResult = (viewModel.initialStateWrapper.value as Success).data
        assertEquals(initialStateWrapperUiModel.initialStateList, actualResult.initialStateList)
        assertTrue(initialStateWrapperUiModel.initialStateList.isNotEmpty())
    }

    @Test
    fun `when fetch initial state should set live data fail`() {
        val errorException = MessageErrorException()

        val localCacheModel = LocalCacheModel(address_id = "0")

        coEvery {
            initialStateUseCase.get().execute(localCacheModel)
        } throws errorException

        viewModel.fetchInitialState(localCacheModel)

        coVerify {
            initialStateUseCase.get().execute(localCacheModel)
        }

        val actualResult = (viewModel.initialStateWrapper.value as Fail).throwable::class.java
        val expectedResult = errorException::class.java
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when remove recent search should set live data success`() {
        val jsonResponse = JsonResourcesUtil.createSuccessResponse<RemoveSearchHistoryResponse>(
            REMOVE_RECENT_SEARCH_SUCCESS
        ).tokofoodRemoveSearchHistory

        val removeSearchUiModel = mapper.mapToRemoveSearchUiModel(jsonResponse)

        coEvery {
            removeSearchHistoryUseCase.get().execute(anyString())
        } returns removeSearchUiModel

        viewModel.removeRemoveRecentSearch(anyString())

        coVerify {
            removeSearchHistoryUseCase.get().execute(anyString())
        }

        val actualResult = (viewModel.removeSearchHistory.value as Success).data
        assertEquals(removeSearchUiModel.message, actualResult.message)
        assertEquals(removeSearchUiModel.isSuccess, actualResult.isSuccess)
    }

    @Test
    fun `when remove recent search should set live data fail`() {
        val errorException = MessageErrorException()

        coEvery {
            removeSearchHistoryUseCase.get().execute(anyString())
        } throws errorException

        viewModel.removeRemoveRecentSearch(anyString())

        coVerify {
            removeSearchHistoryUseCase.get().execute(anyString())
        }

        val actualResult = (viewModel.removeSearchHistory.value as Fail).throwable::class.java
        val expectedResult = errorException::class.java
        assertEquals(expectedResult, actualResult)
    }
}