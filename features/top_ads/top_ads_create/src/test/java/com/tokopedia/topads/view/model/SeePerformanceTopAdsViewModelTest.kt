@file:Suppress("DEPRECATION")

package com.tokopedia.topads.view.model

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.model.TotalProductKeyResponse
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.SingleAdInFo
import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManage
import com.tokopedia.topads.common.domain.model.TopAdsGetShopInfo
import com.tokopedia.topads.common.domain.usecase.TopAdsGetAutoAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductManageUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetShopInfoV1UseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeePerformanceTopAdsViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: SeePerformanceTopAdsViewModel
    private val topAdsGetTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase =
        mockk(relaxed = true)
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase = mockk(relaxed = true)
    private val topAdsGetProductManageUseCase: TopAdsGetProductManageUseCase = mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase =
        mockk(relaxed = true)
    private val topAdsGetShopInfoV1UseCase: TopAdsGetShopInfoV1UseCase = mockk(relaxed = true)
    private val topAdsGetGroupIdUseCase: TopAdsGetPromoUseCase = mockk(relaxed = true)
    private val topAdsGetAutoAdsUseCase: TopAdsGetAutoAdsUseCase = mockk(relaxed = true)
    private val topAdsGetGroupInfoUseCase: GraphqlUseCase<TopAdsGroupsResponse> =
        mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        viewModel = SeePerformanceTopAdsViewModel(
            topAdsGetTotalAdsAndKeywordsUseCase,
            rule.dispatchers,
            userSession
        )
        viewModel.topAdsGetDepositUseCase = topAdsGetDepositUseCase
        viewModel.topAdsGetProductManageUseCase = topAdsGetProductManageUseCase
        viewModel.topAdsGetProductStatisticsUseCase = topAdsGetProductStatisticsUseCase
        viewModel.topAdsGetShopInfoV1UseCase = topAdsGetShopInfoV1UseCase
        viewModel.topAdsGetGroupIdUseCase = topAdsGetGroupIdUseCase
        viewModel.topAdsGetAutoAdsUseCase = topAdsGetAutoAdsUseCase
        viewModel.topAdsGetGroupInfoUseCase = topAdsGetGroupInfoUseCase
        viewModel.topAdsProductActionUseCase = topAdsProductActionUseCase
    }

    @Test
    fun `getTopAdsDeposit success`() {
        val data = Deposit()
        val result = Success(data)
        coEvery { topAdsGetDepositUseCase.execute(any(), any()) } answers {
            firstArg<(Deposit) -> Unit>().invoke(data)
        }
        viewModel.getTopAdsDeposit()
        assertEquals(result, viewModel.topAdsDeposits.value)
    }

    @Test
    fun `getTopAdsDeposit failure`() {
        val throwable = Throwable()
        coEvery { topAdsGetDepositUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getTopAdsDeposit()
        assertEquals(Fail(throwable), viewModel.topAdsDeposits.value)
    }

    @Test
    fun `getProductManage success`() {
        val response = TopAdsGetProductManage()
        val adId = response.data.adId
        coEvery { topAdsGetProductManageUseCase.executeOnBackground() } answers {
            response
        }
        viewModel.getProductManage(String.EMPTY)
        assertEquals(adId, viewModel.adId.value)
        assertEquals(response, viewModel.topAdsGetProductManage.value)
    }

    @Test
    fun `getProductManage failure`() {
        val throwable = Throwable()
        coEvery { topAdsGetProductManageUseCase.executeOnBackground() } throws throwable
        viewModel.getProductManage(String.EMPTY)
        coVerify { topAdsGetProductManageUseCase.executeOnBackground() }
    }

    @Test
    fun `getShopInfo success`() {
        val response = TopAdsGetShopInfo()
        coEvery { topAdsGetShopInfoV1UseCase.executeOnBackground() } answers {
            response
        }
        viewModel.getShopInfo()
        assertEquals(response, viewModel.topAdsGetShopInfo.value)
    }

    @Test
    fun `getShopInfo failure`() {
        val throwable = Throwable()
        coEvery { topAdsGetShopInfoV1UseCase.executeOnBackground() } throws throwable
        viewModel.getShopInfo()
        coVerify { topAdsGetShopInfoV1UseCase.executeOnBackground() }
    }

    @Test
    fun `getPromoInfo success`() {
        val response = SingleAdInFo()
        coEvery {
            topAdsGetGroupIdUseCase.setParams(any(), any())
            topAdsGetGroupIdUseCase.execute(any(), any()) } answers {
            firstArg<(SingleAdInFo) -> Unit>().invoke(response)
        }
        viewModel.getPromoInfo()
        assertEquals(response, viewModel.topAdsPromoInfo.value)
    }

    @Test
    fun `getPromoInfo failure`() {
        val throwable: Throwable = mockk(relaxed = true)
        coEvery { topAdsGetGroupIdUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        viewModel.getPromoInfo()
        coVerify { throwable.printStackTrace() }
    }

    @Test
    fun `getTopAdsProductStatistics success`() {
        val response = ProductStatisticsResponse()
        coEvery { topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), any()) } answers {
            firstArg<(ProductStatisticsResponse) -> Unit>().invoke(response)
        }
        val resources: Resources = mockk(relaxed = true)
        viewModel.getTopAdsProductStatistics(resources, String.EMPTY, String.EMPTY, Int.ZERO)
        assertEquals(Success(response), viewModel.productStatistics.value)
        assertEquals(Int.ZERO, viewModel.goalId.value)
    }

    @Test
    fun `getTopAdsProductStatistics failure`() {
        val throwable = Throwable()
        coEvery { topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
        val resources: Resources = mockk(relaxed = true)
        viewModel.getTopAdsProductStatistics(resources, String.EMPTY, String.EMPTY, Int.ZERO)
        assertEquals(Fail(throwable), viewModel.productStatistics.value)
    }

    @Test
    fun `setProductAction success`() {
        viewModel.setProductAction(String.EMPTY, listOf(), String.EMPTY)
        coVerify { topAdsProductActionUseCase.execute(any()) }
    }

    @Test
    fun `setProductAction failure`() {
        val throwable: Throwable = mockk(relaxed = true)
        coEvery {
            topAdsProductActionUseCase.execute(any())
        } answers {
            throw throwable
        }
        viewModel.setProductAction(String.EMPTY, listOf(), String.EMPTY)
        coVerify { throwable.printStackTrace() }
    }

    @Test
    fun `getGroupInfo success`() {
        val response = TopAdsGroupsResponse()
        coEvery {
            topAdsGetGroupInfoUseCase.executeOnBackground()
        } answers {
            response
        }
        viewModel.getGroupInfo()
        assertEquals(response, viewModel.topAdsGetGroupInfo.value)
    }

    @Test
    fun `getGroupInfo failure`() {
        val throwable = Throwable()
        coEvery {
            topAdsGetGroupInfoUseCase.executeOnBackground()
        } throws throwable
        viewModel.getGroupInfo()
        coVerify { topAdsGetGroupInfoUseCase.executeOnBackground() }
    }

    @Test
    fun `getTotalAdsAndKeywordsCount success`() {
        val response = TotalProductKeyResponse()
        val data = response.topAdsGetTotalAdsAndKeywords.data
        coEvery {
            topAdsGetTotalAdsAndKeywordsUseCase(any())
        } answers {
            response
        }
        viewModel.getTotalAdsAndKeywordsCount()
        assertEquals(data, viewModel.totalAdsAndKeywordsCount.value)
    }

    @Test
    fun `getTotalAdsAndKeywordsCount failure`() {
        val throwable = Throwable()
        coEvery {
            topAdsGetTotalAdsAndKeywordsUseCase(any())
        } throws throwable
        viewModel.getTotalAdsAndKeywordsCount()
        coVerify { topAdsGetTotalAdsAndKeywordsUseCase(any()) }
    }

    @Test
    fun `checkIsSingleAds true`() {
        `getProductManage success`()
        `getPromoInfo success`()
        viewModel.checkIsSingleAds()
        assertEquals(true, viewModel.isSingleAds.value)
    }

    @Test
    fun `getAutoAdsInfo success`() {
        val response = AutoAdsResponse.TopAdsGetAutoAds()
        coEvery { topAdsGetAutoAdsUseCase.executeOnBackground() } answers {
            response
        }
        viewModel.getAutoAdsInfo()
        assertEquals(response, viewModel.topAdsGetAutoAds.value)
    }

    @Test
    fun `getAutoAdsInfo failure`() {
        val throwable = Throwable()
        coEvery { topAdsGetAutoAdsUseCase.executeOnBackground() } throws throwable
        viewModel.getAutoAdsInfo()
        coVerify { topAdsGetAutoAdsUseCase.executeOnBackground() }
    }

}
