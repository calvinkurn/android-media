package com.tokopedia.product.manage.feature.quickedit.stock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import org.junit.Before
import org.junit.Rule

abstract class ProductManageQuickEditStockViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductManageQuickEditStockViewModel

    @Before
    fun setup() {
        viewModel= ProductManageQuickEditStockViewModel()
    }
}