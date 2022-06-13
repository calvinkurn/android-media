package com.tokopedia.profilecompletion.addbod

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.addbod.data.AddBodData
import com.tokopedia.profilecompletion.addbod.data.UserProfileCompletionUpdateBodData
import com.tokopedia.profilecompletion.addbod.viewmodel.AddBodViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    val bodGraphqlUseCase = mockk<GraphqlUseCase<UserProfileCompletionUpdateBodData>>(relaxed = true)
    val context = mockk<Context>(relaxed = true)

    private var observer = mockk<Observer<Result<AddBodData>>>(relaxed = true)
    lateinit var viewModel: AddBodViewModel

    val mockDate = "05/01/1990"
    val errMsg = "Error"
    val mockThrowable = Throwable(errMsg)

    val messageException = MessageErrorException()

    @Before
    fun setUp() {
        viewModel = AddBodViewModel(
                bodGraphqlUseCase,
                CoroutineTestDispatchersProvider
        )
        viewModel.editBodUserProfileResponse.observeForever(observer)
    }

    @Test
    fun `on Edit DOB executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_BOD to mockDate)

        viewModel.editBodUserProfile(context, mockDate)

        /* Then */
        verify {
            bodGraphqlUseCase.setGraphqlQuery(any<String>())
            bodGraphqlUseCase.setTypeClass(any())
            bodGraphqlUseCase.setRequestParams(mockParam)
            bodGraphqlUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on Success Edit DOB`() {
        /* When */
        val mockBodData = AddBodData(isSuccess = true)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(mockBodData)

        every { bodGraphqlUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileCompletionUpdateBodData) -> Unit>().invoke(mockUpdateModel)
        }

        viewModel.editBodUserProfile(context, mockDate)

        /* Then */
        verify { observer.onChanged(Success(mockBodData)) }
    }

    @Test
    fun `on generic Error Edit DOB`() {

        every { bodGraphqlUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.editBodUserProfile(context, mockDate)

        /* Then */
        verify { observer.onChanged(Fail(mockThrowable)) }
    }

    @Test
    fun `on Error Birth Date Message not empty or blank`() {
        /* When */
        val birthDateMsg = "Test Message"
        val mockBodData = AddBodData(isSuccess = false, birthDateMessage = birthDateMsg)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(mockBodData)

        every { bodGraphqlUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileCompletionUpdateBodData) -> Unit>().invoke(mockUpdateModel)
        }

        viewModel.editBodUserProfile(context, mockDate)

        /* Then */
        assertThat(viewModel.editBodUserProfileResponse.value, instanceOf(Fail::class.java))
        assertEquals(birthDateMsg, (viewModel.editBodUserProfileResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ observer.onChanged(any()) }
    }

    @Test
    fun `on Another Error occurs`() {
        /* When */
        val mockBodData = AddBodData(isSuccess = false)
        val mockUpdateModel = UserProfileCompletionUpdateBodData(mockBodData)

        every { bodGraphqlUseCase.execute(any(), any()) } answers {
            firstArg<(UserProfileCompletionUpdateBodData) -> Unit>().invoke(mockUpdateModel)
        }

        viewModel.editBodUserProfile(context, mockDate)

        /* Then */
        assertThat(viewModel.editBodUserProfileResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.editBodUserProfileResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ observer.onChanged(any()) }
    }
}