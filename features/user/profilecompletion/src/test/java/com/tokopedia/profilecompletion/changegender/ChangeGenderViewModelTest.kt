package com.tokopedia.profilecompletion.changegender

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.profilecompletion.domain.ChangeGenderUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangeGenderViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: ChangeGenderViewModel

    val gender = 1
    private var changeGenderPojo = ChangeGenderPojo()

    private val changeGenderUseCase = mockk<ChangeGenderUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChangeGenderViewModel(
            changeGenderUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on mutateChangeGender Success`() {
        changeGenderPojo.data.isSuccess = true

        coEvery { changeGenderUseCase(any()) } returns changeGenderPojo

        viewModel.mutateChangeGender(gender)

        /* Then */
        val result = viewModel.mutateChangeGenderResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Success::class.java))
        assertEquals(gender, (result as Success<ChangeGenderResult>).data.selectedGender)
    }

    @Test
    fun `on mutateChangeGender get error`() {
        changeGenderPojo.data.isSuccess = false

        val mockThrowable = mockk<Throwable>(relaxed = true)
        coEvery { changeGenderUseCase(any()) } throws mockThrowable

        viewModel.mutateChangeGender(gender)

        /* Then */
        val result = viewModel.mutateChangeGenderResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on mutateChangeGender Error message not blank`() {
        changeGenderPojo.data.isSuccess = false
        changeGenderPojo.data.errorMessage = "Error"

        coEvery { changeGenderUseCase(any()) } returns changeGenderPojo

        viewModel.mutateChangeGender(gender)

        /* Then */
        val result = viewModel.mutateChangeGenderResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(MessageErrorException::class.java))
        assertEquals(changeGenderPojo.data.errorMessage, (result as Fail).throwable.message)
        coVerify (atLeast = 1){ changeGenderUseCase(any()) }
    }

    @Test
    fun `on another Error happen`() {
        changeGenderPojo.data.isSuccess = false

        coEvery { changeGenderUseCase(any()) } returns changeGenderPojo

        viewModel.mutateChangeGender(gender)

        /* Then */
        val result = viewModel.mutateChangeGenderResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
        coVerify(atLeast = 1){ changeGenderUseCase(any()) }
    }

}
