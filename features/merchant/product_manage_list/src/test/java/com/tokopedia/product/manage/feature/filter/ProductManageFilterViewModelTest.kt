package com.tokopedia.product.manage.feature.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductManageFilterViewModelTest {

    @RelaxedMockK
    lateinit var getGetProductManageFilterOptionsUseCase: GetProductManageFilterOptionsUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ProductManageFilterViewModel(getGetProductManageFilterOptionsUseCase, userSession, dispatchers)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getCombined should execute expected use case`() {
        mockkObject(getGetProductManageFilterOptionsUseCase)
        coEvery {
            getGetProductManageFilterOptionsUseCase.executeOnBackground()
        }
        viewModel.getData("0")
        coVerify {
            getGetProductManageFilterOptionsUseCase.executeOnBackground()
        }
    }
}