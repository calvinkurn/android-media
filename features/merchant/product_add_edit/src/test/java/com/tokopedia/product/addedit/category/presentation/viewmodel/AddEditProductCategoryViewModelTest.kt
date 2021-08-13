package com.tokopedia.product.addedit.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.core.common.category.domain.interactor.GetCategoryLiteTreeUseCase
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import rx.Observable
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class AddEditProductCategoryViewModelTest {
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getCategoryLiteTreeUseCase: GetCategoryLiteTreeUseCase

    private lateinit var viewModel: AddEditProductCategoryViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = AddEditProductCategoryViewModel(
                CoroutineTestDispatchersProvider,
                getCategoryLiteTreeUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `check whether get category lite tree is success`() {
        runBlocking {
            coEvery {
                getCategoryLiteTreeUseCase.createObservable(any())
            } returns Observable.just(CategoriesResponse())

            viewModel.getCategoryLiteTree()

            coVerify { getCategoryLiteTreeUseCase.createObservable(any())}
            Assert.assertTrue(viewModel.categoryLiteTree.value is Success)
        }
    }

    @Test
    fun `check whether get category lite tree is fail`() {
        runBlocking {
            coEvery {
                getCategoryLiteTreeUseCase.createObservable(any())
            } throws Throwable()

            viewModel.getCategoryLiteTree()

            coVerify { getCategoryLiteTreeUseCase.createObservable(any())}
            Assert.assertTrue(viewModel.categoryLiteTree.value is Fail)
        }
    }
}