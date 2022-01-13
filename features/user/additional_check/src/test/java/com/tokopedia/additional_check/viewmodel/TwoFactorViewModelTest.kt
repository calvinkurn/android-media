package com.tokopedia.additional_check.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.additional_check.data.AccountLinkReminderData
import com.tokopedia.additional_check.data.ShowInterruptData
import com.tokopedia.additional_check.data.ShowInterruptResponse
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.ShowInterruptUseCase
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 14/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TwoFactorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val useCase = mockk<ShowInterruptUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val pref = mockk<AdditionalCheckPreference>(relaxed = true)
    val mockThrowable = mockk<Throwable>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: TwoFactorViewModel

    val onSuccess: (ShowInterruptData) -> Unit = mockk(relaxed = true)
    val onError: (Throwable) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TwoFactorViewModel(
                userSession,
                pref,
                useCase,
                dispatcher
        )
    }

    @Test
    fun `Execute Check Success`() {
        val interval = 10

        /* When */
        val showInterruptData = ShowInterruptData(interval = interval, popupType = 1)
        val result = ShowInterruptResponse(showInterruptData)

        coEvery { useCase.invoke(any()) } returns result
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess, onError)

        verify {
            pref.setInterval(interval)
            onSuccess.invoke(result.data)
        }
    }

    @Test
    fun `Execute Check Success - Link Account Reminder`() {
        val interval = 1
        val linkAccReminder = 2

        /* When */
        val accountLinkReminderData = AccountLinkReminderData(linkAccReminder, true)
        val showInterruptData = ShowInterruptData(interval = interval, popupType = 0, accountLinkReminderData = accountLinkReminderData)
        val result = ShowInterruptResponse(showInterruptData)

        coEvery { useCase.invoke(any()) } returns result
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess, onError)

        verify {
            pref.setInterval(linkAccReminder)
            onSuccess.invoke(result.data)
        }
    }

    @Test
    fun `Execute Check Fail`() {
        /* When */
        coEvery { useCase.invoke(any()) } throws mockThrowable
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess, onError)

        verify {
            onError.invoke(mockThrowable)
        }
    }

}