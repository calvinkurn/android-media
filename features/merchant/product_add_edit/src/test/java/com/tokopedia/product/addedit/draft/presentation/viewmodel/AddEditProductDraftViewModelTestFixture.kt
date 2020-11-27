package com.tokopedia.product.addedit.draft.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.common.coroutine.CoroutineDispatchersProvider
import com.tokopedia.product.addedit.draft.domain.usecase.*
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
abstract class AddEditProductDraftViewModelTestFixture {
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var deleteAllProductDraftUseCase: DeleteAllProductDraftUseCase

    @RelaxedMockK
    lateinit var deleteProductDraftUseCase: DeleteProductDraftUseCase

    @RelaxedMockK
    lateinit var getAllProductDraftUseCase: GetAllProductDraftUseCase

    protected val viewModel: AddEditProductDraftViewModel by lazy {
        AddEditProductDraftViewModel(
                CoroutineDispatchersProvider,
                deleteProductDraftUseCase,
                deleteAllProductDraftUseCase,
                getAllProductDraftUseCase
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
}