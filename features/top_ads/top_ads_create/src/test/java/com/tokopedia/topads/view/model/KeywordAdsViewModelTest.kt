package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.data.response.KeywordData
import com.tokopedia.topads.data.response.KeywordDataItem
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.data.response.TopAdsGetKeywordSuggestionV3
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
class KeywordAdsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: KeywordAdsViewModel

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
        viewModel = spyk(KeywordAdsViewModel(context, rule.dispatchers, userSession, searchKeywordUseCase, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `check invocation of onEmpty validateGroup`() {
        val expected = "empty"
        var actual = ""
        val data = ResponseKeywordSuggestion.Result()
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseKeywordSuggestion.Result::class.java) } returns listOf()
        every { response.getData<ResponseKeywordSuggestion.Result>(ResponseKeywordSuggestion.Result::class.java) } returns data

        viewModel.getSuggestionKeyword(
                productIds = "",
                groupId = 1,
                onSuccess = {
                },
                onEmpty = {
                    actual = "empty"
                }
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess validateGroup`() {
        val expected = 1000
        var actual = 0
        val data = ResponseKeywordSuggestion.Result(TopAdsGetKeywordSuggestionV3(listOf(KeywordData(keywordData = listOf(KeywordDataItem(bidSuggest = expected))))))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseKeywordSuggestion.Result::class.java) } returns listOf()
        every { response.getData<ResponseKeywordSuggestion.Result>(ResponseKeywordSuggestion.Result::class.java) } returns data

        viewModel.getSuggestionKeyword(
                productIds = "",
                groupId = 1,
                onSuccess = {
                    actual = it[0].keywordData.get(0).bidSuggest
                },
                onEmpty = {
                }
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test searchKeyword`() {

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""

        viewModel.searchKeyword(keyword = "keyword", product_ids = "productIds") {}

        verify { searchKeywordUseCase.execute(any(), any()) }
    }


}