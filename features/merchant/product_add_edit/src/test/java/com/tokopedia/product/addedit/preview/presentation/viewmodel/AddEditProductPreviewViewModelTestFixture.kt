package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach

@ExperimentalCoroutinesApi
abstract class AddEditProductPreviewViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getProductUseCase: GetProductUseCase

    @RelaxedMockK
    lateinit var getProductDraftUseCase: GetProductDraftUseCase

    @RelaxedMockK
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase

    @RelaxedMockK
    lateinit var getProductMapper: GetProductMapper

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    protected val viewModel: AddEditProductPreviewViewModel by lazy {
        spyk(AddEditProductPreviewViewModel(getProductUseCase,
                getProductMapper,
                resourceProvider,
                getProductDraftUseCase,
                saveProductDraftUseCase,
                TestCoroutineDispatchers))
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
}