package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.usecase.GetCategoryRecommendationUseCase
import com.tokopedia.product.addedit.detail.domain.usecase.GetNameRecommendationUseCase
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.any

@ExperimentalCoroutinesApi
class AddEditProductDetailViewModelTest {

    @RelaxedMockK
    lateinit var provider: ResourceProvider

    @RelaxedMockK
    lateinit var getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase

    @RelaxedMockK
    lateinit var getNameRecommendationUseCase: GetNameRecommendationUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val viewModel: AddEditProductDetailViewModel by lazy {
        AddEditProductDetailViewModel(provider, coroutineDispatcher, getNameRecommendationUseCase, getCategoryRecommendationUseCase)
    }

    @Test
    fun `success get category recommendation`() = runBlocking {
        val successResult = listOf(any<ListItemUnify>(), any<ListItemUnify>(), any<ListItemUnify>())

        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } returns successResult

        viewModel.getCategoryRecommendation("baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productCategoryRecommendationLiveData.value
        Assert.assertTrue(result != null && result == Success(successResult))
    }

    @Test
    fun `failed get category recommendation`() = runBlocking {
        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getCategoryRecommendation("baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productCategoryRecommendationLiveData.value
        Assert.assertTrue(result != null && result is Fail)
    }

    @Test
    fun `success get name recommendation`() = runBlocking {
        val resultNameRecommendation = listOf("batik", "batik couple", "baju batik wanita", "baju batik pria", "batik kultut")

        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } returns resultNameRecommendation

        viewModel.getProductNameRecommendation(query = "batik")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        val resultViewmodel = viewModel.productNameRecommendations.value
        Assert.assertTrue(resultViewmodel != null && resultViewmodel == Success(resultNameRecommendation))
    }

    @Test
    fun `failed get name recommendation`() = runBlocking {
        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getProductNameRecommendation(query = "baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productNameRecommendations.value
        Assert.assertTrue(result != null && result is Fail)
    }
}