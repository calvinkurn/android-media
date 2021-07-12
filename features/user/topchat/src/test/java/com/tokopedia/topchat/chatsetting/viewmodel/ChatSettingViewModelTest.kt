package com.tokopedia.topchat.chatsetting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingBuyerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingSellerUiModel
import com.tokopedia.topchat.chatsetting.usecase.GetChatSettingUseCase
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import org.junit.Before

import org.junit.Rule
import org.junit.Test

class ChatSettingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var getChatSettingUseCase: GetChatSettingUseCase

    private lateinit var viewModel: ChatSettingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChatSettingViewModel(Dispatchers.Unconfined, getChatSettingUseCase)
    }

    @Test
    fun `isSeller field change properly`() {
        // When
        viewModel.isSeller = true

        // Then
        assertTrue(viewModel.isSeller)
    }

    @Test
    fun `on success get chat setting`() {
        // Given
        val settings = listOf<Visitable<ChatSettingTypeFactory>>(
                ChatSettingBuyerUiModel(),
                ChatSettingSellerUiModel()
        )
        val observer: Observer<Result<List<Visitable<ChatSettingTypeFactory>>>> = mockk(
                relaxed = true
        )
        every { getChatSettingUseCase.get(captureLambda(), any()) } answers {
            val onSuccess = lambda<(List<Visitable<ChatSettingTypeFactory>>) -> Unit>()
            onSuccess.invoke(settings)
        }

        // When
        viewModel.chatSettings.observeForever(observer)
        viewModel.getChatSetting()

        // Then
        verify(exactly = 1) { observer.onChanged(Success(settings)) }
    }

    @Test
    fun `on error get chat setting`() {
        // Given
        val throwable = Throwable()
        val observer: Observer<Result<List<Visitable<ChatSettingTypeFactory>>>> = mockk(
                relaxed = true
        )
        every { getChatSettingUseCase.get(any(), captureLambda()) } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(throwable)
        }

        // When
        viewModel.chatSettings.observeForever(observer)
        viewModel.getChatSetting()

        // Then
        verify(exactly = 1) { observer.onChanged(Fail(throwable)) }
    }

}