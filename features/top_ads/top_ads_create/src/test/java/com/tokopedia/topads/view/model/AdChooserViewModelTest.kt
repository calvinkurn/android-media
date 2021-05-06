package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.data.response.AdCreationOption
import com.tokopedia.topads.data.response.TopAdsAutoAdsCreate
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AdChooserViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: AdChooserViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private lateinit var userSession: UserSession

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel = spyk(AdChooserViewModel(context, userSession, rule.dispatchers, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `test result in getShopAdsInfo`() {
        val expected = "categoryDesc"
        var actual = ""
        val data = AdCreationOption(AdCreationOption.TopAdsGetShopInfo(data = AdCreationOption.TopAdsGetShopInfo.Data(expected)))
        val response: GraphqlResponse = mockk(relaxed = true)

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(AdCreationOption::class.java) } returns listOf()
        every { response.getData<AdCreationOption>(AdCreationOption::class.java) } returns data

        viewModel.getAdsState {
            actual = it.topAdsGetShopInfo.data.categoryDesc
        }

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test result in postAutoAds`() {

        val expected = "status_desc"
        var actual: String?
        val data = TopAdsAutoAdsCreate.Response(TopAdsAutoAdsCreate(TopAdsAutoAdsCreate.Response.TopAdsAutoAdsData(statusDesc = expected)))
        val response: GraphqlResponse = mockk(relaxed = true)

        every { userSession.shopId } returns "12"

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(TopAdsAutoAdsCreate.Response::class.java) } returns listOf()
        every { response.getData<TopAdsAutoAdsCreate.Response>(TopAdsAutoAdsCreate.Response::class.java) } returns data

        viewModel.postAutoAds("toggle_status", budget = 1000)

        actual = viewModel.autoAdsData.value?.statusDesc

        Assert.assertEquals(expected, actual)

    }

    @Test
    fun `test result in getAutoAdsStatus`() {

        val expected = "status_desc"
        var actual: String? = ""
        val data = AutoAdsResponse(AutoAdsResponse.TopAdsGetAutoAds(AutoAdsResponse.TopAdsGetAutoAds.Data(statusDesc = expected)))
        val response: GraphqlResponse = mockk(relaxed = true)

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(AutoAdsResponse::class.java) } returns listOf()
        every { response.getData<AutoAdsResponse>(AutoAdsResponse::class.java) } returns data

        viewModel.getAutoAdsStatus {
            actual = it.topAdsGetAutoAds.data.statusDesc
        }

        Assert.assertEquals(expected, actual)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}