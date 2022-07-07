package com.tokopedia.topads.edit.view.model

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class KeywordAdsViewModelTest {

    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)
    private val searchKeywordUseCase = mockk<GraphqlUseCase<KeywordSearch>>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var count: HashMap<String, ArrayList<Int>>
    private lateinit var search: HashSet<String>
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        count = mockk(relaxed = true)
        search = mockk(relaxed = true)
        viewModel = KeywordAdsViewModel(testDispatcher,
            userSession,
            searchKeywordUseCase,
            suggestionKeywordUseCase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getSuggestionKeyword exception check`() {
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        var successCalled = false
        viewModel.getSuggestionKeyword("", 1) { successCalled = true }

        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `getSuggestionKeyword success test`() {
        val data = KeywordSuggestionResponse.Result()
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(KeywordSuggestionResponse.Result) -> Unit>()
            onSuccess.invoke(data)
        }

        var actual: List<KeywordData>? = null
        viewModel.getSuggestionKeyword("name", 12) { actual = it }

        Assert.assertEquals(data.topAdsGetKeywordSuggestionV3.data, actual)
    }

    @Test
    fun `getSuggestionKeyword test`() {
        viewModel.getSuggestionKeyword("name", 12) {}

        verify { suggestionKeywordUseCase.setParams(12, any()) }
        verify { suggestionKeywordUseCase.executeQuerySafeMode(any(), any()) }
    }

    @Test
    fun `searchKeyword success`() {
        val expected = KeywordSearch()
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""

        every { searchKeywordUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(KeywordSearch) -> Unit>().invoke(expected)
        }

        var actual: List<SearchData>? = null
        viewModel.searchKeyword(keyword = "keyword",
            product_ids = "productIds",
            onSucceed = { actual = it },
            resources = mockk(relaxed = true))

        Assert.assertEquals(expected.topAdsKeywordSearchTerm.data, actual)
    }

    @Test
    fun `searchKeyword error`() {
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""

        every { searchKeywordUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        var actual: List<SearchData>? = null
        viewModel.searchKeyword(keyword = "keyword",
            product_ids = "productIds",
            onSucceed = { actual = it },
            resources = mockk(relaxed = true))

        Assert.assertEquals(null, actual)
    }

    @Test
    fun `test searchKeyword if query is not null`() {

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""

        viewModel.searchKeyword(keyword = "keyword", product_ids = "productIds", onSucceed = {}, resources = mockk(relaxed = true))

        verify { searchKeywordUseCase.execute(any(), any()) }
    }


    @Test
    fun `test searchKeyword if query is null`() {

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns null

        viewModel.searchKeyword(keyword = "keyword", product_ids = "productIds",onSucceed = {}, resources = mockk(relaxed = true))

        verify(exactly = 0) { searchKeywordUseCase.execute(any(), any()) }
    }

    @Test
    fun onCleared() {
        viewModel.onCleared()
        verify { searchKeywordUseCase.cancelJobs() }
        verify { suggestionKeywordUseCase.cancelJobs() }
    }
}