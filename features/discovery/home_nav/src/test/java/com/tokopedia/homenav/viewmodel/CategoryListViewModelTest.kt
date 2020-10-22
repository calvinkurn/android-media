package com.tokopedia.homenav.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.homenav.category.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.category.domain.usecases.GetCategoryListUseCase
import com.tokopedia.homenav.category.viewModel.CategoryListViewModel
import com.tokopedia.homenav.rule.CoroutinesTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 21/10/20.
 */

@ExperimentalCoroutinesApi
class CategoryListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutinesTestRule()

    @RelaxedMockK
    private lateinit var getCategoryListUseCase: GetCategoryListUseCase
    private lateinit var viewModel : CategoryListViewModel

    @Before
    fun setup(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `Get success data`(){
        rule.testDispatcher.runBlockingTest {
            rule.testDispatcher.pauseDispatcher()

            coEvery {
                getCategoryListUseCase.executeOnBackground()
            } returns Success(listOf(DynamicHomeIconEntity.CategoryRow(id = 1)))

            viewModel = CategoryListViewModel(getCategoryListUseCase, rule.testDispatcher)

            viewModel.getCategory()

            rule.testDispatcher.resumeDispatcher()

            Assert.assertTrue(viewModel.categoryList.value is Success)
            Assert.assertTrue((viewModel.categoryList.value as Success).data.isNotEmpty())
        }
    }

    @Test
    fun `Get error data`(){
        rule.testDispatcher.runBlockingTest {
            rule.testDispatcher.pauseDispatcher()

            coEvery {
                getCategoryListUseCase.executeOnBackground()
            } returns Fail(TimeoutException())

            viewModel = CategoryListViewModel(getCategoryListUseCase, rule.testDispatcher)

            viewModel.getCategory()

            rule.testDispatcher.resumeDispatcher()

            Assert.assertTrue(viewModel.categoryList.value is Fail)
            Assert.assertTrue((viewModel.categoryList.value as Fail).throwable is TimeoutException)
        }
    }
}