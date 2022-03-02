package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.SaveAddOnStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseAddOnTest {

    var getAddOnByProductUseCase: GetAddOnByProductUseCase = mockk()
    var getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase = mockk()
    var saveAddOnStateUseCase: SaveAddOnStateUseCase = mockk()

    private var dispatcher: CoroutineDispatchers = CoroutineTestDispatchersProvider
    lateinit var viewModel: AddOnViewModel

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = AddOnViewModel(dispatcher, getAddOnByProductUseCase, getAddOnSavedStateUseCase, saveAddOnStateUseCase)
    }

}