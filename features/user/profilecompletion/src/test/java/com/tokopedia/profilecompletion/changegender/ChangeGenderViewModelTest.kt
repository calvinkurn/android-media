package com.tokopedia.profilecompletion.changegender

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderPojo
import com.tokopedia.profilecompletion.changegender.data.ChangeGenderResult
import com.tokopedia.profilecompletion.changegender.viewmodel.ChangeGenderViewModel
import com.tokopedia.profilecompletion.data.ProfileCompletionQueryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.TestCoroutineDispatcher
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

    val graphqlUseCase = mockk<GraphqlUseCase<ChangeGenderPojo>>(relaxed = true)
    val context = mockk<Context>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()

    private var observer = mockk<Observer<Result<ChangeGenderResult>>>(relaxed = true)
    lateinit var viewModel: ChangeGenderViewModel

    val gender = 1
    private var changeGenderPojo = ChangeGenderPojo()

    @Before
    fun setUp() {
        viewModel = ChangeGenderViewModel(
                graphqlUseCase,
                testDispatcher
        )
        viewModel.mutateChangeGenderResponse.observeForever(observer)
    }

    @Test
    fun `on mutateChangeGender executed`() {
        val mockParam = mapOf(ProfileCompletionQueryConstant.PARAM_GENDER to gender)

        viewModel.mutateChangeGender(context, gender)

        /* Then */
        verify {
            graphqlUseCase.setGraphqlQuery(any())
            graphqlUseCase.setTypeClass(any())
            graphqlUseCase.setRequestParams(mockParam)
            graphqlUseCase.execute(any(), any())
        }
    }

    @Test
    fun `on mutateChangeGender Success`() {
        changeGenderPojo.data.isSuccess = true

        every { graphqlUseCase.execute(any(), any()) } answers {
            firstArg<(ChangeGenderPojo) -> Unit>().invoke(changeGenderPojo)
        }

        viewModel.mutateChangeGender(context, gender)

        /* Then */
        verify { observer.onChanged(any()) }
        assertThat(viewModel.mutateChangeGenderResponse.value, instanceOf(Success::class.java))
        assertEquals(gender, (viewModel.mutateChangeGenderResponse.value as Success<ChangeGenderResult>).data.selectedGender)
    }

    @Test
    fun `on mutateChangeGender Error message not blank`() {
        changeGenderPojo.data.isSuccess = false
        changeGenderPojo.data.errorMessage = "Error"

        every { graphqlUseCase.execute(any(), any()) } answers {
            firstArg<(ChangeGenderPojo) -> Unit>().invoke(changeGenderPojo)
        }

        viewModel.mutateChangeGender(context, gender)

        /* Then */
        assertThat(viewModel.mutateChangeGenderResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.mutateChangeGenderResponse.value as Fail).throwable, instanceOf(MessageErrorException::class.java))
        assertEquals(changeGenderPojo.data.errorMessage, (viewModel.mutateChangeGenderResponse.value as Fail).throwable.message)
        verify(atLeast = 1){ observer.onChanged(any()) }
    }

    @Test
    fun `on another Error happen`() {
        changeGenderPojo.data.isSuccess = false

        every { graphqlUseCase.execute(any(), any()) } answers {
            firstArg<(ChangeGenderPojo) -> Unit>().invoke(changeGenderPojo)
        }

        viewModel.mutateChangeGender(context, gender)

        /* Then */
        assertThat(viewModel.mutateChangeGenderResponse.value, instanceOf(Fail::class.java))
        assertThat((viewModel.mutateChangeGenderResponse.value as Fail).throwable, instanceOf(RuntimeException::class.java))
        verify(atLeast = 1){ observer.onChanged(any()) }
    }

}