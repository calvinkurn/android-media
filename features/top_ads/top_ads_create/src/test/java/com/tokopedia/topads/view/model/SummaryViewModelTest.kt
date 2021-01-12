package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.data.response.ResponseCreateGroup
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
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
class SummaryViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: SummaryViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context

    private var topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(SummaryViewModel(context, rule.dispatchers, topAdsGetShopDepositUseCase, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `test result in getTopAdsDeposit`() {
        viewModel.getTopAdsDeposit({}, {})
        verify {
            topAdsGetShopDepositUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test exception in topAdsCreated`() {
        var t: Throwable? = null
        val myThrowable: Throwable = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws myThrowable

        viewModel.topAdsCreated(hashMapOf(),
                onSuccessGetDeposit = {},
                onErrorGetAds = { t = it }
        )
        Assert.assertEquals(myThrowable.message, t?.message)
    }

    @Test
    fun `test result in topAdsCreated`() {
        val expected = 1000
        var actual = 0
        val data = ResponseCreateGroup(ResponseCreateGroup.TopadsCreateGroupAds(ResponseCreateGroup.TopadsCreateGroupAds.Data(listOf(ResponseCreateGroup.KeywordsItem(priceBid = expected)))))
        val response: GraphqlResponse = mockk(relaxed = true)
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseCreateGroup::class.java) } returns listOf()
        every { response.getData<ResponseCreateGroup>(ResponseCreateGroup::class.java) } returns data

        viewModel.topAdsCreated(
                hashMapOf(),
                onSuccessGetDeposit = {
                    actual = it.topadsCreateGroupAds.data.keywords[0].priceBid
                },
                onErrorGetAds = {}
        )

        Assert.assertEquals(expected, actual)
    }

}