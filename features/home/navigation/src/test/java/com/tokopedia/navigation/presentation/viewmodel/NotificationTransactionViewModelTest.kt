package com.tokopedia.navigation.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.domain.NotificationFilterUseCase
import com.tokopedia.navigation.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.navigation.domain.pojo.NotificationUpdateFilter
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NotificationTransactionViewModelTest {

    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var filterNotificationUseCase: NotificationFilterUseCase

    @Mock lateinit var observerFilterNotification: Observer<NotificationFilterSectionWrapper>
    @Mock lateinit var observerError: Observer<String>

    @Captor lateinit var captorFilterNotification: ArgumentCaptor<NotificationFilterSectionWrapper>
    @Captor lateinit var captorError: ArgumentCaptor<String>

    lateinit var viewModel: NotificationTransactionViewModel

    private val dispatcherProvider = TestDispatcherProvider()
    private val filterMapper = GetNotificationUpdateFilterMapper()

    var mockFilterNoitification = NotificationUpdateFilter()

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = NotificationTransactionViewModel(
                any(),
                any(),
                any(),
                filterNotificationUseCase,
                filterMapper,
                any(),
                dispatcherProvider
        )
        viewModel.filterNotification.observeForever(observerFilterNotification)
        viewModel.errorMessage.observeForever(observerError)
    }

    @Test fun `should return filter of notification`() = runBlocking {
        `when`(filterNotificationUseCase.get(
                any(),
                onSuccessGetFilterNotification(),
                onErrorMessage()))

        viewModel.getNotificationFilter()

        verify(observerFilterNotification, atLeastOnce())
                .onChanged(captorFilterNotification.capture())

        assertEquals(
                mockFilterNoitification,
                captorFilterNotification.allValues.first())
    }

    private fun onSuccessGetFilterNotification(): (NotificationUpdateFilter) -> Unit {
        return {
            mockFilterNoitification = it
        }
    }

    private fun onErrorMessage(): (Throwable) -> Unit {
        return {
            print(it.message)
        }
    }

    class TestDispatcherProvider: DispatcherProvider {
        override fun ui(): CoroutineDispatcher = Dispatchers.Main
        override fun io(): CoroutineDispatcher = Dispatchers.Unconfined
    }

}