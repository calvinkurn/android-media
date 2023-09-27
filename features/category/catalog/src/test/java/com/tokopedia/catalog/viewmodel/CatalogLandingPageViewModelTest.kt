package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.ui.viewmodel.CatalogLandingPageViewModel
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
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
    fun `this is dummy test`() {
        viewModel.getProductCatalogVersion("41")
    }
}
