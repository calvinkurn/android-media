package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
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
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase = mockk(relaxed = true)


    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(SummaryViewModel(context, rule.dispatchers,validGroupUseCase, topAdsGetShopDepositUseCase, repository))
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
    fun validateGroup() {
        val data = ResponseGroupValidateName()
        every {
            validGroupUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ResponseGroupValidateName) -> Unit>()
            onSuccess.invoke(data)
        }

        viewModel.validateGroup("name", {})

        verify {
            validGroupUseCase.execute(any(), any())
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
        val response: GraphqlResponse = mockk(relaxed = true)
        val successData: ResponseCreateGroup = mockk(relaxed = true)
        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseCreateGroup::class.java) } returns listOf()
        every { response.getData<ResponseCreateGroup>(ResponseCreateGroup::class.java) } returns successData
        every { successData.topadsCreateGroupAds.errors } returns emptyList()
        viewModel.topAdsCreated(
                hashMapOf(),
                { actual = 1000 },
                onErrorGetAds = {}
        )

        Assert.assertEquals(expected, actual)
    }

}