package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.common.domain.usecase.GetEtalaseListUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetListProductUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSession
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProductAdsListViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: ProductAdsListViewModel

    private lateinit var userSession: UserSession
    private val getEtalaseListUseCase: GetEtalaseListUseCase = mockk(relaxed = true)
    private val topAdsGetListProductUseCase: TopAdsGetListProductUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        userSession = mockk(relaxed = true)
        viewModel = spyk(ProductAdsListViewModel(rule.dispatchers, userSession, getEtalaseListUseCase, topAdsGetListProductUseCase))
    }

    @Test
    fun `test exception in etalaseList`() {
        var actual: Throwable? = null
        val expected = Exception("my excep")

        val onError: (Throwable) -> Unit = {
            actual = expected
        }
        every { getEtalaseListUseCase.execute(any(), any()) } throws expected

        viewModel.etalaseList(
                onSuccess = {},
                onError = onError
        )
        Assert.assertEquals(actual?.message, expected.message)
    }

    @Test
    fun `test result in etalaseList`() {
        val expected = "name"
        var actual = ""
        val data = ResponseEtalase.Data(ResponseEtalase.Data.ShopShowcasesByShopID(listOf(ResponseEtalase.Data.ShopShowcasesByShopID.Result(name = expected))))
        val onSuccess: (List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit = {
            actual = it.first().name
        }
        every { userSession.shopId } returns "2"
        every { getEtalaseListUseCase.execute(captureLambda(), any()) } answers {
            onSuccess(data.shopShowcasesByShopID.result)
        }
        viewModel.etalaseList(
                onSuccess = onSuccess,
                onError = {}
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test exception in productList`() {
        var actual: Throwable? = null
        val expected = Exception("my excep")

        val onError: (Throwable) -> Unit = {
            actual = expected
        }
        every { userSession.shopId } returns "2"
        every { topAdsGetListProductUseCase.execute(any(), any()) } throws expected

        viewModel.productList("", "", "", "", 0, 0,
                onSuccess = { _, _ -> },
                onEmpty = {},
                onError = onError
        )
        Assert.assertEquals(actual?.message, expected.message)
    }

    @Test
    fun `check invocation of onEmpty in productList`() {
        val expected = "empty"
        var actual = ""
        val data = ResponseProductList.Result()
        val onEmpty: (() -> Unit) = {
            actual = expected
        }

        every { userSession.shopId } returns "2"

        every { topAdsGetListProductUseCase.execute(captureLambda(), any()) } answers {
            if (data.topadsGetListProduct.data.isEmpty()) {
                onEmpty()
            }
        }
        viewModel.productList("", "", "", "", 0, 0,
                onSuccess = { _, _ -> },
                onEmpty = onEmpty,
                onError = {})
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess in productList`() {
        val expectedSize = 1
        var actualSize = 0
        val expectedEof = true
        var actualEof = false
        val onSuccess: ((List<TopAdsProductModel>, eof: Boolean) -> Unit) = { list, eof ->
            actualSize = list.size
            actualEof = eof
        }
        val data = ResponseProductList.Result(ResponseProductList.Result.TopadsGetListProduct(listOf(TopAdsProductModel()), eof = expectedEof))
        every { userSession.shopId } returns "2"
        every { topAdsGetListProductUseCase.execute(captureLambda(), any()) } answers {
            if (data.topadsGetListProduct.data.isNotEmpty()) {
                onSuccess(data.topadsGetListProduct.data, data.topadsGetListProduct.eof)
            }
        }
        viewModel.productList("", "eid", "", "", 0, 0,
                onSuccess = onSuccess,
                onEmpty = { },
                onError = {})

        Assert.assertEquals(expectedSize, actualSize)
        Assert.assertEquals(expectedEof, actualEof)
    }

    @Test
    fun `test addSemuaProduk`() {

        val expected = "Semua Etalase"

        val result = viewModel.addSemuaProduk()
        val actual = result.name

        Assert.assertEquals(expected, actual)
    }

}