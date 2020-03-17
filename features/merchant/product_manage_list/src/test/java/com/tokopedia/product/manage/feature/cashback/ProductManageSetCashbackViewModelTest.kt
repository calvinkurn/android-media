package com.tokopedia.product.manage.feature.cashback

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.cashback.presentation.viewmodel.ProductManageSetCashbackViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductManageSetCashbackViewModelTest {

    @RelaxedMockK
    lateinit var setCashbackUseCase: SetCashbackUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ProductManageSetCashbackViewModel(setCashbackUseCase, dispatcher)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `setCashback should execute setCashback use case`() {
        mockkObject(setCashbackUseCase)
        coEvery {
            setCashbackUseCase.executeOnBackground()
        }
        val productId = "0"
        val cashback = 0
        val productName = "Amazing Product"
        viewModel.setCashback(productId, productName, cashback)
        coVerify {
            setCashbackUseCase.executeOnBackground()
        }
    }
}