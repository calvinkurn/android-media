// package com.tokopedia.inbox.test
//
// import androidx.lifecycle.liveData
// import com.gojek.conversations.channel.ConversationsChannel
// import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
// import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
// import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxMenuAndWidgetMetaResponse
// import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
// import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
// import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
// import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
// import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
// import com.tokopedia.kotlin.extensions.view.toIntOrZero
// import com.tokopedia.usecase.coroutines.Fail
// import com.tokopedia.usecase.coroutines.Success
// import io.mockk.coEvery
// import io.mockk.every
// import io.mockk.verify
// import kotlinx.coroutines.runBlocking
// import kotlinx.coroutines.test.runTest
// import org.junit.Assert
// import org.junit.Test
//
// class UniversalInboxWidgetAndCounterViewModelTest : UniversalInboxViewModelTestFixture() {
//
//    private val dummyIcon = "99"
//    private val dummyWidgetMetaResponse = UniversalInboxWrapperResponse(
//        chatInboxMenu = UniversalInboxMenuAndWidgetMetaResponse(
//            widgetMenu = arrayListOf(
//                UniversalInboxWidgetDataResponse(
//                    icon = dummyIcon,
//                    type = GOJEK_TYPE
//                ),
//                UniversalInboxWidgetDataResponse(
//                    icon = dummyIcon
//                )
//            )
//        )
//    )
//    private val dummyCounterResponse = UniversalInboxAllCounterResponse()
//    private val dummyWidgetUi = UniversalInboxWidgetMetaUiModel(
//        widgetList = arrayListOf(
//            UniversalInboxWidgetUiModel(
//                icon = dummyIcon.toIntOrZero(),
//                type = GOJEK_TYPE
//            ),
//            UniversalInboxWidgetUiModel(
//                icon = dummyIcon.toIntOrZero()
//            )
//        ),
//        isError = false
//    )
//    private val dummyWidgetUiError = UniversalInboxWidgetMetaUiModel(
//        isError = true
//    )
//
//    @Test
//    fun `should give widget error only when fail to get widget, but success get counter`() {
//        runBlocking {
//            // Given
//            coEvery {
//                getAllCounterUseCase(any())
//            } returns dummyCounterResponse
//
//            coEvery {
//                getWidgetMetaUseCase(Unit)
//            } throws dummyThrowable
//
//            every {
//                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
//            } returns dummyWidgetUiError
//
//            // When
//            viewModel.loadWidgetMetaAndCounter()
//
//            // Then
//            Assert.assertEquals(
//                dummyCounterResponse,
//                (viewModel.allCounter.value as Success).data
//            )
//            Assert.assertEquals(
//                Pair(dummyWidgetUiError, dummyCounterResponse),
//                viewModel.widget.value
//            )
//        }
//    }
//
//    @Test
//    fun `should give error when fail to map widget & counter`() {
//        runBlocking {
//            // Given
//            coEvery {
//                getAllCounterUseCase(any())
//            } returns dummyCounterResponse
//
//            coEvery {
//                getWidgetMetaUseCase(Unit)
//            } returns dummyWidgetMetaResponse
//
//            every {
//                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any(), any())
//            } throws dummyThrowable
//
//            // When
//            viewModel.loadWidgetMetaAndCounter()
//
//            // Then
//            Assert.assertEquals(
//                dummyCounterResponse,
//                (viewModel.allCounter.value as Success).data
//            )
//            Assert.assertEquals(
//                dummyWidgetUiError.isError,
//                viewModel.widget.value?.first?.isError
//            )
//        }
//    }
//
//    @Test
//    fun should_give_driver_unread_pair_when_get_all_channels() {
//        // Given
//        val dummy = ConversationsChannel(
//            "", "", "", "", "",
//            1, null, null, listOf(), false,
//            0, System.currentTimeMillis() + 100000,
//            null, null, 0
//        )
//        every {
//            getDriverChatCounterUseCase.getAllChannels()
//        } returns liveData {
//            this.emit(listOf(dummy))
//        }
//
//        // When
//        viewModel.setAllDriverChannels()
//
//        // Then
//        viewModel.driverChatCounter.observe({ lifecycle }) {
//            // Then
//            Assert.assertEquals(
//                1,
//                (it as Success).data.first
//            )
//            Assert.assertEquals(
//                dummy.unreadCount,
//                it.data.second
//            )
//        }
//    }
//
//    @Test
//    fun should_give_empty_pair_when_get_all_channels_give_empty_list() {
//        // Given
//        every {
//            getDriverChatCounterUseCase.getAllChannels()
//        } returns liveData {
//            this.emit(listOf())
//        }
//
//        // When
//        viewModel.setAllDriverChannels()
//
//        // Then
//        viewModel.driverChatCounter.observe({ lifecycle }) {
//            // Then
//            Assert.assertEquals(
//                0,
//                (it as Success).data.first
//            )
//            Assert.assertEquals(
//                0,
//                it.data.second
//            )
//        }
//    }
//
//    @Test
//    fun should_give_error_when_fail_to_get_all_channels() {
//        // Given
//        val dummy = ConversationsChannel(
//            "", "", "", "", "",
//            Int.MAX_VALUE, null, null, listOf(), false,
//            0, System.currentTimeMillis() + 100000,
//            null, null, 0
//        )
//        val dummy2 = ConversationsChannel(
//            "", "", "", "", "",
//            1, null, null, listOf(), false,
//            0, System.currentTimeMillis() + 100000,
//            null, null, 0
//        )
//        every {
//            getDriverChatCounterUseCase.getAllChannels()
//        } returns liveData {
//            this.emit(listOf(dummy, dummy2))
//        }
//
//        // When
//        viewModel.setAllDriverChannels()
//
//        // Then
//        viewModel.driverChatCounter.observe({ lifecycle }) {
//            // Then
//            Assert.assertEquals(
//                IllegalArgumentException().message,
//                (it as Fail).throwable.message
//            )
//            Assert.assertEquals(
//                IllegalArgumentException().message,
//                viewModel.error.value?.first?.message
//            )
//        }
//    }
//
//    @Test
//    fun should_do_nothing_when_get_all_channels_returns_null() {
//        // Given
//        every {
//            getDriverChatCounterUseCase.getAllChannels()
//        } returns null
//
//        // When
//        viewModel.setAllDriverChannels()
//
//        // Then
//        verify(exactly = 1) {
//            getDriverChatCounterUseCase.getAllChannels()
//        }
//        Assert.assertEquals(
//            null,
//            viewModel.driverChatCounter.value
//        )
//    }
//
//    @Test
//    fun should_give_error_when_failed_to_get_all_channels() {
//        // Given
//        every {
//            getDriverChatCounterUseCase.getAllChannels()
//        } throws dummyThrowable
//
//        // When
//        viewModel.setAllDriverChannels()
//
//        // Then
//        Assert.assertEquals(
//            dummyThrowable.message,
//            viewModel.error.value?.first?.message
//        )
//        viewModel.driverChatCounter.observe({ lifecycle }) {
//            Assert.assertEquals(
//                dummyThrowable.message,
//                (viewModel.driverChatCounter.value as Fail).throwable.message
//            )
//        }
//    }
//
//    @Test
//    fun should_give_driver_unread_count() {
//        // When
//        val result = viewModel.getDriverUnreadCount(
//            listOf(
//                getDummyConversationsChannel(),
//                getDummyConversationsChannel(0)
//            )
//        )
//
//        // Then
//        Assert.assertEquals(
//            1,
//            (result.value as Success).data.first
//        )
//        Assert.assertEquals(
//            0,
//            (result.value as Success).data.second
//        )
//    }
//
//    @Test
//    fun should_give_error_when_fail_to_get_driver_unread_count() {
//        // Given
//        val dummy = ConversationsChannel(
//            "", "", "", "", "",
//            Int.MAX_VALUE, null, null, listOf(), false,
//            0, System.currentTimeMillis() + 100000,
//            null, null, 0
//        )
//        val dummy2 = ConversationsChannel(
//            "", "", "", "", "",
//            1, null, null, listOf(), false,
//            0, System.currentTimeMillis() + 100000,
//            null, null, 0
//        )
//
//        // When
//        viewModel.getDriverUnreadCount(
//            listOf(dummy, dummy2)
//        )
//
//        // Then
//        Assert.assertEquals(
//            IllegalArgumentException().message,
//            viewModel.error.value?.first?.message
//        )
//    }
// }
