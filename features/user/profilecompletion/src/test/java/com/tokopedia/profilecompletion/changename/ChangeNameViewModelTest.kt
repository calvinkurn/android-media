package com.tokopedia.profilecompletion.changename

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.domain.ChangeNameUseCase
import com.tokopedia.profilecompletion.domain.UserProfileRuleUseCase
import com.tokopedia.profilecompletion.profilecompletion.data.ProfileRoleData
import com.tokopedia.profilecompletion.profilecompletion.data.UserProfileRoleData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangeNameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: ChangeNameViewModel

    private val name = "Yoris Prayogo"
    private var changeNamePojo = ChangeNamePojo()
    private var mockThrowable = mockk<Throwable>(relaxed = true)

    private val changeNameUseCase = mockk<ChangeNameUseCase>(relaxed = true)
    private val userProfileRoleUseCase = mockk<UserProfileRuleUseCase>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = ChangeNameViewModel(
            changeNameUseCase,
            userProfileRoleUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on changePublicName Success`() {
        changeNamePojo.data.isSuccess = 1

        coEvery { changeNameUseCase(any()) } returns changeNamePojo

        viewModel.changePublicName(name)

        /* Then */
        val result = viewModel.changeNameResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Success::class.java))
        assertEquals(name, (result as Success<ChangeNameResult>).data.fullName)
    }

    @Test
    fun `on changePublicName Error message not blank`() {
        changeNamePojo.data.errors = mutableListOf("Error")

        coEvery { changeNameUseCase(any()) } returns changeNamePojo

        viewModel.changePublicName(name)

        /* Then */
        val  result = viewModel.changeNameResponse.getOrAwaitValue()
        Assert.assertThat(result, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((result as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        assertEquals(changeNamePojo.data.errors[0], (result as Fail).throwable.message)
        coVerify (atLeast = 1){ changeNameUseCase(any()) }
    }

    @Test
    fun `on changePublicName get Error`() {
        changeNamePojo.data.isSuccess = 1

        coEvery { changeNameUseCase(any()) } throws mockThrowable

        viewModel.changePublicName(name)

        /* Then */
        val result = viewModel.changeNameResponse.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }

    @Test
    fun `on get user profile - is allowed change name`() {
        val mockResponse = UserProfileRoleData(ProfileRoleData(isAllowedChangeName = true, chancesChangeName = "10"))

        coEvery { userProfileRoleUseCase(Unit) } returns mockResponse

        viewModel.getUserProfileRole()

        /* Then */
        val result = viewModel.userProfileRole.getOrAwaitValue()
        assertEquals(Success(mockResponse.profileRoleData), result)
        Assert.assertThat(result, CoreMatchers.instanceOf(Success::class.java))
        assertTrue((result as Success).data.isAllowedChangeName)
        assertTrue((result as Success).data.chancesChangeName.toInt() > 0)
    }

    @Test
    fun `on get user profile - got error`() {
        coEvery { userProfileRoleUseCase(Unit) } throws mockThrowable

        viewModel.getUserProfileRole()

        /* Then */
        val result = viewModel.userProfileRole.getOrAwaitValue()
        assertEquals(Fail(mockThrowable), result)
    }
}
