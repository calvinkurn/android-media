package com.tokopedia.kategori.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.usecase.CategoryLevelTwoItemsUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class CategoryLevelTwoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val categoryLevelTwoItemsUseCase: CategoryLevelTwoItemsUseCase = mockk(relaxed = true)

    private val viewModel: CategoryLevelTwoViewModel by lazy {
        spyk(CategoryLevelTwoViewModel(categoryLevelTwoItemsUseCase))
    }


    @RelaxedMockK
    lateinit var requestParams: RequestParams

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    companion object {
        const val ALL_CATEGORY_GQL_RESPONSE_JSON_FILE_PATH = "json/gql_all_category.json"
    }


    @Test
    fun ` category list call fails`() {
        val exception = Exception("adfad")
        every { categoryLevelTwoItemsUseCase.createRequestParams(2, true, "0") } returns requestParams

        coEvery { categoryLevelTwoItemsUseCase.getCategoryListItems(requestParams) } throws exception

        viewModel.refresh("0")
        Assert.assertEquals(viewModel.childItem.value, Fail(exception))
    }


    @Test
    fun `verify calls`() {
        val list: ArrayList<CategoryChildItem> = ArrayList()
        list.add(mockk())

        every { categoryLevelTwoItemsUseCase.createRequestParams(2, true, "0") } returns requestParams

        coEvery { categoryLevelTwoItemsUseCase.getCategoryListItems(requestParams) } returns list

        viewModel.refresh("0")

        Assert.assertEquals(viewModel.childItem.value, Success(list))

    }


}