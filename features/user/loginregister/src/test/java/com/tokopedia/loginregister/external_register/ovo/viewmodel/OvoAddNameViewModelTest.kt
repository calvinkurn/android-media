package com.tokopedia.loginregister.external_register.ovo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.data.ExternalRegisterPreference
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoData
import com.tokopedia.loginregister.external_register.ovo.data.ActivateOvoResponse
import com.tokopedia.loginregister.external_register.ovo.domain.usecase.ActivateOvoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris Prayogo on 28/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

class OvoAddNameViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val activateOvoUseCase = mockk<ActivateOvoUseCase>(relaxed = true)
    private val externalRegisterPreference = mockk<ExternalRegisterPreference>(relaxed = true)

    private var activateOvoResponse = mockk<Observer<Result<ActivateOvoResponse>>>(relaxed = true)

    lateinit var viewModel: OvoAddNameViewModel

    @Before
    fun setUp() {
        viewModel = OvoAddNameViewModel(
                activateOvoUseCase,
                externalRegisterPreference,
                CoroutineTestDispatchersProvider
        )
        viewModel.activateOvoResponse.observeForever(activateOvoResponse)
    }

    private val testPhone = "082242454511"
    private val testName = "Yoris"

    @Test
    fun `on activate OVO success`() {
        val activateOvoData = ActivateOvoData(activationUrl = "https://tokopedia.com", goalKey = "testGoalKey")
        val testResponse = ActivateOvoResponse(activateOvoData = activateOvoData)

        coEvery { activateOvoUseCase.executeOnBackground() } returns testResponse
        viewModel.activateOvo(phoneNumber = testPhone, name = testName)

        verify {
            activateOvoUseCase.setParams(testPhone, testName, ExternalRegisterConstants.KEY.CLIENT_ID)
            externalRegisterPreference.saveGoalKey(activateOvoData.goalKey)
            externalRegisterPreference.saveName(testName)
            externalRegisterPreference.savePhone(testPhone)
            activateOvoResponse.onChanged(Success(testResponse))
        }
    }

    @Test
    fun `on activate OVO fail`() {
        val throwable = Throwable("Error")

        coEvery { activateOvoUseCase.executeOnBackground() } throws throwable
        viewModel.activateOvo(phoneNumber = testPhone, name = testName)

        verify {
            activateOvoResponse.onChanged(Fail(throwable))
        }
    }
}