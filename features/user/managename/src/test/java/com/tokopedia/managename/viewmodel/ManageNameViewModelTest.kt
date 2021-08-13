package com.tokopedia.managename.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.managename.data.model.UpdateNameModel
import com.tokopedia.managename.domain.AddNameUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 05/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ManageNameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val addNameUseCase = mockk<AddNameUseCase>(relaxed = true)
    val name = "Test Name"

    val dispatcher = CoroutineTestDispatchersProvider
    private var observer = mockk<Observer<com.tokopedia.usecase.coroutines.Result<UpdateNameModel>>>(relaxed = true)
    lateinit var viewModel: ManageNameViewModel


    @Before
    fun setUp() {
        viewModel = ManageNameViewModel(
                addNameUseCase,
                dispatcher
        )
        viewModel.updateNameLiveData.observeForever(observer)
    }

    @Test
    fun `Execute Change Name Function Success`() {
        val name = "Test Name"

        /* When */
        val mockUpdateModel = UpdateNameModel(isSuccess = 1)

        every { addNameUseCase.update(any(), any(), any()) } answers {
            secondArg<(UpdateNameModel) -> Unit>().invoke(mockUpdateModel)
        }
        viewModel.updateName(name)

        /* Then */
        verify { addNameUseCase.update(any(), any(), any()) }
        verify { observer.onChanged(Success(mockUpdateModel)) }
    }

    @Test
    fun `Execute Change Name Function Fail`() {

        /* When */
        val mockError = Throwable()

        every { addNameUseCase.update(any(), any(), any()) } answers {
            thirdArg<(Throwable) -> Unit>().invoke(mockError)
        }
        viewModel.updateName(name)

        /* Then */
        verify { addNameUseCase.update(any(), any(), any()) }
        verify { observer.onChanged(Fail(mockError)) }
    }

}