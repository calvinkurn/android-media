package com.tokopedia.additional_check.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.additional_check.data.GetObjectPojo
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.domain.usecase.AdditionalCheckUseCase
import com.tokopedia.additional_check.view.TwoFactorViewModel
import com.tokopedia.user.session.UserSessionInterface
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

    val useCase = mockk<AdditionalCheckUseCase>(relaxed = true)
    val userSession = mockk<UserSessionInterface>(relaxed = true)
    val pref = mockk<AdditionalCheckPreference>(relaxed = true)
    val mockThrowable = mockk<Throwable>(relaxed = true)

    val dispatcher = TestCoroutineDispatcher()
    lateinit var viewModel: TwoFactorViewModel

    val onSuccess: (TwoFactorResult) -> Unit = mockk(relaxed = true)
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
        val twoFactorResult = TwoFactorResult(interval = interval)
        val result = GetObjectPojo(twoFactorResult = twoFactorResult)

        every { useCase.getBottomSheetData(any(), any()) } answers {
            firstArg<(GetObjectPojo) -> Unit>().invoke(result)
        }
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess, onError)

        verify {
            pref.setInterval(interval)
            onSuccess.invoke(result.twoFactorResult)
        }
    }

    @Test
    fun `Execute Check Fail`() {

        /* When */
        every { useCase.getBottomSheetData(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        every { userSession.isLoggedIn } returns true
        every { pref.isNeedCheck() } returns true

        viewModel.check(onSuccess, onError)

        verify {
            onError.invoke(mockThrowable)
        }
    }

}