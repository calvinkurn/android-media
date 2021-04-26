package com.tokopedia.common_digital.atc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class DigitalAddToCartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider

    lateinit var digitalAddToCartViewModel: DigitalAddToCartViewModel

    @RelaxedMockK
    lateinit var digitalAddToCartUseCase: DigitalAddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        topupBillsViewModel = TopupBillsViewModel(graphqlRepository, digitalCheckVoucherUseCase, dispatcher)
    }
}