package com.tokopedia.inbox.test

import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetWrapperResponse
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UniversalInboxWidgetAndCounterViewModelTest : UniversalInboxViewModelTestFixture() {

    private val dummyIcon = "99"
    private val dummyWidgetMetaResponse = UniversalInboxWidgetWrapperResponse(
        chatInboxWidgetMeta = UniversalInboxWidgetMetaResponse(
            metaData = arrayListOf(
                UniversalInboxWidgetDataResponse(
                    icon = dummyIcon,
                    type = GOJEK_TYPE
                ),
                UniversalInboxWidgetDataResponse(
                    icon = dummyIcon
                )
            )
        )
    )
    private val dummyCounterResponse = UniversalInboxAllCounterResponse()
    private val dummyWidgetUi = UniversalInboxWidgetMetaUiModel(
        widgetList = arrayListOf(
            UniversalInboxWidgetUiModel(
                icon = dummyIcon.toIntOrZero(),
                type = GOJEK_TYPE
            ),
            UniversalInboxWidgetUiModel(
                icon = dummyIcon.toIntOrZero()
            )
        ),
        isError = false
    )
    private val dummyWidgetUiError = UniversalInboxWidgetMetaUiModel(
        isError = true
    )
    private fun getDummyConversationsChannel(
        expiredAt: Long = System.currentTimeMillis() + 100000
    ) = ConversationsChannel(
        "",
        "",
        "",
        "",
        "",
        0,
        null,
        null,
        listOf(),
        false,
        0,
        expiredAt,
        null,
        null,
        0
    )

    @Test
    fun `should give widget meta and all counter value when success load widget & counter`() {
        runBlocking {
            // Given
            coEvery {
                getAllCounterUseCase(any())
            } returns dummyCounterResponse

            coEvery {
                getWidgetMetaUseCase(Unit)
            } returns dummyWidgetMetaResponse

            every {
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
            } returns dummyWidgetUi

            // When
            viewModel.loadWidgetMetaAndCounter {}

            // Then
            Assert.assertEquals(
                dummyCounterResponse,
                (viewModel.allCounter.value as Success).data
            )
            Assert.assertEquals(
                Pair(dummyWidgetUi, dummyCounterResponse),
                viewModel.widget.value
            )
        }
    }

    @Test
    fun `should give widget meta and all counter value even when meta data is empty`() {
        runBlocking {
            // Given
            coEvery {
                getAllCounterUseCase(any())
            } returns dummyCounterResponse

            coEvery {
                getWidgetMetaUseCase(Unit)
            } returns dummyWidgetMetaResponse

            every {
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
            } returns dummyWidgetUi

            // When
            viewModel.loadWidgetMetaAndCounter {}

            // Then
            Assert.assertEquals(
                dummyCounterResponse,
                (viewModel.allCounter.value as Success).data
            )
            Assert.assertEquals(
                Pair(dummyWidgetUi, dummyCounterResponse),
                viewModel.widget.value
            )
        }
    }

    @Test
    fun `should give counter error only when fail to get counter, but success get widget`() {
        runBlocking {
            // Given
            coEvery {
                getAllCounterUseCase(any())
            } throws dummyThrowable

            coEvery {
                getWidgetMetaUseCase(Unit)
            } returns dummyWidgetMetaResponse

            every {
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
            } returns dummyWidgetUi

            // When
            viewModel.loadWidgetMetaAndCounter {}

            // Then
            Assert.assertEquals(
                dummyThrowable.message,
                (viewModel.allCounter.value as Fail).throwable.message
            )
            Assert.assertEquals(
                Pair(dummyWidgetUi, null),
                viewModel.widget.value
            )
        }
    }

    @Test
    fun `should give widget error only when fail to get widget, but success get counter`() {
        runBlocking {
            // Given
            coEvery {
                getAllCounterUseCase(any())
            } returns dummyCounterResponse

            coEvery {
                getWidgetMetaUseCase(Unit)
            } throws dummyThrowable

            every {
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
            } returns dummyWidgetUiError

            // When
            viewModel.loadWidgetMetaAndCounter {}

            // Then
            Assert.assertEquals(
                dummyCounterResponse,
                (viewModel.allCounter.value as Success).data
            )
            Assert.assertEquals(
                Pair(dummyWidgetUiError, dummyCounterResponse),
                viewModel.widget.value
            )
        }
    }

    @Test
    fun `should give error when fail to map widget & counter`() {
        runBlocking {
            // Given
            coEvery {
                getAllCounterUseCase(any())
            } returns dummyCounterResponse

            coEvery {
                getWidgetMetaUseCase(Unit)
            } returns dummyWidgetMetaResponse

            every {
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
            } throws dummyThrowable

            // When
            viewModel.loadWidgetMetaAndCounter {}

            // Then
            Assert.assertEquals(
                dummyCounterResponse,
                (viewModel.allCounter.value as Success).data
            )
            Assert.assertEquals(
                dummyWidgetUiError.isError,
                viewModel.widget.value?.first?.isError
            )
        }
    }

    @Test
    fun should_not_call_get_driver_counter_use_case_when_live_data_is_not_null() {
        // Given
        viewModel._allChannelsLiveData = MutableLiveData()

        // When
        viewModel.setAllDriverChannels()

        // Then
        verify(exactly = 0) {
            getDriverChatCounterUseCase.getAllChannels()
        }
    }

    @Test
    fun should_do_nothing_when_get_all_channels_returns_null() {
        // Given
        every {
            getDriverChatCounterUseCase.getAllChannels()
        } returns null

        // When
        viewModel.setAllDriverChannels()

        // Then
        verify(exactly = 1) {
            getDriverChatCounterUseCase.getAllChannels()
        }
        Assert.assertEquals(
            null,
            viewModel.driverChatCounter?.value
        )
    }

    @Test
    fun should_give_error_when_failed_to_get_all_channels() {
        // Given
        every {
            getDriverChatCounterUseCase.getAllChannels()
        } throws dummyThrowable

        // When
        viewModel.setAllDriverChannels()

        // Then
        Assert.assertEquals(
            dummyThrowable.message,
            viewModel.error.value?.first?.message
        )
        Assert.assertEquals(
            dummyThrowable.message,
            (viewModel.driverChatCounter?.value as Fail).throwable.message
        )
    }

    @Test
    fun a() {
        // When
        val result = viewModel.getDriverUnreadCount(
            listOf(
                getDummyConversationsChannel(),
                getDummyConversationsChannel(0)
            )
        )

        // Then
        Assert.assertEquals(
            1,
            (result.value as Success).data.first
        )
        Assert.assertEquals(
            0,
            (result.value as Success).data.second
        )
    }

    @Test
    fun b() {
        // Given
        val dummy = ConversationsChannel(
            "", "", "", "", "",
            Int.MAX_VALUE, null, null, listOf(), false,
            0, System.currentTimeMillis() + 100000,
            null, null, 0
        )
        val dummy2 = ConversationsChannel(
            "", "", "", "", "",
            1, null, null, listOf(), false,
            0, System.currentTimeMillis() + 100000,
            null, null, 0
        )

        // When
        val result = viewModel.getDriverUnreadCount(
            listOf(
                dummy,
                dummy2
            )
        )

        // Then
    }
}
