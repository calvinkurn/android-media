package com.tokopedia.profilecompletion.changename

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNamePojo
import com.tokopedia.profilecompletion.changename.domain.pojo.ChangeNameResult
import com.tokopedia.profilecompletion.changename.viewmodel.ChangeNameViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.profilecompletion.settingprofile.data.ProfileRoleData
import com.tokopedia.profilecompletion.settingprofile.data.UserProfileRoleData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 29/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class ChangeNameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val graphqlUseCase = mockk<GraphqlUseCase<ChangeNamePojo>>(relaxed = true)
    val userProfileRoleUseCase = mockk<GraphqlUseCase<UserProfileRoleData>>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private var observer = mockk<Observer<Result<ChangeNameResult>>>(relaxed = true)
    private var userProfileObserver = mockk<Observer<Result<ProfileRoleData>>>(relaxed = true)
    lateinit var viewModel: ChangeNameViewModel

    private val rawQueries = mapOf(
            ProfileCompletionQueryConstant.MUTATION_CHANGE_NAME to ProfileCompletionQueryConstant.MUTATION_CHANGE_NAME,
            ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE to ProfileCompletionQueryConstant.QUERY_PROFILE_ROLE
    )

    private val name = "Yoris Prayogo"
    private var changeNamePojo = ChangeNamePojo()

    @Before
    fun setUp() {
        viewModel = ChangeNameViewModel(
                graphqlUseCase,
                userProfileRoleUseCase,
                rawQueries,
                testDispatcher
        )
        viewModel.changeNameResponse.observeForever(observer)
        viewModel.userProfileRole.observeForever(userProfileObserver)
    }

    @Test
    fun `on changePublicName executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_NAME to name)

        viewModel.changePublicName(name)

        /* Then */
        verify {
            graphqlUseCase.setGraphqlQuery(any())
            graphqlUseCase.setTypeClass(any())
            graphqlUseCase.setRequestParams(mockParam)
            graphqlUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on changePublicName Success`() {
        changeNamePojo.data.isSuccess = 1

        every { graphqlUseCase.execute(any(), any()) } answers {
            firstArg<(ChangeNamePojo) -> Unit>().invoke(changeNamePojo)
        }

        viewModel.changePublicName(name)

        /* Then */
        verify { observer.onChanged(any()) }
        Assert.assertThat(viewModel.changeNameResponse.value, CoreMatchers.instanceOf(Success::class.java))
        assertEquals(name, (viewModel.changeNameResponse.value as Success<ChangeNameResult>).data.fullName)
    }

    @Test
    fun `on changePublicName Error message not blank`() {
        changeNamePojo.data.errors = mutableListOf("Error")

        every { graphqlUseCase.execute(any(), any()) } answers {
            firstArg<(ChangeNamePojo) -> Unit>().invoke(changeNamePojo)
        }

        viewModel.changePublicName(name)

        /* Then */
        Assert.assertThat(viewModel.changeNameResponse.value, CoreMatchers.instanceOf(Fail::class.java))
        Assert.assertThat((viewModel.changeNameResponse.value as Fail).throwable, CoreMatchers.instanceOf(MessageErrorException::class.java))
        assertEquals(changeNamePojo.data.errors[0], (viewModel.changeNameResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ observer.onChanged(any()) }
    }

    @Test
    fun `on get user profile - is allowed change name`() {
        val mockResponse = UserProfileRoleData(ProfileRoleData(isAllowedChangeName = true, chancesChangeName = "10"))
        every { userProfileRoleUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileRoleData) -> Unit>().invoke(mockResponse)
        }

        viewModel.getUserProfileRole()

        /* Then */
        verify { userProfileObserver.onChanged(any()) }
        Assert.assertThat(viewModel.userProfileRole.value, CoreMatchers.instanceOf(Success::class.java))
        assert((viewModel.userProfileRole.value as Success).data.isAllowedChangeName)
        assert((viewModel.userProfileRole.value as Success).data.chancesChangeName.toInt() > 0)
    }
}