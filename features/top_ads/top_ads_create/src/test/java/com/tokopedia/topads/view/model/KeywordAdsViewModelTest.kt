package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
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

    private lateinit var context: Context
    private lateinit var userSession: UserSession
    private lateinit var searchKeywordUseCase: GraphqlUseCase<KeywordSearch>
    private val suggestionKeywordUseCase: SuggestionKeywordUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        searchKeywordUseCase = mockk(relaxed = true)
        viewModel = spyk(KeywordAdsViewModel(context, rule.dispatchers, userSession, searchKeywordUseCase, suggestionKeywordUseCase))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `check invocation of onEmpty validateGroup`() {
        val expected = "empty"
        var actual = ""
        val data = listOf<KeywordData>()
        val onEmpty:() -> Unit = {
            actual = "empty"
        }
        val onSuccess:(List<KeywordData>) -> Unit = {
            if (it.isEmpty()) {
                onEmpty()
            }
        }
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke(data)
        }
        viewModel.getSuggestionKeyword(
                productIds = "",
                groupId = 1,
                onSuccess = onSuccess,
                onEmpty = onEmpty
        )
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess validateGroup`() {
        val expected = listOf(KeywordData(productId = "1", keywordData = listOf(KeywordDataItem(keyword = "test1"))))
        var actual:List<KeywordData> = listOf()
        val onSuccess:(List<KeywordData>) -> Unit = {
            actual = it
        }
        every {
            suggestionKeywordUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            onSuccess.invoke(expected)
        }
        viewModel.getSuggestionKeyword(
                productIds = "",
                groupId = 1,
                onSuccess = onSuccess,
                onEmpty = {  }
        )
        Assert.assertEquals(expected, actual)
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


}