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
import org.junit.Assert
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

        var actual : List<ResponseEtalase.Data.ShopShowcasesByShopID.Result> ?= null
        viewModel.etalaseList {actual = it}
        verify {
            getEtalaseUseCase.execute(any(), any())
        }
        Assert.assertEquals(data.shopShowcasesByShopID.result, actual)
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
        viewModel.productList("key", "123", "", "", 1, 1,"",
                { _: List<TopAdsProductModel>, b: Boolean -> }, {}, {})
        verify {
            getProductUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test exception of execute in etalaseList`() {
        val expected = Exception("my excep")

        every { getEtalaseUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(expected)
        }

        var successCalled = false
        viewModel.etalaseList(onSuccess = {successCalled = true})
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `test exception of execute in productList`() {
        var actual: Throwable? = null
        val expected = Exception("my excep")

        val onError: (Throwable) -> Unit = {
            actual = expected
        }
        every { getProductUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(expected)
        }

        viewModel.productList("", "", "", "", 0, 0, "",
            onSuccess = { _, _ -> },
            onEmpty = {},
            onError = onError
        )
        Assert.assertEquals(actual?.message, expected.message)
    }

    @Test
    fun `check invocation of onSuccess on etalseid empty in productList`() {

        val data = ResponseProductList.Result(ResponseProductList.Result.TopadsGetListProduct(listOf(TopAdsProductModel())))
        every { userSession.shopId } returns "2"
        every { getProductUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(ResponseProductList.Result)->Unit>().invoke(data)
        }
        var successCalled = false
        viewModel.productList("", "", "", "", 0, 0, "",
            onSuccess = { _, _ -> successCalled = true },
            onEmpty = { },
            onError = {})

        Assert.assertTrue(successCalled)
    }

    @Test
    fun `addsemuaProudk totalcount check if etalseid is empty in productList`() {
        val expected = 1
        val data =
            ResponseProductList.Result(ResponseProductList.Result.TopadsGetListProduct(listOf(
                TopAdsProductModel())))
        every { userSession.shopId } returns "2"
        every { getProductUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(ResponseProductList.Result) -> Unit>().invoke(data)
        }
        viewModel.productList("", "", "", "", 0, 0, "",
            { _, _ -> }, { }, {})

        Assert.assertEquals(viewModel.addSemuaProduk().count, expected)

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