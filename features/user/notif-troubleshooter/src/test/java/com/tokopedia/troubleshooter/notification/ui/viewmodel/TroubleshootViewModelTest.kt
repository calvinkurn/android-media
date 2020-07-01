package com.tokopedia.troubleshooter.notification.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.domain.UpdateTokenUseCase
import com.tokopedia.troubleshooter.notification.entity.NotificationSendTroubleshoot
import com.tokopedia.troubleshooter.notification.entity.NotificationTroubleshoot
import com.tokopedia.troubleshooter.notification.util.TestDispatcherProvider
import com.tokopedia.troubleshooter.notification.util.isEqualsTo
import com.tokopedia.usecase.RequestParams
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TroubleshootViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TroubleshootViewModel
    private val troubleshoot: TroubleshootStatusUseCase = mockk(relaxed = true)
    private val updateToken: UpdateTokenUseCase = mockk(relaxed = true)

    // observer
    private val troubleshootObservable: Observer<NotificationSendTroubleshoot> = mockk(relaxed = true)

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(troubleshoot, updateToken, TestDispatcherProvider())
        viewModel.troubleshoot.observeForever(troubleshootObservable)
    }

    @Test fun `it should troubleshoot properly`() {
        val expectedValue = NotificationSendTroubleshoot()

        coEvery {
            troubleshoot.execute(RequestParams.EMPTY)
        } returns NotificationTroubleshoot()

        viewModel.troubleshoot()

        verify { troubleshootObservable.onChanged(expectedValue) }
        viewModel.troubleshoot isEqualsTo expectedValue
    }

}