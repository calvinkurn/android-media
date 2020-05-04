package com.tokopedia.product.addedit.preview.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.coroutine.TestCoroutineDispatchers
import com.tokopedia.product.addedit.description.domain.usecase.GetProductVariantUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.GetProductDraftUseCase
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.domain.GetProductUseCase
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class AddEditProductPreviewViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    private val mContextMock = mockk<Context>(relaxed = true)

    @RelaxedMockK
    lateinit var getProductUseCase: GetProductUseCase
    @RelaxedMockK
    lateinit var getProductVariantUseCase: GetProductVariantUseCase
    @RelaxedMockK
    lateinit var getProductDraftUseCase: GetProductDraftUseCase
    @RelaxedMockK
    lateinit var saveProductDraftUseCase: SaveProductDraftUseCase

    protected lateinit var viewModel: AddEditProductPreviewViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AddEditProductPreviewViewModel(
                getProductUseCase,
                GetProductMapper(),
                getProductVariantUseCase,
                ResourceProvider(mContextMock),
                getProductDraftUseCase,
                saveProductDraftUseCase,
                TestCoroutineDispatchers)
    }

}