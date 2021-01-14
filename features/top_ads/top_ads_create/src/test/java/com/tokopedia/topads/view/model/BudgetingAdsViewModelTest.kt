package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.data.response.BidInfoDataItem
import com.tokopedia.topads.data.response.ResponseBidInfo
import com.tokopedia.topads.data.response.TopadsBidInfo
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
class BudgetingAdsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: BudgetingAdsViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private lateinit var userSession: UserSession


    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel = spyk(BudgetingAdsViewModel(context, userSession, rule.dispatchers, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }


    @Test
    fun `check onEmpty invocation in getBidInfo`() {
        val expected = "empty"
        var actual = ""
        val data = ResponseBidInfo.Result()
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseBidInfo.Result::class.java) } returns listOf()
        every { response.getData<ResponseBidInfo.Result>(ResponseBidInfo.Result::class.java) } returns data

        viewModel.getBidInfo(
                suggestions = listOf(),
                onSuccess = {

                },
                onEmpty = {
                    actual = "empty"
                }
        )

        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `check onSuccess invocation in getBidInfo`() {
        val expected = 1000
        var actual = 0
        val data = ResponseBidInfo.Result(TopadsBidInfo(listOf(BidInfoDataItem(suggestionBid = expected))))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseBidInfo.Result::class.java) } returns listOf()
        every { response.getData<ResponseBidInfo.Result>(ResponseBidInfo.Result::class.java) } returns data

        viewModel.getBidInfo(
                suggestions = listOf(),
                onSuccess = {
                    actual = it[0].suggestionBid
                },
                onEmpty = {
                }
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check onSuccess invocation in getBidInfoDefault`() {
        val expected = 1000
        var actual = 0
        val data = ResponseBidInfo.Result(TopadsBidInfo(listOf(BidInfoDataItem(suggestionBid = expected))))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "2"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseBidInfo.Result::class.java) } returns listOf()
        every { response.getData<ResponseBidInfo.Result>(ResponseBidInfo.Result::class.java) } returns data

        viewModel.getBidInfoDefault(listOf()) {
            actual = it[0].suggestionBid
        }

        Assert.assertEquals(expected, actual)
    }


}