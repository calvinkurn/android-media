package com.tokopedia.product.manage.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.filter.presentation.viewmodel.ProductManageFilterViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductManageFilterViewModelTest {

    @RelaxedMockK
    lateinit var getProductListMetaUseCase: GetProductListMetaUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ProductManageFilterViewModel(getProductListMetaUseCase, dispatchers)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getProductListMetaData should execute expected use case`() {
        mockkObject(getProductListMetaUseCase)
        coEvery {
            getProductListMetaUseCase.executeOnBackground()
        }
        viewModel.getProductListMetaData(0)
        coVerify {
            getProductListMetaUseCase.executeOnBackground()
        }
    }
}