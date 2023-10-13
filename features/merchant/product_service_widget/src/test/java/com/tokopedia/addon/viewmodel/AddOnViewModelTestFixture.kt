package com.tokopedia.addon.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.addon.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addon.presentation.viewmodel.AddOnViewModel
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach

open class AddOnViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAddOnUseCase: GetAddOnByProductUseCase

    @RelaxedMockK
    lateinit var saveAddOnStateUseCase: SaveAddOnStateUseCase

    @RelaxedMockK
    lateinit var getAddOnDetailUseCase: GetAddOnUseCase

    val viewModel by lazy {
        spyk(
            AddOnViewModel(
                CoroutineTestDispatchersProvider,
                getAddOnUseCase,
                saveAddOnStateUseCase,
                getAddOnDetailUseCase
            )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}
