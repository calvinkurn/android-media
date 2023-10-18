package com.tokopedia.addongifting.addonbottomsheet.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addongifting.addonbottomsheet.domain.usecase.GetAddOnSavedStateUseCase
import com.tokopedia.addongifting.addonbottomsheet.view.AddOnViewModel
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class BaseAddOnTest {

    var getAddOnByProductUseCase: GetAddOnByProductUseCase = mockk()
    var getAddOnSavedStateUseCase: GetAddOnSavedStateUseCase = mockk()
    var saveAddOnStateUseCase: SaveAddOnStateUseCase = mockk()

    @get:Rule
    val testCoroutineRule = UnconfinedTestRule()

    private var dispatcher: CoroutineDispatchers = testCoroutineRule.dispatchers
    lateinit var viewModel: AddOnViewModel

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = AddOnViewModel(dispatcher, getAddOnByProductUseCase, getAddOnSavedStateUseCase, saveAddOnStateUseCase)
    }
}
