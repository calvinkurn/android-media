package com.tokopedia.product.manage.feature.etalase.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

open class EtalasePickerViewModelTestFixture {

    protected lateinit var viewModel: EtalasePickerViewModel
    protected lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getShopEtalaseByShopUseCase = mockk(relaxed = true)
        viewModel = EtalasePickerViewModel(getShopEtalaseByShopUseCase, CoroutineTestDispatchersProvider)
    }
}