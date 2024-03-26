@file:Suppress("DEPRECATION")

package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.common.data.response.Category
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.Product
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopAdsProductResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MpAdsCreateGroupViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: MpAdsCreateGroupViewModel
    private val bidInfoUseCase: BidInfoUseCase = mockk(relaxed = true)
    private val topAdsGetProductUseCase: TopAdsGetProductUseCase = mockk(relaxed = true)
    private val topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase =
        mockk(relaxed = true)
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val topAdsCreateUseCase: TopAdsCreateUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = MpAdsCreateGroupViewModel(
            bidInfoUseCase,
            topAdsGetProductUseCase,
            topAdsGroupValidateNameUseCase,
            topAdsGetDepositUseCase,
            topAdsCreateUseCase,
            rule.dispatchers
        )
    }

    @Test
    fun `getBidInfo success`() {
        val response = ResponseBidInfo.Result()
        val data = response.topadsBidInfo.data
        var actual: List<TopadsBidInfo.DataItem>? = null
        coEvery { bidInfoUseCase.executeQuerySafeMode(any(), any()) } answers {
            firstArg<(ResponseBidInfo.Result) -> Unit>().invoke(response)
        }
        viewModel.getBidInfo(listOf(), String.EMPTY) {
            actual = it
        }
        assertEquals(data, actual)
    }

    @Test
    fun `getBidInfo failure`() {
        val error: Throwable = mockk(relaxed = true)
        coEvery { bidInfoUseCase.executeQuerySafeMode(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel.getBidInfo(listOf(), String.EMPTY) {}
        verify { error.printStackTrace() }
    }

    @Test
    fun `getProduct success`() {
        val category = Category(
            id = String.EMPTY,
            name = String.EMPTY,
            title = String.EMPTY
        )
        val product = Product(
            productId = String.EMPTY,
            productName = String.EMPTY,
            price = String.EMPTY,
            description = String.EMPTY,
            category = category
        )
        val response = TopAdsProductResponse(
            product = product,
            errors = listOf()
        )
        var actual: TopAdsProductResponse? = null
        coEvery { topAdsGetProductUseCase.getProduct(any(), any()) } answers {
            secondArg<(TopAdsProductResponse) -> Unit>().invoke(response)
        }
        viewModel.getProduct(String.EMPTY) {
            actual = it
        }
        assertEquals(response, actual)
    }

    @Test
    fun `validateGroup success`() {
        val response = ResponseGroupValidateName()
        val data = response.topAdsGroupValidateName
        var actual: ResponseGroupValidateName.TopAdsGroupValidateNameV2? = null
        coEvery { topAdsGroupValidateNameUseCase.execute(any(), any()) } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(response)
        }
        viewModel.validateGroup(String.EMPTY) {
            actual = it
        }
        assertEquals(data, actual)
    }

    @Test
    fun `validateGroup failure`() {
        val error: Throwable = mockk(relaxed = true)
        coEvery { topAdsGroupValidateNameUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel.validateGroup(String.EMPTY) {}
        verify { error.printStackTrace() }
    }

    @Test
    fun `topAdsCreate success no error`() {
        val response = FinalAdResponse()
        val dataGroup = response.topadsManageGroupAds.groupResponse
        val data = dataGroup.data.id
        var actual: String? = null
        coEvery { topAdsCreateUseCase.execute(any()) } answers {
            response
        }
        viewModel.topAdsCreate(listOf(), String.EMPTY, Double.NaN, {
            actual = it
        }) {}
        assertEquals(data, actual)
    }

    @Test
    fun `topAdsCreate success with error`() {
        val response = FinalAdResponse(
            topadsManageGroupAds = FinalAdResponse.TopadsManageGroupAds(
                groupResponse = FinalAdResponse.TopadsManageGroupAds.GroupResponse(
                    errors = listOf(
                        FinalAdResponse.TopadsManageGroupAds.ErrorsItem(
                            code = String.EMPTY,
                            detail = String.EMPTY,
                            title = String.EMPTY
                        )
                    )
                ),
                keywordResponse = FinalAdResponse.TopadsManageGroupAds.KeywordResponse(
                    errors = listOf(
                        FinalAdResponse.TopadsManageGroupAds.ErrorsItem(
                            code = String.EMPTY,
                            detail = String.EMPTY,
                            title = String.EMPTY
                        )
                    )
                )
            )
        )
        val dataGroup = response.topadsManageGroupAds.groupResponse
        val dataKeyword = response.topadsManageGroupAds.keywordResponse
        val data =
            dataGroup.errors?.firstOrNull()?.detail + dataKeyword.errors?.firstOrNull()?.detail
        var actual: String? = null
        coEvery { topAdsCreateUseCase.execute(any()) } answers {
            response
        }
        viewModel.topAdsCreate(listOf(), String.EMPTY, Double.NaN, {}) {
            actual = it
        }
        assertEquals(data, actual)
    }

    @Test
    fun `topAdsCreate failure`() {
        val error: Throwable = mockk(relaxed = true)
        coEvery { topAdsCreateUseCase.execute(any()) } throws error
        viewModel.topAdsCreate(listOf(), String.EMPTY, Double.NaN, {}, {})
        verify { error.printStackTrace() }
    }

    @Test
    fun `getTopAdsDeposit success`() {
        val response = Deposit()
        val data = response.topadsDashboardDeposits.data
        var actual: DepositAmount? = null
        coEvery { topAdsGetDepositUseCase.execute(any(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(response)
        }
        viewModel.getTopAdsDeposit {
            actual = it
        }
        assertEquals(data, actual)
    }

    @Test
    fun `getTopAdsDeposit failure`() {
        val error: Throwable = mockk(relaxed = true)
        coEvery { topAdsGetDepositUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel.getTopAdsDeposit {}
        verify { error.printStackTrace() }
    }

    @Test
    fun `onCleared calls cancelJobs`() {
        val method = viewModel.javaClass.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)
        verify { bidInfoUseCase.cancelJobs() }
    }

}
