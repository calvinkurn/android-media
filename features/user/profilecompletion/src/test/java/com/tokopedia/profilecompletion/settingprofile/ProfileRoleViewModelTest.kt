package com.tokopedia.profilecompletion.settingprofile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.settingprofile.data.ProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.viewmodel.ProfileRoleViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ProfileRoleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val userProfileRoleUseCase = mockk<GraphqlUseCase<UserProfileRoleData>>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private var observer = mockk<Observer<Result<ProfileRoleData>>>(relaxed = true)
    lateinit var viewModel: ProfileRoleViewModel

    private var userProfileRoleData = UserProfileRoleData()
    private var mockThrowable = mockk<Throwable>(relaxed = true)

    private val rawQueries = mapOf(
            ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE to ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE
    )

    @Before
    fun setUp() {
        viewModel = ProfileRoleViewModel(
                userProfileRoleUseCase,
                rawQueries,
                testDispatcher
        )
        viewModel.userProfileRole.observeForever(observer)
    }

    @Test
    fun `on getUserProfileRole executed`() {

        viewModel.getUserProfileRole()

        /* Then */
        verify {
            userProfileRoleUseCase.setGraphqlQuery(any<String>())
            userProfileRoleUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on getUserProfileRole Success`() {

        every { userProfileRoleUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileRoleData) -> Unit>().invoke(userProfileRoleData)
        }

        viewModel.getUserProfileRole()

        /* Then */
        verify { observer.onChanged(any()) }
        assertThat(viewModel.userProfileRole.value, CoreMatchers.instanceOf(Success::class.java))
        assertThat((viewModel.userProfileRole.value as Success<UserProfileRoleData>).data, CoreMatchers.instanceOf(ProfileRoleData::class.java))
    }

    @Test
    fun `on another Error happen`() {
        every { userProfileRoleUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getUserProfileRole()

        assertThat(viewModel.userProfileRole.value, CoreMatchers.instanceOf(Fail::class.java))
        assertEquals((viewModel.userProfileRole.value as Fail).throwable, mockThrowable)
        verify(atLeast = 1){ observer.onChanged(any()) }
    }
}