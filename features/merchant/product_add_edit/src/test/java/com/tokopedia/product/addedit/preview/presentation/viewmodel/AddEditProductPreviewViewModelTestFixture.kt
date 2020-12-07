package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.domain.usecase.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.domain.usecase.ValidateProductNameUseCase
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import kotlin.jvm.Throws

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
    lateinit var validateProductNameUseCase: ValidateProductNameUseCase

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
                validateProductNameUseCase,
                CoroutineTestDispatchersProvider))
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