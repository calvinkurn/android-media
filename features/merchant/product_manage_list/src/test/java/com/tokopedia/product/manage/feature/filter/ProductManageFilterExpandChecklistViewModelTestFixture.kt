package com.tokopedia.product.manage.feature.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandChecklistViewModel
import org.junit.Before
import org.junit.Rule

abstract class ProductManageFilterExpandChecklistViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductManageFilterExpandChecklistViewModel

    @Before
    fun setup() {
        viewModel = ProductManageFilterExpandChecklistViewModel()
    }
}