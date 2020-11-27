package com.tokopedia.topads.detail_sheet.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.ProductActionResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.detail_sheet.R
import com.tokopedia.topads.detail_sheet.data.AdData
import com.tokopedia.topads.detail_sheet.data.AdInfo
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any

/**
 * Created by Pika on 25/11/20.
 */
class TopAdsSheetViewModelTest : TestCase() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase = mockk(relaxed = true)
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase = mockk(relaxed = true)
    private val topAdsGetGroupIdUseCase: GraphqlUseCase<AdInfo> = mockk(relaxed = true)
    private val topAdsProductActionUseCase: TopAdsProductActionUseCase = mockk(relaxed = true)
    private val topAdsGetAutoAdsStatusUseCase: GraphqlUseCase<TopAdsAutoAds.Response> = mockk(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()
    private var autoAdsObserver = mockk<Observer<TopAdsAutoAdsData>>(relaxed = true)


    private lateinit var viewModel: TopAdsSheetViewModel
    private lateinit var resources: Resources
    private lateinit var adIds: List<String>

    public override fun setUp() {
        viewModel = TopAdsSheetViewModel(topAdsGetGroupProductDataUseCase, topAdsGetProductStatisticsUseCase, topAdsGetGroupIdUseCase, topAdsProductActionUseCase, topAdsGetAutoAdsStatusUseCase, testDispatcher)
        resources = mockk(relaxed = true)
        viewModel.autoAdsData.observeForever(autoAdsObserver)
        adIds = mutableListOf()
    }

    fun testGetProductStats() {
        val data = WithoutGroupDataItem()
        every {
            topAdsGetProductStatisticsUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(WithoutGroupDataItem) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getProductStats(resources, adIds, ) {}

        verify {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `test on error testGetProductStats`() {
        val exception = Exception("my excep")
        every {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
        }
        viewModel.getProductStats(resources, adIds, ) {}

        verify {
            topAdsGetProductStatisticsUseCase.executeQuerySafeMode(any(), any())
        }
    }


    @Test
    fun `test setting queries getGroupProductData`() {
        viewModel.getGroupProductData(resources, 1) {}
        verify { topAdsGetGroupProductDataUseCase.setQueryString(any()) }
    }

    fun testGetGroupId() {
        val data = AdInfo()
        every {
            topAdsGetGroupIdUseCase.execute(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(AdInfo) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.getGroupId("123", "456", ) {}

        verify {
            topAdsGetGroupIdUseCase.execute(any(), any())
        }
    }

    @Test
    fun `test on error testGetGroupId`() {
        val exception = Exception("my excep")
        every {
            topAdsGetGroupIdUseCase.execute(any(), captureLambda())
        } answers {
            val onError = lambda<(Exception) -> Unit>()
            onError.invoke(exception)
        }
        viewModel.getGroupId("123", "456", ) {}
        verify {
            topAdsGetGroupIdUseCase.execute(any(), any())
        }
    }

    fun testSetProductAction() {
        val data = ProductActionResponse()
        every {
            topAdsProductActionUseCase.executeQuerySafeMode(captureLambda(), any())
        } answers {
            val onSuccess = lambda<(ProductActionResponse) -> Unit>()
            onSuccess.invoke(data)
        }
        viewModel.setProductAction({}, "123", adIds, resources, "filter")

        verify {
            topAdsProductActionUseCase.executeQuerySafeMode(any(), any())
        }
    }

    @Test
    fun `test on error testSetProductAction`() {
        val exception = Exception("my excep")
        every {
            topAdsProductActionUseCase.executeQuerySafeMode(any(), captureLambda())
        } answers {
            val onError = lambda<(Exception) -> Unit>()
            onError.invoke(exception)
        }
        viewModel.setProductAction({}, "123", adIds, resources, "filter")

        verify {
            topAdsProductActionUseCase.executeQuerySafeMode(any(), any())
        }
    }

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
    fun `test auto ads success`(){
        val data = TopAdsAutoAds.Response(mockk(relaxed = true))
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
        val exception = Exception("my excep")
        every {
            topAdsGetAutoAdsStatusUseCase.execute(any(), captureLambda())
        } answers {
            val onError = lambda<(Exception) -> Unit>()
            onError.invoke(exception)
        }
        viewModel.getAutoAdsStatus("123", resources)

        verify {
            topAdsGetAutoAdsStatusUseCase.execute(any(), any())
        }
    }

    fun testOnClear() {
        viewModel.onCleared()
        verify { topAdsGetGroupProductDataUseCase.unsubscribe() }
        verify { topAdsGetProductStatisticsUseCase.cancelJobs() }
        verify { topAdsGetGroupIdUseCase.cancelJobs() }
        verify { topAdsProductActionUseCase.cancelJobs() }
        verify { topAdsGetAutoAdsStatusUseCase.cancelJobs() }
    }
}

