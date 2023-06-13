package com.tokopedia.topads.detail_sheet.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.SingleAdInFo
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Pika on 25/11/20.
 */
class TopAdsSheetViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase =
        mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase =
        mockk(relaxed = true)
    private val topAdsGetGroupIdUseCase: TopAdsGetPromoUseCase = mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val topAdsGetAutoAdsStatusUseCase: GraphqlUseCase<TopAdsAutoAds.Response> =
        mockk(relaxed = true)

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TopAdsSheetViewModel
    private lateinit var resources: Resources
    private lateinit var adIds: List<String>

    @Before
    fun setUp() {
        viewModel = TopAdsSheetViewModel(topAdsGetGroupProductDataUseCase,
            topAdsGetProductStatisticsUseCase,
            topAdsGetGroupIdUseCase,
            topAdsProductActionUseCase,
            topAdsGetAutoAdsStatusUseCase,
            testDispatcher)
        MockKAnnotations.init(this)
        resources = mockk(relaxed = true)
        adIds = mutableListOf()
    }

    @Test
    fun `getProductStats success check`() {
        val expected = ProductStatisticsResponse()
        var actual: List<WithoutGroupDataItem>? = null

        coEvery {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(captureLambda(),
                any())
        } answers {
            firstArg<(ProductStatisticsResponse) -> Unit>().invoke(expected)
        }

        viewModel.getProductStats(resources, listOf()) {
            actual = it
        }
        Assert.assertEquals(expected.getDashboardProductStatistics.data, actual)
    }

    @Test
    fun `getProductStats error check`() {
        val exception = spyk(Throwable())
        every {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        viewModel.getProductStats(resources, listOf()) {}

        verify {
            exception.printStackTrace()
        }
    }

    @Test
    fun `getGroupProductData success check`() {
        val expected = NonGroupResponse()
        var actual: List<WithoutGroupDataItem>? = null

        every {
            topAdsGetGroupProductDataUseCase.setParams(any(), any(),
                any(), any(), any(), any(), any(), any(), any())
        } returns mockk()

        coEvery { topAdsGetGroupProductDataUseCase.execute(any()) } returns expected

        viewModel.getGroupProductData("0") {
            actual = it
        }

        Assert.assertEquals(expected.topadsDashboardGroupProducts.data, actual)
    }

    @Test
    fun `getGroupProductData error check`() {
        val exception = spyk(Exception("my excep"))

        coEvery {
            topAdsGetGroupProductDataUseCase.execute(any())
        } throws exception
        viewModel.getGroupProductData("0") {}

        verify {
            exception.printStackTrace()
        }
    }

    @Test
    fun `getGroupId success check`() {
        val expected = SingleAdInFo()
        var actual: List<SingleAd>? = null

        every {
            topAdsGetGroupIdUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(SingleAdInFo) -> Unit>().invoke(expected)
        }
        viewModel.getGroupId("123", "456") {
            actual = it
        }

        Assert.assertEquals(expected.topAdsGetPromo.data, actual)
    }

    @Test
    fun `test on error getGroupId`() {
        val exception = spyk(Throwable("my excep"))
        every {
            topAdsGetGroupIdUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        viewModel.getGroupId("", "") {}

        verify {
            exception.printStackTrace()
        }
    }

    @Test
    fun testSetProductAction() {
        var successCalled = false
        viewModel.setProductAction({
            successCalled = true
        }, "123", adIds, "filter")

        coVerify {
            topAdsProductActionUseCase.execute(any())
        }
        Assert.assertTrue(successCalled)
    }

    @Test
    fun `test on error testSetProductAction`() {
        val exception = spyk(Exception("my excep"))

        coEvery { topAdsProductActionUseCase.execute(any()) } throws exception
        viewModel.setProductAction({}, "", listOf(), "")

        coVerify {
            exception.printStackTrace()
        }
    }

    @Test
    fun testGetAutoAdsStatus() {
        val data = TopAdsAutoAds.Response(mockk(relaxed = true))
        every {
            topAdsGetAutoAdsStatusUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(TopAdsAutoAds.Response) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getAutoAdsStatus("123", resources)

        verify {
            topAdsGetAutoAdsStatusUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test auto ads success`() {
        val data = TopAdsAutoAds.Response(mockk(relaxed = true))
        val autoAdsObserver: Observer<TopAdsAutoAdsData> = mockk(relaxed = true)

        viewModel.autoAdsData.observeForever(autoAdsObserver)
        every {
            topAdsGetAutoAdsStatusUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(TopAdsAutoAds.Response) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getAutoAdsStatus("123", resources)

        verify {
            autoAdsObserver.onChanged(data.autoAds.data)
        }

    }

    @Test
    fun `test on error testGetAutoAdsStatus`() {
        val exception = spyk(Throwable("my excep"))
        every {
            topAdsGetAutoAdsStatusUseCase.execute(any(), captureLambda())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(exception)
        }

        viewModel.getAutoAdsStatus("123", resources)

        verify {
            exception.printStackTrace()
        }
    }

    @Test
    fun testOnClear() {
        viewModel.onCleared()
        verify { topAdsGetProductStatisticsUseCase.cancelJobs() }
        verify { topAdsGetGroupIdUseCase.cancelJobs() }
        verify { topAdsGetAutoAdsStatusUseCase.cancelJobs() }
    }
}

