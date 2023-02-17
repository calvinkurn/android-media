package com.tokopedia.profilecompletion.addbod

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.domain.AddBodUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 26/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddBodViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: AddBodViewModel

    val mockDate = "05/01/1990"
    val errMsg = "Error"
    val mockThrowable = Throwable(errMsg)

    private val addBodUseCase = mockk<AddBodUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = AddBodViewModel(
                addBodUseCase,
                CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on Success Edit DOB`() {
        /* When */
        val mockBodData = AddBodData(isSuccess = true)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(addBodData = mockBodData)

        coEvery { addBodUseCase(any()) } returns mockUpdateModel

        viewModel.editBodUserProfile(mockDate)

        /* Then */
        val result = viewModel.editBodUserProfileResponse.getOrAwaitValue()
        assertEquals(Success(mockBodData), result)
    }

    @Test
    fun `on generic Error Edit DOB`() {

        coEvery { addBodUseCase(any()) } throws mockThrowable

        viewModel.editBodUserProfile(mockDate)

        /* Then */
        val result = viewModel.editBodUserProfileResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on Error Birth Date Message not empty or blank`() {
        /* When */
        val birthDateMsg = "Test Message"
        val mockBodData = AddBodData(isSuccess = false, birthDateMessage = birthDateMsg)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(mockBodData)

        coEvery { addBodUseCase(any()) } returns mockUpdateModel

        viewModel.editBodUserProfile(mockDate)

        /* Then */
        val result = viewModel.editBodUserProfileResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertEquals(birthDateMsg, (result as Fail).throwable.message)
    }

    @Test
    fun `on Another Error occurs`() {
        /* When */
        val mockBodData = AddBodData(isSuccess = false)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(mockBodData)

        coEvery { addBodUseCase(any()) } returns mockUpdateModel

        viewModel.editBodUserProfile(mockDate)

        /* Then */
        val result = viewModel.editBodUserProfileResponse.getOrAwaitValue()
        assertThat(result, instanceOf(Fail::class.java))
        assertThat((result as Fail).throwable, instanceOf(RuntimeException::class.java))
    }
}
