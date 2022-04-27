package com.tokopedia.topads.edit.view.model

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
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
    private val context:Context= mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        count = mockk(relaxed = true)
        search = mockk(relaxed = true)
        viewModel = KeywordAdsViewModel(testDispatcher,context,userSession,searchKeywordUseCase, suggestionKeywordUseCase)
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
    fun getSuggestionKeyword() {
        val data = KeywordSuggestionResponse.Result()
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(KeywordSuggestionResponse.Result) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getSuggestionKeyword("name", 12) {}

        verify {
            suggestionKeywordUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `test searchKeyword if query is not null`() {

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""

        viewModel.searchKeyword(keyword = "keyword", product_ids = "productIds") {}

        verify { searchKeywordUseCase.execute(any(), any()) }
    }


    @Test
    fun `test searchKeyword if query is null`() {

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns null

        viewModel.searchKeyword(keyword = "keyword", product_ids = "productIds") {}

        verify(exactly = 0) { searchKeywordUseCase.execute(any(), any()) }
    }


    @Test
    fun onCleared() {
        viewModel.onCleared()
        verify { suggestionKeywordUseCase.cancelJobs() }
    }
}