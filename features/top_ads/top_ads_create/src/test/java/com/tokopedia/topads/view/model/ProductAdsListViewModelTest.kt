package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSession
import io.mockk.*
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

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private lateinit var userSession: UserSession
    private lateinit var searchKeywordUseCase: GraphqlUseCase<KeywordSearch>

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        searchKeywordUseCase = mockk(relaxed = true)
        viewModel = spyk(ProductAdsListViewModel(context, rule.dispatchers, userSession, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `test exception in etalaseList`() {
        var t: Throwable? = null
        val myThrowable: Throwable = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws myThrowable

        viewModel.etalaseList(
                onSuccess = {},
                onError = { t = it }
        )

        Assert.assertEquals(myThrowable.message, t?.message)
    }

    @Test
    fun `test result in etalaseList`() {
        val expected = "name"
        var actual = ""
        val data = ResponseEtalase.Data(ResponseEtalase.Data.ShopShowcasesByShopID(listOf(ResponseEtalase.Data.ShopShowcasesByShopID.Result(name = expected))))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseEtalase.Data::class.java) } returns listOf()
        every { response.getData<ResponseEtalase.Data>(ResponseEtalase.Data::class.java) } returns data

        viewModel.etalaseList(
                onSuccess = {
                    actual = it[0].name
                },
                onError = {}
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test exception in productList`() {
        var t: Throwable? = null
        val myThrowable: Throwable = Exception("my excep")

        every { userSession.shopId } returns "2"

        coEvery { repository.getReseponse(any(), any()) } throws myThrowable

        viewModel.productList("", "", "", "", 0, 0,
                onSuccess = { list, eof -> },
                onEmpty = {},
                onError = { t = it })

        Assert.assertEquals(myThrowable.message, t?.message)
    }

    @Test
    fun `check invocation of onEmpty in productList`() {
        val expected = "empty"
        var actual = ""
        val data = ResponseProductList.Result()
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseProductList.Result::class.java) } returns listOf()
        every { response.getData<ResponseProductList.Result>(ResponseProductList.Result::class.java) } returns data

        viewModel.productList("", "", "", "", 0, 0,
                onSuccess = { list, eof -> },
                onEmpty = { actual = "empty" },
                onError = {})

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess in productList`() {
        val expectedSize = 1
        var actualSize = 0
        val expectedEof = true
        var actualEof = false

        val data = ResponseProductList.Result(ResponseProductList.Result.TopadsGetListProduct(listOf(ResponseProductList.Result.TopadsGetListProduct.Data()), eof = expectedEof))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseProductList.Result::class.java) } returns listOf()
        every { response.getData<ResponseProductList.Result>(ResponseProductList.Result::class.java) } returns data

        viewModel.productList("", "eid", "", "", 0, 0,
                onSuccess = { list, eof ->
                    actualSize = list.size
                    actualEof = eof
                },
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