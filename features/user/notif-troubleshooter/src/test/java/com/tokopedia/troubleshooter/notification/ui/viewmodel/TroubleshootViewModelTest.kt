package com.tokopedia.troubleshooter.notification.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.troubleshooter.notification.domain.TroubleshootStatusUseCase
import com.tokopedia.troubleshooter.notification.entity.NotifierSendTroubleshooter
import com.tokopedia.troubleshooter.notification.entity.PushNotifCheckerResponse
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
    private val useCase: TroubleshootStatusUseCase = mockk(relaxed = true)

    // observer
    private val troubleshootObservable: Observer<NotifierSendTroubleshooter> = mockk(relaxed = true)

    @Before fun setUp() {
        viewModel = TroubleshootViewModel(useCase, TestDispatcherProvider())
        viewModel.troubleshoot.observeForever(troubleshootObservable)
    }

    @Test fun `it should troubleshoot properly`() {
        val expectedValue = NotifierSendTroubleshooter()

        coEvery {
            useCase.execute(RequestParams.EMPTY)
        } returns PushNotifCheckerResponse()

        viewModel.troubleshoot()

        verify { troubleshootObservable.onChanged(expectedValue) }
        viewModel.troubleshoot isEqualsTo expectedValue
    }

}