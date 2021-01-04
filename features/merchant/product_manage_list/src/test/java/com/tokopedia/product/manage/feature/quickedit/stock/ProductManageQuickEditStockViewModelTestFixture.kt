package com.tokopedia.product.manage.feature.quickedit.stock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel.ProductManageQuickEditStockViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class ProductManageQuickEditStockViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userSession: UserSessionInterface
    protected lateinit var viewModel: ProductManageQuickEditStockViewModel

    @Before
    fun setup() {
        userSession = mockk(relaxed = true)
        viewModel = ProductManageQuickEditStockViewModel(userSession)
    }
}