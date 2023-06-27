package com.tokopedia.product.addedit.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.manage.common.feature.category.domain.GetCategoryLiteTreeUseCase
import com.tokopedia.product.manage.common.feature.category.model.CategoriesResponse
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

@ExperimentalCoroutinesApi
class AddEditProductCategoryUIModelTest {
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
                getCategoryLiteTreeUseCase.getCategoryLiteData(any())
            } returns CategoriesResponse()

            viewModel.getCategoryLiteTree()

            coVerify { getCategoryLiteTreeUseCase.getCategoryLiteData(any()) }
            Assert.assertTrue(viewModel.categoryLiteTree.value is Success)
        }
    }

    @Test
    fun `check whether get category lite tree is fail`() {
        runBlocking {
            coEvery {
                getCategoryLiteTreeUseCase.getCategoryLiteData(any())
            } throws Throwable()

            viewModel.getCategoryLiteTree()

            coVerify { getCategoryLiteTreeUseCase.getCategoryLiteData(any()) }
            Assert.assertTrue(viewModel.categoryLiteTree.value is Fail)
        }
    }
}
