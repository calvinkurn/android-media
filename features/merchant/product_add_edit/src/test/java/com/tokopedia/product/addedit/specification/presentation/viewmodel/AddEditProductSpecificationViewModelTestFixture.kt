package com.tokopedia.product.addedit.specification.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
abstract class AddEditProductSpecificationViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var annotationCategoryUseCase: AnnotationCategoryUseCase

    protected val viewModel: AddEditProductSpecificationViewModel by lazy {
        spyk(AddEditProductSpecificationViewModel(
                CoroutineTestDispatchersProvider,
                annotationCategoryUseCase
        ))
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }
}