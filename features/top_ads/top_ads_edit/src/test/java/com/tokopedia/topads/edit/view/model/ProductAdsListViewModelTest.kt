package com.tokopedia.topads.edit.view.model

import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductAdsListViewModelTest {
    private val getProductUseCase: TopAdsGetListProductUseCase = mockk(relaxed = true)
    private val getEtalaseUseCase: GetEtalaseListUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ProductAdsListViewModel

    @Before
    fun setUp() {
        viewModel = ProductAdsListViewModel(testDispatcher, userSession, getProductUseCase, getEtalaseUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun etalaseList() {
        val data = ResponseEtalase.Data()
        every { userSession.shopId } returns "123"
        every {
            getEtalaseUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseEtalase.Data) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.etalaseList {}
        verify {
            getEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun productList() {
        val data = ResponseProductList.Result()
        every { userSession.shopId } returns "123"
        every {
            getProductUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseProductList.Result) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.productList("key", "123", "", "", 1, 1,
                { _: List<TopAdsProductModel>, b: Boolean -> }, {}, {})
        verify {
            getProductUseCase.execute(any(), any())
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