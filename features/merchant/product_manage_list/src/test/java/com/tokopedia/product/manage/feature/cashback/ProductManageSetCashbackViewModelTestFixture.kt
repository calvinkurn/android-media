package com.tokopedia.product.manage.feature.cashback

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.cashback.presentation.viewmodel.ProductManageSetCashbackViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

abstract class ProductManageSetCashbackViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var setCashbackUseCase: SetCashbackUseCase

    protected lateinit var viewModel: ProductManageSetCashbackViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductManageSetCashbackViewModel(setCashbackUseCase, CoroutineTestDispatchersProvider)
    }
}