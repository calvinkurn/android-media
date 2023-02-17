package com.tokopedia.profilecompletion.profilecompletion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.profilecompletion.profilecompletion.viewmodel.ProfileRoleViewModel
import com.tokopedia.profilecompletion.profileinfo.data.ProfileRoleResponse
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProfileRoleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var viewModel: ProfileRoleViewModel

    private var userProfileRoleData = ProfileRoleResponse()
    private var mockThrowable = mockk<Throwable>(relaxed = true)
    private val profileRoleUseCase = mockk<ProfileRoleUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ProfileRoleViewModel(
            profileRoleUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on getUserProfileRole Success`() {

        coEvery { profileRoleUseCase(Unit) } returns userProfileRoleData

        viewModel.getUserProfileRole()

        /* Then */
        val result = viewModel.userProfileRole.getOrAwaitValue()
        assertTrue(result is Success)
        assertEquals(userProfileRoleData.profileRole, result.data)
    }

    @Test
    fun `on another Error happen`() {
        coEvery { profileRoleUseCase(Unit) } throws mockThrowable

        viewModel.getUserProfileRole()

        val result = viewModel.userProfileRole.getOrAwaitValue()
        assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((result as Fail).throwable, mockThrowable)
        coVerify(atLeast = 1){ profileRoleUseCase(Unit) }
    }
}
