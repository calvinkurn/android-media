package com.tokopedia.additional_check.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.additional_check.data.*
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.RecoverGoogleTinkUseCase
import com.tokopedia.additional_check.domain.usecase.OfferInterruptUseCase
import com.tokopedia.additional_check.domain.usecase.ShowInterruptUseCase
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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
    val offerInterruptUseCase = mockk<OfferInterruptUseCase>(relaxed = true)
    val fingerprintPreference = mockk<FingerprintPreference>(relaxed = true)
    val recoverGoogleTinkUseCase = mockk<RecoverGoogleTinkUseCase>(relaxed = true)

    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val pref = mockk<AdditionalCheckPreference>(relaxed = true)
    val mockThrowable = mockk<Throwable>(relaxed = true)
    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: TwoFactorViewModel

    val onSuccess1: (ShowInterruptData) -> Unit = mockk(relaxed = true)
    val onError: (Throwable) -> Unit = mockk(relaxed = true)

    val onSuccess: (MutableList<OfferingData>) -> Unit = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = TwoFactorViewModel(
            userSession,
            pref,
            useCase,
            offerInterruptUseCase,
            fingerprintPreference,
            recoverGoogleTinkUseCase,
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

        viewModel.check(onSuccess1, onError)

        verify {
            pref.setInterval(interval)
            onSuccess1.invoke(result.data)
        }
    }

    @Test
    fun `Execute Check Success - Link Account Reminder`() {
        val interval = 1
        val linkAccReminder = 2

        /* When */
        val accountLinkReminderData = AccountLinkReminderData(linkAccReminder, true)
        val showInterruptData = ShowInterruptData(
            interval = interval,
            popupType = 0,
            accountLinkReminderData = accountLinkReminderData
        )
        val result = ShowInterruptResponse(showInterruptData)

        coEvery { useCase.invoke(any()) } returns result
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess1, onError)

        verify {
            pref.setInterval(linkAccReminder)
            onSuccess1.invoke(result.data)
        }
    }

    @Test
    fun `Execute Check Fail`() {
        /* When */
        coEvery { useCase.invoke(any()) } throws mockThrowable
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess1, onError)

        verify {
            onError.invoke(mockThrowable)
        }
    }

    @Test
    fun `get offering success - has offers`() {

        val onSuccess: (MutableList<OfferingData>) -> Unit = mockk(relaxed = true)
        val onError: (Throwable) -> Unit = mockk(relaxed = true)

        val data = OfferInterruptData(
            interval = 10000, errorMessages = listOf(""), offers = mutableListOf(
                OfferingData("1"),
                OfferingData("2")
            )
        )
        val mockResponse = OfferInterruptResponse(data)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } returns mockResponse

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            pref.setInterval(10000)
            pref.setNextOffering(any())
            onSuccess.invoke(mockResponse.data.offers)
        }
    }

    @Test
    fun `get offering success - has 1 offer`() {
        val onSuccess: (MutableList<OfferingData>) -> Unit = mockk(relaxed = true)
        val onError: (Throwable) -> Unit = mockk(relaxed = true)

        val data = OfferInterruptData(
            interval = 10000, errorMessages = listOf(""), offers = mutableListOf(
                OfferingData("1")
            )
        )
        val mockResponse = OfferInterruptResponse(data)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } returns mockResponse

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            pref.setInterval(10000)
            onSuccess.invoke(mockResponse.data.offers)
        }
        verify(exactly = 0) { pref.setNextOffering(any()) }
    }

    @Test
    fun `get offering success - has error`() {
        val errMsg = "error"
        val data = OfferInterruptData(
            interval = 10000,
            errorMessages = listOf(errMsg),
            offers = mutableListOf()
        )
        val mockResponse = OfferInterruptResponse(data)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } returns mockResponse

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            pref.setInterval(10000)
            onError.invoke(any())
        }
    }

    @Test
    fun `get offering success - throws error`() {
        val onSuccess: (MutableList<OfferingData>) -> Unit = mockk(relaxed = true)
        val onError: (Throwable) -> Unit = mockk(relaxed = true)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } throws mockThrowable

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            onError.invoke(mockThrowable)
        }
    }

    @Test
    fun `get offering success - error msg is empty`() {
        val data =
            OfferInterruptData(interval = 10000, errorMessages = listOf(), offers = mutableListOf())
        val mockResponse = OfferInterruptResponse(data)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } returns mockResponse

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            pref.setInterval(10000)
            onError.invoke(any())
        }
    }

    @Test
    fun `get offering success - error msg is not empty & first msg is empty`() {
        val data = OfferInterruptData(
            interval = 10000,
            errorMessages = listOf("error"),
            offers = mutableListOf()
        )
        val mockResponse = OfferInterruptResponse(data)

        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true
        coEvery { offerInterruptUseCase(any()) } returns mockResponse

        viewModel.getOffering(true, onSuccess, onError)

        verify {
            pref.setInterval(10000)
            onError.invoke(any())
        }
    }

    @Test
    fun `get offering - is not logged in`() {
        every { userSession.isLoggedIn } returns false
        every { pref.isNeedCheck() } returns true
        viewModel.getOffering(true, onSuccess, onError)
        verify(exactly = 0) {
            onSuccess(any())
            onError(any())
        }
    }

    @Test
    fun `get offering - is need check == false`() {
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns false
        viewModel.getOffering(true, onSuccess, onError)
        verify(exactly = 0) {
            onSuccess(any())
            onError(any())
        }
    }

    @Test
    fun `check - is not logged in`() {
        every { userSession.isLoggedIn } returns false
        every { pref.isNeedCheck() } returns true
        viewModel.check(onSuccess1, onError)
        verify(exactly = 0) {
            onSuccess1(any())
            onError(any())
        }
    }

    @Test
    fun `check - is need check == false`() {
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns false
        viewModel.check(onSuccess1, onError)
        verify(exactly = 0) {
            onSuccess1(any())
            onError(any())
        }
    }

    @Test
    fun `recover test`() {
        viewModel.refreshUserSession { }
        coVerify {
            recoverGoogleTinkUseCase.invoke(Unit)
        }
    }

}
