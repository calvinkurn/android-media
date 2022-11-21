package com.tokopedia.topads.view.model

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.RequestParams
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
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)


    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        viewModel = spyk(SummaryViewModel(rule.dispatchers,
            validGroupUseCase,
            topAdsGetShopDepositUseCase,
            topAdsCreateUseCase))
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
    fun `getTopAdsDeposit success test`() {
        val obj = Deposit()
        every { topAdsGetShopDepositUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(obj)
        }
        var actual: DepositAmount? = null
        viewModel.getTopAdsDeposit({ actual = it }, {})
        Assert.assertEquals(obj.topadsDashboardDeposits.data, actual)
    }

    @Test
    fun `getTopAdsDeposit error test`() {
        val obj = Throwable("it")
        every { topAdsGetShopDepositUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(obj)
        }
        var actual: Throwable? = null
        viewModel.getTopAdsDeposit({}, { actual = it })
        Assert.assertEquals(obj, actual)
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
    fun `validateGroup success`() {
        val expected = ResponseGroupValidateName()
        var actual: ResponseGroupValidateName.TopAdsGroupValidateNameV2? = null

        every {
            validGroupUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(expected)
        }

        viewModel.validateGroup("name") { actual = it }
        verify { validGroupUseCase.setParams(any(), any()) }
        Assert.assertEquals(expected.topAdsGroupValidateName, actual)
    }

    @Test
    fun `validateGroup error`() {
        var actual: ResponseGroupValidateName.TopAdsGroupValidateNameV2? = null

        every {
            validGroupUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        viewModel.validateGroup("name") { actual = it }
        Assert.assertEquals(null, actual)
    }

    @Test
    fun `test exception in topAdsCreated`() {
        val dataProduct: Bundle = mockk()
        val dataKeyword: HashMap<String, Any?> = mockk()
        val dataGroup: HashMap<String, Any?> = mockk()
        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup, {}, {})
        coVerify {
            topAdsCreateUseCase.execute(any<RequestParams>())
        }
    }

    @Test
    fun `topadscreated success check`() {
        every {
            topAdsCreateUseCase.setParam(any(),
                any(),
                any(),
                any())
        } returns mockk(relaxed = true)
        coEvery { topAdsCreateUseCase.execute(any<RequestParams>()) } returns FinalAdResponse()

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), { successCalled = true }, {})
        Assert.assertTrue(successCalled)
    }

    @Test
    fun `topadscreated error check`() {
        every {
            topAdsCreateUseCase.setParam(any(),
                any(),
                any(),
                any())
        } returns mockk(relaxed = true)
        coEvery { topAdsCreateUseCase.execute(any<RequestParams>()) } returns FinalAdResponse(
            FinalAdResponse.TopadsManageGroupAds(FinalAdResponse.TopadsManageGroupAds.KeywordResponse(
                errors = listOf(
                    FinalAdResponse.TopadsManageGroupAds.ErrorsItem())),
                FinalAdResponse.TopadsManageGroupAds.GroupResponse(errors = listOf(FinalAdResponse.TopadsManageGroupAds.ErrorsItem()))))

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), { successCalled = true }, {})
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun `topadscreated exception check`() {
        every { topAdsCreateUseCase.setParam(any(), any(), any(), any()) } throws Throwable()

        var successCalled = false
        viewModel.topAdsCreated(mockk(), mockk(), mockk(), { successCalled = true }, {})
        Assert.assertTrue(!successCalled)
    }

    @Test
    fun onClear() {
        viewModel.onCleared()
        verify { validGroupUseCase.cancelJobs() }
        verify { topAdsGetShopDepositUseCase.cancelJobs() }
    }
}