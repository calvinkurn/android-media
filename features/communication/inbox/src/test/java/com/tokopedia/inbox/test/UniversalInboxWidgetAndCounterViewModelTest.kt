package com.tokopedia.inbox.test

import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetMetaResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetWrapperResponse
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UniversalInboxWidgetAndCounterViewModelTest : UniversalInboxViewModelTestFixture() {

    private val dummyIcon = "99"
    private val dummyWidgetMetaResponse = UniversalInboxWidgetWrapperResponse(
        chatInboxWidgetMeta = UniversalInboxWidgetMetaResponse(
            metaData = arrayListOf(
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
                icon = dummyIcon.toIntOrZero()
            )
        ),
        isError = false
    )
    private val dummyWidgetUiError = UniversalInboxWidgetMetaUiModel(
        isError = true
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
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any())
            } returns dummyWidgetUi

            // When
            viewModel.loadWidgetMetaAndCounter()

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
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any())
            } returns dummyWidgetUi

            // When
            viewModel.loadWidgetMetaAndCounter()

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
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any())
            } returns dummyWidgetUiError

            // When
            viewModel.loadWidgetMetaAndCounter()

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
                inboxMenuMapper.mapWidgetMetaToUiModel(any(), any())
            } throws dummyThrowable

            // When
            viewModel.loadWidgetMetaAndCounter()

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
}
