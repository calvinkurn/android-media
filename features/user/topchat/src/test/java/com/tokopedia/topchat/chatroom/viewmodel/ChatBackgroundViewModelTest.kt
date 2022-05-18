package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Test

class ChatBackgroundViewModelTest: BaseTopChatViewModelTest() {

    private val emptyCacheTest = ""
    private val valueCacheTest = "background_test"
    private val differentBackgroundTest = "diff_bg_test"

    @Test
    fun should_get_response_when_success_get_background() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } returns emptyCacheTest
        coEvery {
            getChatBackgroundUseCase(Unit)
        } returns flow {
            emit(differentBackgroundTest)
        }

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            differentBackgroundTest,
            (viewModel.chatBackground.value as Success).data
        )
    }

    @Test
    fun should_get_background_from_cache_when_available() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } returns valueCacheTest
        coEvery {
            getChatBackgroundUseCase(Unit)
        } returns flow {
            emit(valueCacheTest)
        }

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            valueCacheTest,
            (viewModel.chatBackground.value as Success).data
        )
    }

    @Test
    fun should_get_background_from_cache_when_available_but_different_image() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } returns valueCacheTest
        coEvery {
            getChatBackgroundUseCase(Unit)
        } returns flow {
            emit(differentBackgroundTest)
        }

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            differentBackgroundTest,
            (viewModel.chatBackground.value as Success).data
        )
    }

    @Test
    fun should_get_response_when_success_get_background_and_error_from_cache() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } throws expectedThrowable
        coEvery {
            getChatBackgroundUseCase(Unit)
        } returns flow {
            emit(differentBackgroundTest)
        }

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            differentBackgroundTest,
            (viewModel.chatBackground.value as Success).data
        )
    }

    @Test
    fun should_get_error_message_when_error_get_background() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } returns emptyCacheTest
        coEvery {
            getChatBackgroundUseCase(Unit)
        } throws expectedThrowable

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatBackground.value as Fail).throwable.message
        )
    }

    @Test
    fun should_get_error_message_when_error_get_background_from_cache_and_gql() {
        //Given
        coEvery {
            cacheManager.loadCache<String>(any(), any())
        } throws expectedThrowable
        coEvery {
            getChatBackgroundUseCase(Unit)
        } throws expectedThrowable

        //When
        viewModel.getBackground()

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.chatBackground.value as Fail).throwable.message
        )
    }
}