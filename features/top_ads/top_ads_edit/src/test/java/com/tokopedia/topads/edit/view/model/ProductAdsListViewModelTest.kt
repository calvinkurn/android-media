package com.tokopedia.topads.edit.view.model

import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.edit.usecase.GetEtalaseUseCase
import com.tokopedia.topads.edit.usecase.GetProductUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductAdsListViewModelTest {
    private val getProductUseCase: GetProductUseCase = mockk(relaxed = true)
    private val getEtalaseUseCase: GetEtalaseUseCase = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ProductAdsListViewModel

    @Before
    fun setUp() {
        viewModel = ProductAdsListViewModel(testDispatcher, getProductUseCase, getEtalaseUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun etalaseList() {
        val data = ResponseEtalase.Data()
        every {
            getEtalaseUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseEtalase.Data) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.etalaseList {}
        verify {
            getEtalaseUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun productList() {
        val data = ResponseProductList.Result()
        every {
            getProductUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseProductList.Result) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.productList("key", "123", "", "", 1, 1, {}, {}, {})
        verify {
            getProductUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun addSemuaProduk() {
        val totalCount = 10
        val expected = ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
        val actual = viewModel.addSemuaProduk()
        assertNotEquals(actual, expected)
    }

    @Test
    fun onClearedSuccess() {
        viewModel.onCleared()
        verify { getEtalaseUseCase.cancelJobs() }
        verify { getProductUseCase.cancelJobs() }
    }
}