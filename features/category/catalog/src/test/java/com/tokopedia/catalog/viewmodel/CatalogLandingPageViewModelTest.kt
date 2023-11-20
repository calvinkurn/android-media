package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.ui.viewmodel.CatalogLandingPageViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach

class CatalogLandingPageViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var catalogDetailUseCase: CatalogDetailUseCase

    val viewModel by lazy {
        spyk(
            CatalogLandingPageViewModel(
                CoroutineTestDispatchersProvider,
                catalogDetailUseCase
            )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    // Testing scope
    @Test
    fun `When getProductCatalogVersion is layout 4, Then should invoke usingV4AboveLayout true`() {
        coEvery {
            catalogDetailUseCase.initializeGetCatalogDetail(any())
        } returns true
        viewModel.getProductCatalogVersion("41")
        val result = viewModel.usingV4AboveLayout.getOrAwaitValue()
        assert(result)
    }

    @Test
    fun `When getProductCatalogVersion is layout below 3, Then should invoke usingV4AboveLayout false`() {
        coEvery {
            catalogDetailUseCase.initializeGetCatalogDetail(any())
        } returns false
        viewModel.getProductCatalogVersion("41")
        val result = viewModel.usingV4AboveLayout.getOrAwaitValue()
        assert(!result)
    }

    @Test
    fun `When getProductCatalogVersion is error, Then should invoke errorPage`() {
        val errorMessage = "error message"
        coEvery {
            catalogDetailUseCase.initializeGetCatalogDetail(any())
        } throws MessageErrorException(errorMessage)
        viewModel.getProductCatalogVersion("41")
        val result = viewModel.errorPage.getOrAwaitValue()
        assert(result.message == errorMessage)
    }
}
