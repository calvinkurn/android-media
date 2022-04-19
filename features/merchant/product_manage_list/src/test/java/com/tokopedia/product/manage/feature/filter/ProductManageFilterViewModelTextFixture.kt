package com.tokopedia.product.manage.feature.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule

abstract class ProductManageFilterViewModelTextFixture {

    @RelaxedMockK
    lateinit var getProductManageFilterOptionsUseCase: GetProductManageFilterOptionsUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ProductManageFilterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ProductManageFilterViewModel(getProductManageFilterOptionsUseCase, userSession, CoroutineTestDispatchersProvider)
    }
}