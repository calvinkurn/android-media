package com.tokopedia.gifting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.gifting.presentation.viewmodel.GiftingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach

open class GiftingBottomsheetViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getAddOnUseCase: GetAddOnUseCase

    val viewModel by lazy {
        spyk(
            GiftingViewModel(
                CoroutineTestDispatchersProvider,
                getAddOnUseCase
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
        Dispatchers.resetMain()
    }
}