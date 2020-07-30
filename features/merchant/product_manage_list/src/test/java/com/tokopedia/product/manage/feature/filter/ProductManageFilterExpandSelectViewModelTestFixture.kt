package com.tokopedia.product.manage.feature.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandSelectViewModel
import org.junit.Before
import org.junit.Rule

abstract class ProductManageFilterExpandSelectViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductManageFilterExpandSelectViewModel

    @Before
    fun setup() {
        viewModel = ProductManageFilterExpandSelectViewModel()
    }
}