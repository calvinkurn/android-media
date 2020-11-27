package com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

abstract class QuickEditVariantViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: QuickEditVariantViewModel
    protected lateinit var getProductVariantUseCase: GetProductVariantUseCase

    @Before
    fun setUp() {
        getProductVariantUseCase = mockk(relaxed = true)
        viewModel = QuickEditVariantViewModel(
                getProductVariantUseCase,
                TestCoroutineDispatchers
        )
    }
}